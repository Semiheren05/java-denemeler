package com.semih.gunluk.gun22;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LruOnbellekTest {

    @Test
    void dolunceEnUzunSuredirKullanilmayanAtilir() {
        LruOnbellek<String, String> onbellek = new LruOnbellek<>(3);
        onbellek.koy("/urun/1", "Klavye");
        onbellek.koy("/urun/2", "Mouse");
        onbellek.koy("/urun/3", "Monitör");

        onbellek.koy("/urun/4", "Kulaklık");

        assertEquals(Optional.empty(), onbellek.getir("/urun/1"));
        assertEquals(List.of("/urun/4", "/urun/3", "/urun/2"), onbellek.kullanimSirasi());
    }

    @Test
    void okumakKaydiTazeler() {
        LruOnbellek<String, String> onbellek = new LruOnbellek<>(3);
        onbellek.koy("a", "1");
        onbellek.koy("b", "2");
        onbellek.koy("c", "3");

        onbellek.getir("a");          // a artık en yeni
        onbellek.koy("d", "4");       // en eski b atılır

        assertEquals(List.of("d", "a", "c"), onbellek.kullanimSirasi());
    }

    @Test
    void varOlanAnahtaraYazmakBoyutuBuyutmezTazeler() {
        LruOnbellek<String, String> onbellek = new LruOnbellek<>(2);
        onbellek.koy("a", "1");
        onbellek.koy("b", "2");

        onbellek.koy("a", "yeni");

        assertEquals(2, onbellek.boyut());
        assertEquals(Optional.of("yeni"), onbellek.getir("a"));
        assertEquals(List.of("a", "b"), onbellek.kullanimSirasi());
    }

    @Test
    void kapasiteBirdeHepSonKalir() {
        LruOnbellek<Integer, String> onbellek = new LruOnbellek<>(1);
        onbellek.koy(1, "bir");
        onbellek.koy(2, "iki");

        assertEquals(List.of(2), onbellek.kullanimSirasi());
    }

    @Test
    void atilanKayitBildirilir() {
        List<String> atilanlar = new ArrayList<>();
        LruOnbellek<String, String> onbellek = new LruOnbellek<>(2, (anahtar, deger) -> atilanlar.add(anahtar + "=" + deger));

        onbellek.koy("a", "1");
        onbellek.koy("b", "2");
        onbellek.koy("a", "1b");   // güncelleme: atma yok
        onbellek.koy("c", "3");

        assertEquals(List.of("b=2"), atilanlar);
    }

    @Test
    void isabetOraniHesaplanir() {
        LruOnbellek<String, String> onbellek = new LruOnbellek<>(2);
        assertEquals(0.0, onbellek.isabetOrani());
        onbellek.koy("a", "1");

        onbellek.getir("a");
        onbellek.getir("a");
        onbellek.getir("a");
        onbellek.getir("yok");

        assertEquals(0.75, onbellek.isabetOrani());
    }

    @Test
    void hazirLinkedHashMapSurumuAyniSirayiTutar() {
        KisaLruOnbellek<String, String> kisa = new KisaLruOnbellek<>(3);
        kisa.put("a", "1");
        kisa.put("b", "2");
        kisa.put("c", "3");
        kisa.get("a");
        kisa.put("d", "4");

        // LinkedHashMap eskiden yeniye sıralıyor
        assertEquals(List.of("c", "a", "d"), new ArrayList<>(kisa.keySet()));
    }

    @Test
    void ikiSurumRastgeleIslemlerdeAyniDavranir() {
        Random rastgele = new Random(22);
        LruOnbellek<Integer, Integer> elle = new LruOnbellek<>(8);
        KisaLruOnbellek<Integer, Integer> hazir = new KisaLruOnbellek<>(8);

        for (int i = 0; i < 20_000; i++) {
            int anahtar = rastgele.nextInt(20);
            if (rastgele.nextBoolean()) {
                elle.koy(anahtar, i);
                hazir.put(anahtar, i);
            } else {
                assertEquals(Optional.ofNullable(hazir.get(anahtar)), elle.getir(anahtar));
            }
        }
        List<Integer> hazirSira = new ArrayList<>(hazir.keySet());
        Collections.reverse(hazirSira);
        assertEquals(hazirSira, elle.kullanimSirasi());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void kapasiteEnAzBir(int kapasite) {
        assertThrows(IllegalArgumentException.class, () -> new LruOnbellek<String, String>(kapasite));
    }

    @Test
    void bosAnahtarYaDaDegerKonamaz() {
        LruOnbellek<String, String> onbellek = new LruOnbellek<>(2);

        assertThrows(NullPointerException.class, () -> onbellek.koy(null, "1"));
        assertThrows(NullPointerException.class, () -> onbellek.koy("a", null));
    }
}
