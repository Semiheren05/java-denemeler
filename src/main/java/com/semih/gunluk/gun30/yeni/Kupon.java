package com.semih.gunluk.gun30.yeni;

import java.math.BigDecimal;

/** Açık/kapalı: yeni bir kupon türü yeni bir sınıf demek, hesaplayıcıya dokunmak değil. */
@FunctionalInterface
public interface Kupon {

    /** @return bu tutardan düşülecek indirim (uygulanmıyorsa 0) */
    BigDecimal indirim(BigDecimal tutar);

    /** "En az {@code altSinir} TL'lik siparişte {@code tutar} TL indirim." */
    static Kupon sabitTutar(BigDecimal tutar, BigDecimal altSinir) {
        return siparisTutari -> siparisTutari.compareTo(altSinir) >= 0 ? tutar : BigDecimal.ZERO;
    }
}
