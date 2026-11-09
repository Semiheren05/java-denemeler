package com.semih.gunluk.gun31;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Çift kontrollü kilitleme (double-checked locking). Kilitsiz ilk kontrol hızlı yol;
 * kilit içindeki ikinci kontrol yarışı kapatıyor.
 *
 * <p>{@code volatile} şart: yoksa JVM, nesne tamamen kurulmadan referansı alana yazabilir ve
 * başka bir thread yarı kurulmuş nesne görebilir. Holder deyimi aynı işi daha sade yaptığı
 * için genelde o tercih edilir; bu sürüm mülakatta sık sorulduğu için burada.
 */
public final class CiftKontrolluTekil {

    static final AtomicInteger KURULUM_SAYISI = new AtomicInteger();
    private static volatile CiftKontrolluTekil ornek;

    private CiftKontrolluTekil() {
        KURULUM_SAYISI.incrementAndGet();
        HataliTekil.yavasKurulum();
    }

    public static CiftKontrolluTekil ornek() {
        CiftKontrolluTekil yerel = ornek;   // volatile alanı bir kez oku
        if (yerel == null) {
            synchronized (CiftKontrolluTekil.class) {
                yerel = ornek;
                if (yerel == null) {
                    ornek = yerel = new CiftKontrolluTekil();
                }
            }
        }
        return yerel;
    }

    static void sifirla() {
        ornek = null;
        KURULUM_SAYISI.set(0);
    }
}
