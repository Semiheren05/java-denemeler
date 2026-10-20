package com.semih.gunluk.gun03;

/**
 * Bir öğrenci ve dersten aldığı puan (0–100).
 * Geçersiz değerler nesne oluşturulurken reddedilir (compact constructor).
 */
public record Ogrenci(String ad, String bolum, int puan) {

    public Ogrenci {
        if (ad == null || ad.isBlank()) {
            throw new IllegalArgumentException("Öğrenci adı boş olamaz");
        }
        if (bolum == null || bolum.isBlank()) {
            throw new IllegalArgumentException("Bölüm boş olamaz");
        }
        if (puan < 0 || puan > 100) {
            throw new IllegalArgumentException("Puan 0 ile 100 arasında olmalı: " + puan);
        }
        ad = ad.trim();
        bolum = bolum.trim();
    }

    public HarfNotu harfNotu() {
        return HarfNotu.puandan(puan);
    }

    public boolean gectiMi() {
        return harfNotu().gecerMi();
    }
}
