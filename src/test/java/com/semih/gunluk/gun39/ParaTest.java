package com.semih.gunluk.gun39;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParaTest {

    private static Para toplam(List<Para> paralar) {
        return paralar.stream().reduce(Para::topla).orElseThrow();
    }

    private static List<Para> tl(String... tutarlar) {
        return Arrays.stream(tutarlar).map(Para::tl).toList();
    }

    @Test
    void bigDecimalEqualsTuzagi_paraOlcegiSabitler() {
        assertNotEquals(new BigDecimal("10.5"), new BigDecimal("10.50"));            // ölçek farklı: equals false
        assertEquals(0, new BigDecimal("10.5").compareTo(new BigDecimal("10.50")));  // değer aynı

        assertEquals(Para.tl("10.5"), Para.tl("10.50"));
        assertEquals(new BigDecimal("10.50"), Para.tl("10.5").tutar());
    }

    @Test
    void kurusHanesiBirimdenGelir_fazlaOndalikReddedilir() {
        assertEquals(new BigDecimal("1500"), Para.of("1500", "JPY").tutar());   // yende kuruş yok

        IllegalArgumentException hata = assertThrows(IllegalArgumentException.class, () -> Para.tl("12.345"));
        assertEquals("12.345 TRY için en fazla 2 ondalık olabilir", hata.getMessage());
        assertThrows(IllegalArgumentException.class, () -> Para.of("10.5", "JPY"));
    }

    @Test
    void farkliBirimlerToplanmaz() {
        FarkliParaBirimi hata = assertThrows(FarkliParaBirimi.class, () -> Para.tl("10").topla(Para.of("10", "EUR")));

        assertEquals("TRY ile EUR doğrudan toplanamaz; önce kurla çevir", hata.getMessage());
    }

    @Test
    void esitDagit_kurusKaybolmaz() {
        List<Para> paylar = Para.tl("100").dagit(3);

        assertEquals(tl("33.34", "33.33", "33.33"), paylar);
        assertEquals(Para.tl("100"), toplam(paylar));
    }

    @ParameterizedTest
    @CsvSource({"0.01, 3", "1000, 7", "999.99, 12", "0.00, 4", "-10, 3", "12345.67, 1"})
    void esitDagit_toplamHepAyni(String tutar, int parca) {
        List<Para> paylar = Para.tl(tutar).dagit(parca);
        List<BigDecimal> tutarlar = paylar.stream().map(Para::tutar).toList();

        assertEquals(parca, paylar.size());
        assertEquals(Para.tl(tutar), toplam(paylar));
        BigDecimal enBuyuk = tutarlar.stream().max(BigDecimal::compareTo).orElseThrow();
        BigDecimal enKucuk = tutarlar.stream().min(BigDecimal::compareTo).orElseThrow();
        assertTrue(enBuyuk.subtract(enKucuk).compareTo(new BigDecimal("0.01")) <= 0);   // en fazla 1 kuruş fark
    }

    @Test
    void oranlaDagit_enBuyukKalan() {
        assertEquals(tl("500.00", "300.00", "200.00"), Para.tl("1000").oranlaDagit(List.of(50, 30, 20)));
        assertEquals(tl("0.02", "0.03"), Para.tl("0.05").oranlaDagit(List.of(3, 7)));   // 0,015 ve 0,035: eşit kalan, öndeki alır
        assertEquals(tl("33.34", "33.33", "33.33"), Para.tl("100").oranlaDagit(List.of(1, 1, 1)));
        assertEquals(tl("0.00", "0.01"), Para.tl("0.01").oranlaDagit(List.of(1, 2)));   // kalan büyük olan alır
        assertThrows(IllegalArgumentException.class, () -> Para.tl("1").oranlaDagit(List.of(0, 0)));
    }

    @Test
    void halfEven_ortalamadaYanliDegil() {
        BigDecimal halfUpHata = BigDecimal.ZERO;
        BigDecimal halfEvenHata = BigDecimal.ZERO;
        for (int i = 0; i < 1000; i++) {
            BigDecimal deger = BigDecimal.valueOf(i, 2).add(new BigDecimal("0.005"));   // 0,005  0,015  0,025 ...: hepsi tam yarıda
            halfUpHata = halfUpHata.add(deger.setScale(2, RoundingMode.HALF_UP).subtract(deger));
            halfEvenHata = halfEvenHata.add(deger.setScale(2, RoundingMode.HALF_EVEN).subtract(deger));
        }

        assertEquals(new BigDecimal("5.000"), halfUpHata);     // hep yukarı: 1000 × 0,005 TL fazla
        assertEquals(new BigDecimal("0.000"), halfEvenHata);   // çifte yuvarlıyor: yarısı yukarı, yarısı aşağı
    }

    @ParameterizedTest
    @CsvSource({"120.00, 20, 100.00, 20.00", "99.99, 20, 83.33, 16.66", "10.00, 10, 9.09, 0.91", "1.00, 1, 0.99, 0.01"})
    void kdvAyir_toplamFiyataEsit(String fiyat, int kdv, String net, String vergi) {
        List<Para> sonuc = Para.tl(fiyat).kdvAyir(kdv);

        assertEquals(tl(net, vergi), sonuc);
        assertEquals(Para.tl(fiyat), toplam(sonuc));
    }

    @Test
    void carp_yuvarlamaModuAcikca() {
        Para fiyat = Para.tl("19.99");

        assertEquals(Para.tl("16.99"), fiyat.carp(new BigDecimal("0.85"), RoundingMode.HALF_UP));     // 16,9915
        assertEquals(Para.tl("17.00"), fiyat.carp(new BigDecimal("0.85"), RoundingMode.CEILING));
        assertTrue(Para.tl("16.99").compareTo(Para.tl("17")) < 0);
    }

    @Test
    void turkceBicim() {
        String metin = Para.tl("1234.5").bicimle(Locale.forLanguageTag("tr-TR"));

        assertTrue(metin.contains("1.234,50"), metin);   // binlik ayırıcı nokta, ondalık virgül
        assertTrue(metin.contains("₺"), metin);
    }
}
