package com.semih.gunluk.gun37;

import java.time.Clock;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Kayan pencere (log): her anahtar için kabul edilen isteklerin zamanları bir kuyrukta. Yeni istek
 * geldiğinde pencereden çıkanlar baştan atılıyor, kalan sayı limitten azsa izin. "Son 60 saniyede"
 * sorusunu tam doğru cevaplıyor; bedeli anahtar başına limit kadar zaman damgası.
 */
public class KayanPencere implements HizSinirlayici {

    private final Clock saat;
    private final int limit;
    private final long pencereMs;
    private final ConcurrentHashMap<String, Deque<Long>> kayitlar = new ConcurrentHashMap<>();

    public KayanPencere(Clock saat, int limit, Duration pencere) {
        this.saat = saat;
        this.limit = limit;
        this.pencereMs = pencere.toMillis();
    }

    @Override
    public boolean izinVer(String anahtar) {
        long simdi = saat.millis();
        Deque<Long> zamanlar = kayitlar.computeIfAbsent(anahtar, k -> new ArrayDeque<>());
        synchronized (zamanlar) {
            while (!zamanlar.isEmpty() && zamanlar.peekFirst() <= simdi - pencereMs) {
                zamanlar.pollFirst();
            }
            if (zamanlar.size() >= limit) {
                return false;
            }
            zamanlar.addLast(simdi);
            return true;
        }
    }
}
