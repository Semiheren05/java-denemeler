package com.semih.gunluk.gun14;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Binlerce SMS'i aynı anda gönderir. Her gönderim bir HTTP çağrısı gibi bekleyen (bloklayan)
 * bir iş; bu tür işler için Java 21'in <b>virtual thread</b>'leri tasarlandı.
 *
 * <ul>
 *   <li><b>Platform thread</b> bir işletim sistemi thread'i: her biri ~1 MB yığın belleği ayırıyor,
 *       binlercesi açılamıyor. Bu yüzden sabit bir havuz (ör. 10 thread) kullanılıyor ve
 *       10.000 iş bu 10 thread'i sırayla bekliyor.</li>
 *   <li><b>Virtual thread</b> JVM'in yönettiği hafif bir thread: bekleme sırasında altındaki
 *       platform thread'i bırakıyor. Her işe ayrı bir virtual thread açmak ucuz; havuz gerekmiyor.</li>
 * </ul>
 *
 * <p>Thread ucuz olsa da karşıdaki servis sınırsız değil. {@link Semaphore} aynı anda en fazla
 * {@code esZamanliSinir} gönderime izin veriyor; fazlası sırasını bekliyor.
 */
public class TopluSmsGonderici {

    private final SmsServisi servis;
    private final Semaphore izinler;

    public TopluSmsGonderici(SmsServisi servis, int esZamanliSinir) {
        if (esZamanliSinir < 1) {
            throw new IllegalArgumentException("Eş zamanlı sınır en az 1 olmalı: " + esZamanliSinir);
        }
        this.servis = servis;
        this.izinler = new Semaphore(esZamanliSinir);
    }

    /** Her bildirime ayrı bir virtual thread. */
    public GonderimRaporu sanalThreadlerle(List<Bildirim> bildirimler) {
        // Java 19'dan beri ExecutorService AutoCloseable: close() bütün işlerin bitmesini bekler
        return calistir(bildirimler, Executors.newVirtualThreadPerTaskExecutor());
    }

    /** Karşılaştırma için: sabit sayıda platform thread'li klasik havuz. */
    public GonderimRaporu platformHavuzuyla(List<Bildirim> bildirimler, int havuzBoyutu) {
        return calistir(bildirimler, Executors.newFixedThreadPool(havuzBoyutu));
    }

    private GonderimRaporu calistir(List<Bildirim> bildirimler, ExecutorService havuz) {
        AtomicInteger basarili = new AtomicInteger();
        AtomicInteger basarisiz = new AtomicInteger();
        long basla = System.nanoTime();
        try (havuz) {
            for (Bildirim bildirim : bildirimler) {
                havuz.submit(() -> {
                    if (gonder(bildirim)) {
                        basarili.incrementAndGet();
                    } else {
                        basarisiz.incrementAndGet();
                    }
                });
            }
        }
        return new GonderimRaporu(basarili.get(), basarisiz.get(), Duration.ofNanos(System.nanoTime() - basla));
    }

    private boolean gonder(Bildirim bildirim) {
        try {
            izinler.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
        try {
            servis.gonder(bildirim);
            return true;
        } catch (Exception e) {
            // Bir numaraya gidemeyen SMS diğerlerini durdurmuyor; rapora başarısız olarak giriyor
            return false;
        } finally {
            // İzin her durumda geri veriliyor, yoksa sınır her hatada bir azalır
            izinler.release();
        }
    }
}
