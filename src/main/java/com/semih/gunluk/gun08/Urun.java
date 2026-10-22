package com.semih.gunluk.gun08;

import java.math.BigDecimal;

/** Stok listesindeki bir ürün. Geçersiz değerler nesne oluşmadan reddedilir. */
public record Urun(String kod, String ad, BigDecimal fiyat, int stok) {

    public Urun {
        if (kod == null || kod.isBlank()) {
            throw new IllegalArgumentException("Ürün kodu boş olamaz");
        }
        if (ad == null || ad.isBlank()) {
            throw new IllegalArgumentException("Ürün adı boş olamaz");
        }
        if (fiyat == null || fiyat.signum() <= 0) {
            throw new IllegalArgumentException("Fiyat sıfırdan büyük olmalı: " + fiyat);
        }
        if (stok < 0) {
            throw new IllegalArgumentException("Stok negatif olamaz: " + stok);
        }
    }
}
