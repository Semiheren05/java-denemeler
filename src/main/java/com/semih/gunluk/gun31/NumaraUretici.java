package com.semih.gunluk.gun31;

/** Bağımlılığı arayüzle tanımla; "tek örnek" kararı kullanan sınıfın değil, kuranın işi olsun. */
@FunctionalInterface
public interface NumaraUretici {

    long sonraki();
}
