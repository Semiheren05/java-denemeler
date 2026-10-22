package com.semih.gunluk.gun06;

import java.math.BigDecimal;

/**
 * Bir gönderinin kargo ücretini hesaplama yolu (strateji deseni).
 *
 * <p>Tek soyut metodu olduğu için {@code @FunctionalInterface}: yeni bir strateji
 * için sınıf yazmak şart değil, lambda da yeterli. Hesaplayıcı hangi stratejiyle
 * çalıştığını bilmez; yeni bir kargo firması eklemek mevcut kodu değiştirmez.
 */
@FunctionalInterface
public interface KargoUcretStratejisi {

    BigDecimal hesapla(Gonderi gonderi);
}
