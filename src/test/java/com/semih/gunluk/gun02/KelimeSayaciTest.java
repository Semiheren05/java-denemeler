package com.semih.gunluk.gun02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KelimeSayaciTest {

    private final KelimeSayaci sayac = new KelimeSayaci();

    @Test
    void kelimeleriSayar() {
        Map<String, Integer> sonuc = sayac.say("elma armut elma kiraz elma armut");

        assertEquals(Map.of("elma", 3, "armut", 2, "kiraz", 1), sonuc);
    }

    @Test
    void buyukKucukHarfAyrimiYapmaz() {
        assertEquals(Map.of("java", 3), sayac.say("Java JAVA java"));
    }

    @Test
    @DisplayName("Türkçe I/İ harfleri doğru küçültülür")
    void turkceHarfleriDogruKucultur() {
        Map<String, Integer> sonuc = sayac.say("IŞIK ışık İstanbul istanbul");

        assertEquals(Map.of("ışık", 2, "istanbul", 2), sonuc);
    }

    @Test
    void noktalamaIsaretleriniYokSayar() {
        Map<String, Integer> sonuc = sayac.say("Merhaba, dünya! Merhaba... dünya? (merhaba)");

        assertEquals(Map.of("merhaba", 3, "dünya", 2), sonuc);
    }

    @Test
    @DisplayName("Kesme işaretli ekler kelimeden ayrılır")
    void kesmeIsaretliEkleriAyirir() {
        Map<String, Integer> sonuc = sayac.say("Semih'in kodu, Semih’e ait. Semih yazdı.");

        assertEquals(3, sonuc.get("semih"));
        assertTrue(!sonuc.containsKey("in") && !sonuc.containsKey("e"));
    }

    @Test
    void rakamlarKelimeSayilir() {
        assertEquals(Map.of("java", 2, "21", 2), sayac.say("Java 21, java 21"));
    }

    @Test
    void birdenFazlaBoslukVeSatirSonuSorunCikarmaz() {
        assertEquals(Map.of("bir", 1, "iki", 1), sayac.say("  bir \n\t  iki  "));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "!!! ... ,,,"})
    void bosMetinIcinBosSonucDoner(String metin) {
        assertEquals(Map.of(), sayac.say(metin));
        assertEquals(List.of(), sayac.enSikKelimeler(metin, 5));
        assertEquals(0, sayac.toplamKelime(metin));
    }

    @Test
    void enSikBesKelimeyiCoktanAzaSiralar() {
        String metin = "a a a a a b b b b c c c d d e f f f f f f";

        List<KelimeSayisi> sonuc = sayac.enSikKelimeler(metin, 5);

        assertEquals(List.of(
                new KelimeSayisi("f", 6),
                new KelimeSayisi("a", 5),
                new KelimeSayisi("b", 4),
                new KelimeSayisi("c", 3),
                new KelimeSayisi("d", 2)
        ), sonuc);
    }

    @Test
    @DisplayName("Eşit sayıda geçen kelimeler alfabetik sıralanır")
    void esitSayilardaAlfabetikSiralar() {
        List<KelimeSayisi> sonuc = sayac.enSikKelimeler("zeytin elma muz elma muz zeytin", 3);

        assertEquals(List.of(
                new KelimeSayisi("elma", 2),
                new KelimeSayisi("muz", 2),
                new KelimeSayisi("zeytin", 2)
        ), sonuc);
    }

    @Test
    void farkliKelimeSayisiNdenAzsaHepsiniDoner() {
        assertEquals(2, sayac.enSikKelimeler("bir iki", 5).size());
    }

    @Test
    void sifirIstenirseBosListeDoner() {
        assertEquals(List.of(), sayac.enSikKelimeler("bir iki", 0));
    }

    @Test
    void negatifNKabulEdilmez() {
        assertThrows(IllegalArgumentException.class, () -> sayac.enSikKelimeler("bir", -1));
    }

    @Test
    void toplamKelimeTekrarlariDaSayar() {
        assertEquals(6, sayac.toplamKelime("elma armut elma kiraz elma armut"));
    }

    @Test
    void sonucDisaridanDegistirilemez() {
        assertThrows(UnsupportedOperationException.class,
                () -> sayac.say("elma").put("armut", 1));
    }
}
