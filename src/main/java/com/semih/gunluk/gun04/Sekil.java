package com.semih.gunluk.gun04;

/**
 * Bir geometrik şekil.
 *
 * <p>{@code sealed} olduğu için yalnızca {@code permits} listesindeki tipler bu arayüzü
 * uygulayabilir. Derleyici tüm alt tipleri bildiği için {@code switch} ifadelerinde
 * {@code default} yazmaya gerek kalmaz; yeni bir şekil eklenip bir {@code switch}'te
 * unutulursa kod derlenmez.
 */
public sealed interface Sekil permits Daire, Dikdortgen, Kare, Ucgen {

    /** Kenar ve yarıçap gibi ölçülerin pozitif ve sonlu olduğunu kontrol eder. */
    static void pozitifOlmali(String ad, double deger) {
        if (!(deger > 0) || Double.isInfinite(deger)) {
            throw new IllegalArgumentException(ad + " pozitif bir sayı olmalı: " + deger);
        }
    }
}
