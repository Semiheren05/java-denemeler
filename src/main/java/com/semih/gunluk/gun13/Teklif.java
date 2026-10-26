package com.semih.gunluk.gun13;

import java.math.BigDecimal;

public record Teklif(String tedarikci, BigDecimal fiyat) {

    public Teklif {
        if (fiyat == null || fiyat.signum() <= 0) {
            throw new IllegalArgumentException(tedarikci + " geçersiz fiyat verdi: " + fiyat);
        }
    }
}
