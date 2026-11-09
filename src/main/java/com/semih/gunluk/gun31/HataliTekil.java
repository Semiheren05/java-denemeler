package com.semih.gunluk.gun31;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <b>Bilerek hatalı.</b> Kontrol ve atama arasında başka bir thread araya girebilir
 * ("check-then-act" yarışı): iki thread de {@code null} görür, ikisi de nesne kurar.
 */
public final class HataliTekil {

    static final AtomicInteger KURULUM_SAYISI = new AtomicInteger();
    private static HataliTekil ornek;

    private HataliTekil() {
        KURULUM_SAYISI.incrementAndGet();
        yavasKurulum();
    }

    public static HataliTekil ornek() {
        if (ornek == null) {               // 1. thread A ve B burada ikisi de null görür
            ornek = new HataliTekil();      // 2. ikisi de yeni nesne kurar
        }
        return ornek;
    }

    static void sifirla() {
        ornek = null;
        KURULUM_SAYISI.set(0);
    }

    /** Bağlantı açmak, dosya okumak gibi süren bir kurulumu taklit ediyor; yarışı görünür kılar. */
    static void yavasKurulum() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
