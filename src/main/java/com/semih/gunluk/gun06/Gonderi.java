package com.semih.gunluk.gun06;

import java.math.BigDecimal;

/**
 * Kargoya verilecek bir paket.
 *
 * @param agirlikKg    paketin ağırlığı (kg)
 * @param mesafeKm     gönderilecek mesafe (km)
 * @param sepetTutari  siparişin tutarı; ücretsiz kargo kampanyaları buna bakar
 */
public record Gonderi(double agirlikKg, int mesafeKm, BigDecimal sepetTutari) {

    public Gonderi {
        if (!(agirlikKg > 0) || Double.isInfinite(agirlikKg)) {
            throw new IllegalArgumentException("Ağırlık pozitif olmalı: " + agirlikKg);
        }
        if (mesafeKm <= 0) {
            throw new IllegalArgumentException("Mesafe pozitif olmalı: " + mesafeKm);
        }
        if (sepetTutari == null || sepetTutari.signum() < 0) {
            throw new IllegalArgumentException("Sepet tutarı boş ya da negatif olamaz");
        }
    }
}
