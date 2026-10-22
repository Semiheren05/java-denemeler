package com.semih.gunluk.gun09;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Bir müşterinin randevusu: ne zaman başlıyor ve ne kadar sürüyor.
 *
 * <p>Bitiş saati ayrıca saklanmıyor, başlangıç + süreden hesaplanıyor; böylece ikisi
 * hiçbir zaman birbiriyle çelişemez.
 */
public record Randevu(String musteri, LocalDateTime baslangic, Duration sure) {

    public Randevu {
        if (musteri == null || musteri.isBlank()) {
            throw new IllegalArgumentException("Müşteri adı boş olamaz");
        }
        if (baslangic == null) {
            throw new IllegalArgumentException("Başlangıç zamanı boş olamaz");
        }
        if (sure == null || sure.isNegative() || sure.isZero()) {
            throw new IllegalArgumentException("Süre pozitif olmalı: " + sure);
        }
        musteri = musteri.trim();
    }

    public LocalDateTime bitis() {
        return baslangic.plus(sure);
    }

    /**
     * İki zaman aralığı, biri diğeri bitmeden başlıyorsa çakışır. Uç uca eklenen
     * randevular (biri 10:00'da biterken öteki 10:00'da başlıyor) çakışmaz.
     */
    public boolean cakisirMi(Randevu diger) {
        return baslangic.isBefore(diger.bitis()) && diger.baslangic.isBefore(bitis());
    }
}
