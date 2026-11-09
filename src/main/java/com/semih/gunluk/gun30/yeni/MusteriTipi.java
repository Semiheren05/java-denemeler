package com.semih.gunluk.gun30.yeni;

import java.math.BigDecimal;

public enum MusteriTipi {
    ALTIN("0.10"),
    GUMUS("0.05"),
    STANDART("0");

    private final BigDecimal indirimOrani;

    MusteriTipi(String indirimOrani) {
        this.indirimOrani = new BigDecimal(indirimOrani);
    }

    public BigDecimal indirimOrani() {
        return indirimOrani;
    }
}
