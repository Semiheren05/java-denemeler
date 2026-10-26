package com.semih.gunluk.gun10;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FaturaTest {

    private static final LocalDate TARIH = LocalDate.of(2026, 10, 26);

    private static Fatura.Builder ornek() {
        return Fatura.builder()
                .no("SE-2026-0042")
                .musteri("Ayşe Yılmaz")
                .tarih(TARIH)
                .kalem("Klavye", "750.00", 2, KdvOrani.YUZDE_20)
                .kalem("Java kitabı", "450.00", 1, KdvOrani.YUZDE_10);
    }

    @Test
    void indirimOnceUygulanirKdvIndirimliTutarUzerindenHesaplanir() {
        Fatura fatura = ornek().indirim(10).build();

        assertEquals(new BigDecimal("1950.00"), fatura.araToplam());
        assertEquals(new BigDecimal("195.00"), fatura.indirimTutari());
        assertEquals(Map.of(KdvOrani.YUZDE_20, new BigDecimal("270.00"), KdvOrani.YUZDE_10, new BigDecimal("40.50")),
                fatura.kdvDokumu());
        assertEquals(new BigDecimal("310.50"), fatura.kdvToplami());
        assertEquals(new BigDecimal("2065.50"), fatura.genelToplam());
    }

    @Test
    void indirimYoksaGenelToplamAraToplamArtiKdv() {
        Fatura fatura = ornek().build();

        assertEquals(new BigDecimal("0.00"), fatura.indirimTutari());
        assertEquals(new BigDecimal("345.00"), fatura.kdvToplami());
        assertEquals(new BigDecimal("2295.00"), fatura.genelToplam());
    }

    @Test
    void kuruslarHerAdimdaYuvarlanirVeToplamlarTutar() {
        Fatura fatura = Fatura.builder().no("1").musteri("Ali").tarih(TARIH)
                .kalem("Kablo", "33.33", 1, KdvOrani.YUZDE_20)
                .indirim(15)
                .build();

        // 33.33 × %85 = 28.3305 → 28.33; KDV 28.33 × %20 = 5.666 → 5.67
        assertEquals(new BigDecimal("5.00"), fatura.indirimTutari());
        assertEquals(new BigDecimal("5.67"), fatura.kdvToplami());
        assertEquals(new BigDecimal("34.00"), fatura.genelToplam());
    }

    @Test
    void kdvDokumundeSadeceKullanilanOranlarVar() {
        Fatura fatura = Fatura.builder().no("2").musteri("Deniz").tarih(TARIH)
                .kalem("Ekmek", "15.00", 4, KdvOrani.YUZDE_1)
                .build();

        assertEquals(Map.of(KdvOrani.YUZDE_1, new BigDecimal("0.60")), fatura.kdvDokumu());
    }

    @Test
    void yuzdeYuzIndirimdeOdenecekTutarSifir() {
        assertEquals(new BigDecimal("0.00"), ornek().indirim(100).build().genelToplam());
    }

    @Test
    void buildSonrasiBuildereEklenenKalemFaturayiDegistirmez() {
        Fatura.Builder builder = ornek();
        Fatura fatura = builder.build();

        builder.kalem("Mouse", "300.00", 1, KdvOrani.YUZDE_20);

        assertEquals(2, fatura.kalemler().size());
        assertEquals(3, builder.build().kalemler().size());
    }

    @Test
    void kalemListesiDisaridanDegistirilemez() {
        List<FaturaKalemi> kalemler = ornek().build().kalemler();

        assertThrows(UnsupportedOperationException.class,
                () -> kalemler.add(new FaturaKalemi("Hile", BigDecimal.ONE, 1, KdvOrani.YUZDE_20)));
        assertThrows(UnsupportedOperationException.class, () -> kalemler.remove(0));
    }

    @Test
    void toBuilderYeniFaturaUretirAsliDegismez() {
        Fatura asil = ornek().build();

        Fatura indirimli = asil.toBuilder().indirim(10).build();

        assertEquals(0, asil.indirimYuzdesi());
        assertEquals(new BigDecimal("2295.00"), asil.genelToplam());
        assertEquals(new BigDecimal("2065.50"), indirimli.genelToplam());
        assertEquals(asil.no(), indirimli.no());
        assertEquals(TARIH, indirimli.tarih());
    }

    @Test
    void zorunluAlanlarEksikseFaturaOlusmaz() {
        assertThrows(IllegalStateException.class,
                () -> Fatura.builder().musteri("Ali").kalem("Kalem", "10", 1, KdvOrani.YUZDE_20).build());
        assertThrows(IllegalStateException.class,
                () -> Fatura.builder().no("3").kalem("Kalem", "10", 1, KdvOrani.YUZDE_20).build());
        assertThrows(IllegalStateException.class,
                () -> Fatura.builder().no("3").musteri("Ali").build());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 101})
    void indirimSifirIleYuzArasindaOlmali(int yuzde) {
        assertThrows(IllegalArgumentException.class, () -> ornek().indirim(yuzde));
    }

    @Test
    void gecersizKalemEklenemez() {
        Fatura.Builder builder = ornek();

        assertThrows(IllegalArgumentException.class, () -> builder.kalem("Klavye", "750.00", 0, KdvOrani.YUZDE_20));
        assertThrows(IllegalArgumentException.class, () -> builder.kalem("Klavye", "-5", 1, KdvOrani.YUZDE_20));
        assertThrows(IllegalArgumentException.class, () -> builder.kalem(" ", "5", 1, KdvOrani.YUZDE_20));
    }

    @Test
    void kalemTutariBirimFiyatCarpiAdet() {
        FaturaKalemi kalem = new FaturaKalemi(" Klavye ", new BigDecimal("749.90"), 3, KdvOrani.YUZDE_20);

        assertEquals("Klavye", kalem.urun());
        assertEquals(new BigDecimal("2249.70"), kalem.tutar());
    }
}
