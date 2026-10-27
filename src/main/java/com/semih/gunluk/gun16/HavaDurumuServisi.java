package com.semih.gunluk.gun16;

/** Uygulamanın hava durumunu sorduğu tek arayüz. Arkasında ne olduğunu bilmiyor. */
@FunctionalInterface
public interface HavaDurumuServisi {

    /** @return santigrat derece, bir ondalıkla */
    double sicaklik(String sehir);
}
