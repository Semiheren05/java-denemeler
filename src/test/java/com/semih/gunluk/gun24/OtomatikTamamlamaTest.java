package com.semih.gunluk.gun24;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OtomatikTamamlamaTest {

    private OtomatikTamamlama tamamlama;

    @BeforeEach
    void setUp() {
        tamamlama = new OtomatikTamamlama();
        ara("istanbul", 5);
        ara("istasyon", 2);
        ara("istek", 2);
        ara("izmir", 4);
        ara("ankara", 3);
    }

    @Test
    void onekleBaslayanlarEnCokArananOnce() {
        assertEquals(List.of(new Oneri("istanbul", 5), new Oneri("istasyon", 2), new Oneri("istek", 2)),
                tamamlama.oneriler("ist", 10));
    }

    @Test
    void adetSiniriEnIyileriBirakir() {
        assertEquals(List.of(new Oneri("istanbul", 5), new Oneri("istasyon", 2)), tamamlama.oneriler("is", 2));
    }

    @Test
    void bosOnekEnPopulerleriVerir() {
        assertEquals(List.of(new Oneri("istanbul", 5), new Oneri("izmir", 4), new Oneri("ankara", 3)),
                tamamlama.oneriler("", 3));
    }

    @Test
    void turkceBuyukKucukHarfAyrimiYok() {
        tamamlama.ara("İSTANBUL");
        tamamlama.ara("Iğdır");

        assertEquals(new Oneri("istanbul", 6), tamamlama.oneriler("İst", 1).get(0));
        assertEquals(List.of(new Oneri("ığdır", 1)), tamamlama.oneriler("IĞ", 5));
    }

    @Test
    void sadeceTamKelimelerKelimeSayilir() {
        assertTrue(tamamlama.iceriyorMu("istek"));
        assertFalse(tamamlama.iceriyorMu("ist"));     // ağaçta bir yol, ama kelime değil
        assertFalse(tamamlama.iceriyorMu("istekler"));
    }

    @Test
    void onekSayisiAgaciGezmedenBulunur() {
        assertEquals(3, tamamlama.onekSayisi("ist"));
        assertEquals(4, tamamlama.onekSayisi("i"));
        assertEquals(5, tamamlama.onekSayisi(""));
        assertEquals(0, tamamlama.onekSayisi("x"));
    }

    @Test
    void bilinmeyenOnekBosListe() {
        assertEquals(List.of(), tamamlama.oneriler("bursa", 5));
    }

    @Test
    void silinenKelimeninKullanilmayanDallariKoparilir() {
        int once = tamamlama.dugumSayisi();

        assertTrue(tamamlama.sil("istasyon"));

        // "ista" ortak değil: "istanbul" "istan..." üzerinden gidiyor; s-y-o-n düğümleri atıldı
        assertEquals(once - 4, tamamlama.dugumSayisi());
        assertFalse(tamamlama.iceriyorMu("istasyon"));
        assertTrue(tamamlama.iceriyorMu("istanbul"));
        assertEquals(2, tamamlama.onekSayisi("ist"));
        assertFalse(tamamlama.sil("istasyon"));
    }

    @Test
    void baskaKelimeninOnekiOlanKelimeSilinince() {
        tamamlama.ara("is");

        tamamlama.sil("is");

        // Hiçbir düğüm atılmadı; "is" artık kelime değil, ama ondan geçen kelimeler duruyor
        assertFalse(tamamlama.iceriyorMu("is"));
        assertEquals(3, tamamlama.oneriler("is", 10).size());
    }

    @Test
    void rastgeleKelimelerdeKabaKuvvetleAyniOneriler() {
        Random rastgele = new Random(24);
        OtomatikTamamlama trie = new OtomatikTamamlama();
        Map<String, Long> sayac = new HashMap<>();
        for (int i = 0; i < 3_000; i++) {
            String kelime = rastgeleKelime(rastgele);
            trie.ara(kelime);
            sayac.merge(kelime, 1L, Long::sum);
        }

        for (int i = 0; i < 200; i++) {
            String onek = rastgeleKelime(rastgele).substring(0, 1 + rastgele.nextInt(2));
            List<Oneri> beklenen = new ArrayList<>();
            sayac.forEach((kelime, siklik) -> {
                if (kelime.startsWith(onek)) {
                    beklenen.add(new Oneri(kelime, siklik));
                }
            });
            beklenen.sort(Comparator.comparingLong(Oneri::siklik).reversed().thenComparing(Oneri::kelime));

            assertEquals(beklenen.subList(0, Math.min(5, beklenen.size())), trie.oneriler(onek, 5));
            assertEquals(beklenen.size(), trie.onekSayisi(onek));
        }
    }

    @Test
    void gecersizGirdilerReddedilir() {
        assertThrows(IllegalArgumentException.class, () -> tamamlama.ara("  "));
        assertThrows(IllegalArgumentException.class, () -> tamamlama.oneriler("ist", 0));
        assertThrows(IllegalArgumentException.class, () -> tamamlama.oneriler(null, 3));
    }

    private void ara(String kelime, int kez) {
        for (int i = 0; i < kez; i++) {
            tamamlama.ara(kelime);
        }
    }

    /** a–d harflerinden 1–5 harfli kelimeler: çok sayıda ortak önek olsun diye. */
    private static String rastgeleKelime(Random rastgele) {
        StringBuilder sb = new StringBuilder();
        int uzunluk = 2 + rastgele.nextInt(4);
        for (int i = 0; i < uzunluk; i++) {
            sb.append((char) ('a' + rastgele.nextInt(4)));
        }
        return sb.toString();
    }
}
