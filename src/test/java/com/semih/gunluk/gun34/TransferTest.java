package com.semih.gunluk.gun34;

import org.junit.jupiter.api.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransferTest {

    private final TransferServisi servis = new TransferServisi();

    @Test
    void transferParayiTasir() {
        Hesap a = new Hesap(1, 10_000);
        Hesap b = new Hesap(2, 0);

        servis.transfer(a, b, 2_500);

        assertEquals(7_500, a.bakiye());
        assertEquals(2_500, b.bakiye());
    }

    @Test
    void yetersizBakiye_hicbirSeyDegismez_kilitlerBirakilir() {
        Hesap a = new Hesap(1, 100);
        Hesap b = new Hesap(2, 0);

        YetersizBakiye hata = assertThrows(YetersizBakiye.class, () -> servis.transfer(a, b, 101));

        assertEquals("Hesap 1: bakiye 100, istenen 101", hata.getMessage());
        assertEquals(100, a.bakiye());
        assertEquals(0, b.bakiye());
        assertFalse(a.kilit().isLocked());   // finally sayesinde istisnada da bırakıldı
        assertFalse(b.kilit().isLocked());
    }

    @Test
    void gecersizIstekler() {
        Hesap a = new Hesap(1, 100);

        assertThrows(IllegalArgumentException.class, () -> servis.transfer(a, a, 10));
        assertThrows(IllegalArgumentException.class, () -> servis.transfer(a, new Hesap(2, 0), 0));
    }

    @Test
    void parametreSirasiylaKilit_deadlockOlur() throws Exception {
        Hesap a = new Hesap(1, 1_000);
        Hesap b = new Hesap(2, 1_000);
        CyclicBarrier ikisiDeIlkKilitte = new CyclicBarrier(2);
        HataliTransferServisi hatali = new HataliTransferServisi(() -> {
            try {
                ikisiDeIlkKilitte.await(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
        Thread t1 = new Thread(() -> calistir(() -> hatali.transfer(a, b, 10)), "A'dan B'ye");
        Thread t2 = new Thread(() -> calistir(() -> hatali.transfer(b, a, 10)), "B'den A'ya");
        t1.start();
        t2.start();

        long[] kilitlenenler = deadlockBekle(5_000);

        t1.interrupt();   // lockInterruptibly sayesinde kurtarılabiliyorlar
        t2.interrupt();
        t1.join(5_000);
        t2.join(5_000);
        assertNotNull(kilitlenenler, "deadlock bulunamadı");
        assertEquals(2, kilitlenenler.length);
        assertEquals(2_000, a.bakiye() + b.bakiye());
    }

    @Test
    void ayniSiraylaKilit_karsilikliTransferlerBiter() throws Exception {
        Hesap a = new Hesap(1, 1_000_000);
        Hesap b = new Hesap(2, 1_000_000);

        try (ExecutorService havuz = Executors.newFixedThreadPool(8)) {
            for (int i = 0; i < 5_000; i++) {
                havuz.submit(() -> servis.transfer(a, b, 3));
                havuz.submit(() -> servis.transfer(b, a, 2));
            }
        }   // close() hepsinin bitmesini bekler; deadlock olsaydı test burada asılı kalırdı

        assertEquals(1_000_000 - 5_000, a.bakiye());
        assertEquals(1_000_000 + 5_000, b.bakiye());
    }

    @Test
    void cokHesapEszamanli_toplamParaKorunur() throws Exception {
        List<Hesap> hesaplar = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            hesaplar.add(new Hesap(i, 10_000));
        }
        try (ExecutorService havuz = Executors.newFixedThreadPool(8)) {
            for (int t = 0; t < 8; t++) {
                long tohum = t;
                havuz.submit(() -> {
                    Random r = new Random(tohum);
                    for (int i = 0; i < 2_000; i++) {
                        Hesap kaynak = hesaplar.get(r.nextInt(10));
                        Hesap hedef = hesaplar.get(r.nextInt(10));
                        if (kaynak == hedef) {
                            continue;
                        }
                        try {
                            servis.transfer(kaynak, hedef, 1 + r.nextInt(500));
                        } catch (YetersizBakiye e) {
                            // beklenen: reddedilen transfer hiçbir şeyi değiştirmez
                        }
                    }
                });
            }
        }

        assertEquals(100_000, hesaplar.stream().mapToLong(Hesap::bakiye).sum());
        assertTrue(hesaplar.stream().allMatch(h -> h.bakiye() >= 0));
    }

    @Test
    void tryLock_kilitBaskasindaysaSureDoluncaVazgecer() throws Exception {
        Hesap a = new Hesap(1, 1_000);
        Hesap b = new Hesap(2, 0);
        CountDownLatch tutuldu = new CountDownLatch(1);
        CountDownLatch birak = new CountDownLatch(1);
        Thread tutan = new Thread(() -> {
            b.kilit().lock();
            try {
                tutuldu.countDown();
                birak.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                b.kilit().unlock();
            }
        });
        tutan.start();
        tutuldu.await();

        long bas = System.nanoTime();
        boolean yapildi = servis.dene(a, b, 100, 200);
        long gecenMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - bas);
        birak.countDown();
        tutan.join();

        assertFalse(yapildi);
        assertTrue(gecenMs >= 190, "süre: " + gecenMs);
        assertEquals(1_000, a.bakiye());
        assertTrue(servis.dene(a, b, 100, 200));   // kilit boşalınca başarıyor
    }

    @Test
    void tryLock_vazgecipTekrarDeneyince_tersSiraliTransferlerDeBiter() throws Exception {
        Hesap a = new Hesap(1, 100_000);
        Hesap b = new Hesap(2, 100_000);

        try (ExecutorService havuz = Executors.newFixedThreadPool(2)) {
            havuz.submit(() -> tekrarlaDene(a, b, 1_000));   // kilitleri A, B sırasıyla istiyor
            havuz.submit(() -> tekrarlaDene(b, a, 1_000));   // B, A sırasıyla: lock() olsaydı kilitlenebilirdi
        }

        assertEquals(200_000, a.bakiye() + b.bakiye());
        assertEquals(100_000, a.bakiye());
    }

    private Void tekrarlaDene(Hesap kaynak, Hesap hedef, int adet) throws InterruptedException {
        for (int i = 0; i < adet; i++) {
            while (!servis.dene(kaynak, hedef, 7, 5)) {
                Thread.sleep(0, 1_000);   // vazgeçti: kilitleri bıraktı, kısa bir an bekleyip tekrar
            }
        }
        return null;
    }

    @Test
    void reentrantLock_ayniThreadTekrarAlabilir() {
        Hesap a = new Hesap(1, 500);

        a.kilit().lock();
        try {
            assertEquals(500, a.bakiye());   // bakiye() de kilit alıyor; aynı thread olduğu için kilitlenmiyor
            a.kilit().lock();
            assertEquals(2, a.kilit().getHoldCount());
            a.kilit().unlock();
        } finally {
            a.kilit().unlock();
        }
        assertFalse(a.kilit().isLocked());
    }

    @Test
    void condition_paraGelenceBekleyenCekimDevamEder() throws Exception {
        Hesap a = new Hesap(1, 0);
        boolean[] sonuc = new boolean[1];
        Thread cekim = new Thread(() -> {
            try {
                sonuc[0] = a.bekleVeCek(300, 5_000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        cekim.start();

        a.yatir(100);   // yetmiyor: uyanıp tekrar uyuyor
        a.yatir(250);
        cekim.join(5_000);

        assertTrue(sonuc[0]);
        assertEquals(50, a.bakiye());
        assertFalse(new Hesap(2, 0).bekleVeCek(1, 50));   // kimse yatırmazsa süre dolunca false
    }

    private static long[] deadlockBekle(long sureMs) throws InterruptedException {
        ThreadMXBean mx = ManagementFactory.getThreadMXBean();
        long son = System.currentTimeMillis() + sureMs;
        while (System.currentTimeMillis() < son) {
            long[] idler = mx.findDeadlockedThreads();   // ReentrantLock gibi kilitleri de görüyor
            if (idler != null) {
                return idler;
            }
            Thread.sleep(20);
        }
        return null;
    }

    @FunctionalInterface
    private interface Is {
        void calis() throws InterruptedException;
    }

    private static void calistir(Is is) {
        try {
            is.calis();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
