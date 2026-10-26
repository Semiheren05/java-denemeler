package com.semih.gunluk.gun10;

import java.math.BigDecimal;

/** Faturadaki bir satır: ürün, birim fiyat, adet ve KDV oranı. */
public record FaturaKalemi(String urun, BigDecimal birimFiyat, int adet, KdvOrani kdv) {

    public FaturaKalemi {
        if (urun == null || urun.isBlank()) {
            throw new IllegalArgumentException("Ürün adı boş olamaz");
        }
        if (birimFiyat == null || birimFiyat.signum() <= 0) {
            throw new IllegalArgumentException("Birim fiyat pozitif olmalı: " + birimFiyat);
        }
        if (adet <= 0) {
            throw new IllegalArgumentException("Adet pozitif olmalı: " + adet);
        }
        if (kdv == null) {
            throw new IllegalArgumentException("KDV oranı boş olamaz");
        }
        urun = urun.trim();
    }

    /** KDV ve indirim hariç satır tutarı. */
    public BigDecimal tutar() {
        return birimFiyat.multiply(BigDecimal.valueOf(adet));
    }
}
