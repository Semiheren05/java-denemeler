package com.semih.gunluk.gun13;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SayfaSayaciTest {

    @Test
    void ayniAndaGelenZiyaretlerinHicbiriKaybolmaz() throws Exception {
        SayfaSayaci sayac = new SayfaSayaci();
        int threadSayisi = 8;
        int threadBasinaZiyaret = 10_000;
        // Bütün thread'ler hazır olunca aynı anda başlasın, çakışma ihtimali en yüksek olsun
        CountDownLatch basla = new CountDownLatch(1);

        // ExecutorService Java 19'dan beri AutoCloseable: close() işlerin bitmesini bekler
        try (ExecutorService havuz = Executors.newFixedThreadPool(threadSayisi)) {
            for (int t = 0; t < threadSayisi; t++) {
                havuz.submit(() -> {
                    basla.await();
                    for (int i = 0; i < threadBasinaZiyaret; i++) {
                        sayac.kaydet("/blog/java-21");
                    }
                    return null;
                });
            }
            basla.countDown();
        }

        assertEquals(80_000, sayac.sayi("/blog/java-21"));
        assertEquals(80_000, sayac.toplam());
    }

    @Test
    void farkliSayfalarAyriSayilir() throws Exception {
        SayfaSayaci sayac = new SayfaSayaci();
        List<String> sayfalar = List.of("/", "/blog", "/iletisim");

        try (ExecutorService havuz = Executors.newFixedThreadPool(6)) {
            for (int t = 0; t < 6; t++) {
                int numara = t;
                havuz.submit(() -> {
                    for (int i = 0; i < 1_000; i++) {
                        sayac.kaydet(sayfalar.get(numara % 3));
                    }
                });
            }
        }

        assertEquals(2_000, sayac.sayi("/"));
        assertEquals(2_000, sayac.sayi("/blog"));
        assertEquals(2_000, sayac.sayi("/iletisim"));
        assertEquals(6_000, sayac.toplam());
    }

    @Test
    void enCokZiyaretEdilenlerEsitlikteAlfabetik() {
        SayfaSayaci sayac = new SayfaSayaci();
        ziyaret(sayac, "/blog", 5);
        ziyaret(sayac, "/projeler", 3);
        ziyaret(sayac, "/", 3);
        ziyaret(sayac, "/iletisim", 1);

        assertEquals(List.of("/blog", "/", "/projeler"), sayac.enCokZiyaretEdilenler(3));
    }

    @Test
    void hicZiyaretEdilmemisSayfaSifir() {
        assertEquals(0, new SayfaSayaci().sayi("/yok"));
    }

    @Test
    void bosSayfaAdiKabulEdilmez() {
        assertThrows(IllegalArgumentException.class, () -> new SayfaSayaci().kaydet(" "));
    }

    private static void ziyaret(SayfaSayaci sayac, String sayfa, int kez) {
        for (int i = 0; i < kez; i++) {
            sayac.kaydet(sayfa);
        }
    }
}
