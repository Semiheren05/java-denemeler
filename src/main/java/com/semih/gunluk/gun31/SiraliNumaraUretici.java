package com.semih.gunluk.gun31;

import java.util.concurrent.atomic.AtomicLong;

/** Spring'de {@code @Component} olurdu: konteyner bunu bir kez kurar (varsayılan kapsam singleton). */
public class SiraliNumaraUretici implements NumaraUretici {

    private final AtomicLong sayac;

    public SiraliNumaraUretici(long baslangic) {
        this.sayac = new AtomicLong(baslangic);
    }

    @Override
    public long sonraki() {
        return sayac.incrementAndGet();
    }
}
