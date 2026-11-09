package com.semih.gunluk.gun31;

/**
 * Singleton'ın asıl sorunu thread değil, <b>küresel durum</b>: her yerden erişiliyor, testler
 * arasında taşınıyor, sahte (mock) ile değiştirilemiyor.
 */
public final class GlobalSayac {

    private static final GlobalSayac ORNEK = new GlobalSayac();
    private long deger;

    private GlobalSayac() {
    }

    public static GlobalSayac ornek() {
        return ORNEK;
    }

    public synchronized long sonraki() {
        return ++deger;
    }
}
