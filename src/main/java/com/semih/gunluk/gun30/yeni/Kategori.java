package com.semih.gunluk.gun30.yeni;

import java.math.BigDecimal;

/** {@code "ELEKTRONIK"} metni yerine tip: yazım hatası derlemede yakalanıyor, oran yanında duruyor. */
public enum Kategori {
    ELEKTRONIK("0.20"),
    GIDA("0.01"),
    DIGER("0.10");

    private final BigDecimal kdvOrani;

    Kategori(String kdvOrani) {
        this.kdvOrani = new BigDecimal(kdvOrani);
    }

    public BigDecimal kdvOrani() {
        return kdvOrani;
    }
}
