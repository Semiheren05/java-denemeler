package com.semih.gunluk.gun31;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tembel (lazy) singleton, "holder" deyimiyle. İç sınıf {@code Tutucu}, ancak {@link #ornek()}
 * ilk kez çağrılınca yüklenir; JVM sınıf yüklemeyi zaten thread-safe yaptığı için
 * {@code synchronized} ya da {@code volatile} gerekmez.
 */
public final class TembelTekil {

    static final AtomicInteger KURULUM_SAYISI = new AtomicInteger();

    private TembelTekil() {
        KURULUM_SAYISI.incrementAndGet();
    }

    /** Dış sınıfın başka bir üyesine dokunmak örneği kurmaz. */
    public static String surum() {
        return "1.0";
    }

    public static TembelTekil ornek() {
        return Tutucu.ORNEK;
    }

    private static final class Tutucu {
        private static final TembelTekil ORNEK = new TembelTekil();
    }
}
