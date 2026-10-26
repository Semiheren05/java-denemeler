package com.semih.gunluk.gun10;

import java.math.BigDecimal;

/** Türkiye'deki KDV oranları. */
public enum KdvOrani {
    YUZDE_1("0.01"),
    YUZDE_10("0.10"),
    YUZDE_20("0.20");

    private final BigDecimal oran;

    KdvOrani(String oran) {
        this.oran = new BigDecimal(oran);
    }

    public BigDecimal oran() {
        return oran;
    }
}
