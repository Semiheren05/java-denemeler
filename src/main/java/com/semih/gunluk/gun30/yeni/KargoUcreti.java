package com.semih.gunluk.gun30.yeni;

import java.math.BigDecimal;
import java.util.Set;

/** Kargo kuralı tek yerde, sabitleri adlı. */
public final class KargoUcreti {

    static final BigDecimal UCRETSIZ_KARGO_SINIRI = new BigDecimal("750");
    static final BigDecimal BUYUKSEHIR_UCRETI = new BigDecimal("29.99");
    static final BigDecimal DIGER_UCRET = new BigDecimal("49.99");
    private static final Set<String> BUYUKSEHIRLER = Set.of("Istanbul", "Ankara", "Izmir");

    private KargoUcreti() {
    }

    public static BigDecimal hesapla(BigDecimal tutar, String sehir) {
        if (tutar.compareTo(UCRETSIZ_KARGO_SINIRI) >= 0) {
            return BigDecimal.ZERO;   // erken dönüş: iç içe if yok
        }
        return BUYUKSEHIRLER.contains(sehir) ? BUYUKSEHIR_UCRETI : DIGER_UCRET;
    }
}
