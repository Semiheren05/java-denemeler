package com.semih.gunluk.gun37;

import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * En basiti: zamanı sabit dilimlere böl (ör. her dakika), dilim başına sayaç tut. Ucuz (anahtar
 * başına iki sayı) ama sınırda zayıf: bir dakikanın son anında ve sonrakinin ilk anında limit
 * kadar istek gelirse, bir saniye içinde limitin iki katı geçiyor.
 */
public class SabitPencere implements HizSinirlayici {

    private record Durum(long pencere, int sayi) {
    }

    private final Clock saat;
    private final int limit;
    private final long pencereMs;
    private final ConcurrentHashMap<String, Durum> durumlar = new ConcurrentHashMap<>();

    public SabitPencere(Clock saat, int limit, Duration pencere) {
        this.saat = saat;
        this.limit = limit;
        this.pencereMs = pencere.toMillis();
    }

    @Override
    public boolean izinVer(String anahtar) {
        long pencere = saat.millis() / pencereMs;
        boolean[] izin = new boolean[1];
        // compute anahtar başına atomik: oku-karar ver-yaz arasına başka thread giremiyor
        durumlar.compute(anahtar, (k, d) -> {
            Durum simdiki = (d == null || d.pencere() != pencere) ? new Durum(pencere, 0) : d;
            izin[0] = simdiki.sayi() < limit;
            return izin[0] ? new Durum(pencere, simdiki.sayi() + 1) : simdiki;
        });
        return izin[0];
    }
}
