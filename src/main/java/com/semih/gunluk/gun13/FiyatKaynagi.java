package com.semih.gunluk.gun13;

import java.math.BigDecimal;

/** Bir tedarikçinin fiyat servisi; gerçekte yavaş bir HTTP çağrısı olabilir. */
@FunctionalInterface
public interface FiyatKaynagi {

    BigDecimal fiyat(String urun);
}
