package com.semih.gunluk.gun05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SiraliListeTest {

    @Test
    void siraliListeEklemedeSirayiKorur() {
        SiraliListe<Integer> liste = new SiraliListe<>();

        liste.ekle(5);
        liste.ekle(1);
        liste.ekle(3);
        liste.ekle(3);

        assertEquals(List.of(1, 3, 3, 5), liste.listele());
    }

    @Test
    void siraliListeMetinleriAlfabetikTutar() {
        SiraliListe<String> liste = new SiraliListe<>();

        liste.hepsiniEkle(List.of("kiraz", "armut", "elma"));

        assertEquals(List.of("armut", "elma", "kiraz"), liste.listele());
    }

    @Test
    void enKucukVeEnBuyuk() {
        SiraliListe<Integer> liste = new SiraliListe<>();
        liste.hepsiniEkle(List.of(42, 7, 19));

        assertEquals(Optional.of(7), liste.enKucuk());
        assertEquals(Optional.of(42), liste.enBuyuk());
    }

    @Test
    void bosListedeEnKucukYoktur() {
        SiraliListe<Integer> liste = new SiraliListe<>();

        assertEquals(Optional.empty(), liste.enKucuk());
        assertEquals(Optional.empty(), liste.enBuyuk());
    }

    @Test
    void iceriyorIkiliAramayla() {
        SiraliListe<Integer> liste = new SiraliListe<>();
        liste.hepsiniEkle(List.of(2, 4, 6));

        assertTrue(liste.iceriyor(4));
        assertFalse(liste.iceriyor(5));
        assertFalse(liste.iceriyor(null));
    }

    @Test
    void bosElemanEklenemez() {
        SiraliListe<String> liste = new SiraliListe<>();

        assertThrows(NullPointerException.class, () -> liste.ekle(null));
    }

    @Test
    @DisplayName("Dışarıya verilen liste değiştirilemez, sıralama bozulamaz")
    void listeDisaridanDegistirilemez() {
        SiraliListe<Integer> liste = new SiraliListe<>();
        liste.ekle(1);

        assertThrows(UnsupportedOperationException.class, () -> liste.listele().add(0));
    }

    @Test
    void ciftTersCevrilince() {
        Cift<String, Integer> cift = new Cift<>("Semih", 25);

        Cift<Integer, String> ters = cift.tersCevir();

        assertEquals(new Cift<>(25, "Semih"), ters);
    }

    @Test
    @DisplayName("enBuyuk alt tiplerin listesini de kabul eder (? extends T)")
    void enBuyukHerKarsilastirilabilirTipleCalisir() {
        assertEquals(Optional.of("zeytin"), GenelAraclar.enBuyuk(List.of("elma", "zeytin", "armut")));
        assertEquals(Optional.of(9), GenelAraclar.enBuyuk(List.of(3, 9, 1)));
        assertEquals(Optional.empty(), GenelAraclar.enBuyuk(List.<Integer>of()));
    }

    @Test
    @DisplayName("PECS: Integer listesi Number listesine kopyalanabilir")
    void kopyalaUstTipliHedefeYazar() {
        List<Integer> kaynak = List.of(1, 2, 3);
        List<Number> hedef = new ArrayList<>(List.of(0.5));

        GenelAraclar.kopyala(kaynak, hedef);

        assertEquals(List.of(0.5, 1, 2, 3), hedef);
    }

    @Test
    void donusturHerElemaniCevirir() {
        List<Integer> uzunluklar = GenelAraclar.donustur(List.of("a", "bb", "ccc"), String::length);

        assertEquals(List.of(1, 2, 3), uzunluklar);
    }

    @Test
    void ayirKosulaGoreIkiyeBoler() {
        Cift<List<Integer>, List<Integer>> sonuc = GenelAraclar.ayir(List.of(1, 2, 3, 4, 5), n -> n % 2 == 0);

        assertEquals(List.of(2, 4), sonuc.birinci());
        assertEquals(List.of(1, 3, 5), sonuc.ikinci());
    }
}
