package com.semih.gunluk.gun03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotAnaliziTest {

    private static final Ogrenci ALI = new Ogrenci("Ali", "Bilgisayar", 95);
    private static final Ogrenci AYSE = new Ogrenci("Ayşe", "Bilgisayar", 88);
    private static final Ogrenci CAN = new Ogrenci("Can", "Elektrik", 72);
    private static final Ogrenci DENIZ = new Ogrenci("Deniz", "Elektrik", 58);
    private static final Ogrenci ECE = new Ogrenci("Ece", "Bilgisayar", 88);
    private static final Ogrenci FATMA = new Ogrenci("Fatma", "Makine", 40);

    private final NotAnalizi sinif = new NotAnalizi(List.of(ALI, AYSE, CAN, DENIZ, ECE, FATMA));
    private final NotAnalizi bosSinif = new NotAnalizi(List.of());

    @Test
    void sinifOrtalamasiniHesaplar() {
        // (95 + 88 + 72 + 58 + 88 + 40) / 6 = 441 / 6
        assertEquals(73.5, sinif.ortalama().orElseThrow(), 1e-9);
    }

    @Test
    void enYuksekVeEnDusukPuaniBulur() {
        assertEquals(ALI, sinif.enYuksek().orElseThrow());
        assertEquals(FATMA, sinif.enDusuk().orElseThrow());
    }

    @Test
    @DisplayName("İlk N sıralamasında eşit puanlılar alfabetik dizilir")
    void ilkNOgrenciyiSiralar() {
        assertEquals(List.of(ALI, AYSE, ECE), sinif.ilkN(3));
    }

    @Test
    void ilkNListeBoyundanBuyukseHepsiniDoner() {
        assertEquals(6, sinif.ilkN(100).size());
        assertEquals(List.of(), sinif.ilkN(0));
    }

    @Test
    void ilkNNegatifOlamaz() {
        assertThrows(IllegalArgumentException.class, () -> sinif.ilkN(-1));
    }

    @Test
    @DisplayName("Harf notuna göre gruplar; anahtarlar AA'dan FF'ye sıralı")
    void harfNotunaGoreGruplar() {
        Map<HarfNotu, List<String>> gruplar = sinif.harfNotunaGoreGrupla();

        assertEquals(List.of(HarfNotu.AA, HarfNotu.BA, HarfNotu.CC, HarfNotu.FD, HarfNotu.FF),
                List.copyOf(gruplar.keySet()));
        assertEquals(List.of("Ali"), gruplar.get(HarfNotu.AA));
        assertEquals(List.of("Ayşe", "Ece"), gruplar.get(HarfNotu.BA));
        assertEquals(List.of("Deniz"), gruplar.get(HarfNotu.FD));
    }

    @Test
    void harfNotuDagiliminiSayar() {
        assertEquals(Map.of(
                HarfNotu.AA, 1L,
                HarfNotu.BA, 2L,
                HarfNotu.CC, 1L,
                HarfNotu.FD, 1L,
                HarfNotu.FF, 1L
        ), sinif.harfNotuDagilimi());
    }

    @Test
    void bolumOrtalamalariniAlfabetikVerir() {
        Map<String, Double> ortalamalar = sinif.bolumOrtalamalari();

        assertEquals(List.of("Bilgisayar", "Elektrik", "Makine"), List.copyOf(ortalamalar.keySet()));
        assertEquals((95 + 88 + 88) / 3.0, ortalamalar.get("Bilgisayar"), 1e-9);
        assertEquals(65.0, ortalamalar.get("Elektrik"), 1e-9);
        assertEquals(40.0, ortalamalar.get("Makine"), 1e-9);
    }

    @Test
    void gecenVeKalanlariAyirir() {
        Map<Boolean, List<Ogrenci>> sonuc = sinif.gecenVeKalanlar();

        assertEquals(List.of(ALI, AYSE, CAN, ECE), sonuc.get(true));
        assertEquals(List.of(DENIZ, FATMA), sonuc.get(false));
    }

    @Test
    void istatistikleriTekSeferdeVerir() {
        IntSummaryStatistics ist = sinif.istatistikler();

        assertEquals(6, ist.getCount());
        assertEquals(40, ist.getMin());
        assertEquals(95, ist.getMax());
        assertEquals(441, ist.getSum());
    }

    @Test
    @DisplayName("Boş sınıfta hata yerine boş sonuç döner")
    void bosSinifGuvenlidir() {
        assertTrue(bosSinif.ortalama().isEmpty());
        assertTrue(bosSinif.enYuksek().isEmpty());
        assertTrue(bosSinif.enDusuk().isEmpty());
        assertEquals(Map.of(), bosSinif.harfNotunaGoreGrupla());
        assertEquals(Map.of(true, List.of(), false, List.of()), bosSinif.gecenVeKalanlar());
        assertEquals(0, bosSinif.istatistikler().getCount());
    }

    @Test
    void disaridakiListeDegisinceAnalizEtkilenmez() {
        List<Ogrenci> liste = new ArrayList<>(List.of(ALI, CAN));
        NotAnalizi analiz = new NotAnalizi(liste);

        liste.add(FATMA);

        assertEquals(2, analiz.istatistikler().getCount());
    }

    @ParameterizedTest
    @CsvSource({
            "100, AA", "90, AA", "89, BA", "85, BA", "84, BB", "75, CB",
            "70, CC", "65, DC", "60, DD", "59, FD", "50, FD", "49, FF", "0, FF"
    })
    void puandanHarfNotuHesaplar(int puan, HarfNotu beklenen) {
        assertEquals(beklenen, HarfNotu.puandan(puan));
    }

    @Test
    void ddVeUstuGecerFdVeFfKalir() {
        assertTrue(HarfNotu.DD.gecerMi());
        assertFalse(HarfNotu.FD.gecerMi());
        assertFalse(HarfNotu.FF.gecerMi());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 101})
    void gecersizPuanReddedilir(int puan) {
        assertThrows(IllegalArgumentException.class, () -> new Ogrenci("Ali", "Bilgisayar", puan));
        assertThrows(IllegalArgumentException.class, () -> HarfNotu.puandan(puan));
    }

    @Test
    void adVeBolumZorunludur() {
        assertThrows(IllegalArgumentException.class, () -> new Ogrenci(" ", "Bilgisayar", 50));
        assertThrows(IllegalArgumentException.class, () -> new Ogrenci("Ali", null, 50));
        assertThrows(IllegalArgumentException.class, () -> new NotAnalizi(null));
    }
}
