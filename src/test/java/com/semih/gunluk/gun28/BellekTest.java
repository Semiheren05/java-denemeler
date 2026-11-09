package com.semih.gunluk.gun28;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BellekTest {

    // --- Değerle geçirme ---

    @Test
    void metotNesneninIciniDegistirirseCagiranGorur() {
        Sepet sepet = new Sepet();
        sepet.ekle("Klavye");

        SepetIslemleri.hediyeEkle(sepet);

        assertEquals(List.of("Klavye", "Hediye paketi"), sepet.urunler());
    }

    @Test
    void parametreyeYeniNesneAtamakCagiraniEtkilemez() {
        Sepet sepet = new Sepet();
        sepet.ekle("Klavye");

        SepetIslemleri.yanlisBosalt(sepet);
        assertEquals(List.of("Klavye"), sepet.urunler());   // boşalmadı

        SepetIslemleri.dogruBosalt(sepet);
        assertEquals(List.of(), sepet.urunler());
    }

    @Test
    void ilkelTipteDegerinKendisiKopyalanir() {
        int fiyat = 1_000;

        int indirimli = SepetIslemleri.indirimliFiyat(fiyat);

        assertEquals(900, indirimli);
        assertEquals(1_000, fiyat);
    }

    @Test
    void kopyalanmayanListeDisaridanDegisir() {
        List<String> urunler = new ArrayList<>(List.of("Klavye"));
        HataliKampanya hatali = new HataliKampanya(urunler);
        Kampanya dogru = new Kampanya(urunler);

        urunler.add("Laptop");   // kampanyalar oluşturulduktan sonra

        assertTrue(hatali.kapsiyorMu("Laptop"));    // istemeden kampanyaya girdi
        assertFalse(dogru.kapsiyorMu("Laptop"));
    }

    @Test
    void finalAlanIcininDegismesiniEngellemez() {
        final List<String> liste = new ArrayList<>();
        liste.add("değişti");   // final: liste = new ArrayList<>() derlenmez, ama bu derlenir

        assertEquals(1, liste.size());
    }

    // --- String ve Integer: == adres, equals içerik ---

    @Test
    void stringHavuzuVeNew() {
        String a = "semih";
        String b = "sem" + "ih";              // derleme anında birleşir, havuzdaki aynı nesne
        String c = new String("semih");       // heap'te yeni nesne
        String parca = "sem";
        String d = parca + "ih";              // çalışma anında birleşir: yeni nesne

        assertSame(a, b);
        assertNotSame(a, c);
        assertNotSame(a, d);
        assertTrue(a.equals(c) && a.equals(d));   // içerik hep aynı
        assertSame(a, d.intern());                // intern(): havuzdakini ver
    }

    @Test
    void integerOnbellegi127deBiter() {
        Integer kucuk1 = 127;
        Integer kucuk2 = 127;
        Integer buyuk1 = 128;
        Integer buyuk2 = 128;

        // -128..127 arası kutular önbellekten gelir: == tesadüfen çalışır
        assertTrue(kucuk1 == kucuk2);
        // 128'de iki ayrı nesne: == false, equals true. Bu yüzden kutular hep equals ile karşılaştırılır
        assertFalse(buyuk1 == buyuk2);
        assertTrue(buyuk1.equals(buyuk2));
    }

    @Test
    void nullKutuAcilincaNullPointerException() {
        Integer stok = null;   // ör. Map.get bulamadı

        assertThrows(NullPointerException.class, () -> {
            int adet = stok;   // gizli stok.intValue()
            assertEquals(0, adet);
        });
    }

    // --- Stack ---

    @Test
    void derinOzyinelemeStackiTasirir() {
        assertEquals(5_050, Ozyineleme.toplamOzyinelemeli(100));
        assertThrows(StackOverflowError.class, () -> Ozyineleme.toplamOzyinelemeli(10_000_000));
        // Döngü tek çerçevede kalır
        assertEquals(50_000_005_000_000L, Ozyineleme.toplamDongulu(10_000_000));
    }

    @Test
    void stackThreadeOzel() throws InterruptedException {
        // Stack boyutu thread başına; 512 MB'lık stack'i olan bir thread aynı özyinelemeyi kaldırır
        AtomicLong sonuc = new AtomicLong();
        Thread genisStackli = new Thread(null,
                () -> sonuc.set(Ozyineleme.toplamOzyinelemeli(1_000_000)), "genis-stack", 512L * 1024 * 1024);
        genisStackli.start();
        genisStackli.join();

        assertEquals(500_000_500_000L, sonuc.get());
    }
}
