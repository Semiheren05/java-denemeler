package com.semih.gunluk.gun30.yeni;

import java.math.BigDecimal;

/** Tek bir sayı yerine dökümü: müşteriye ve teste "neden bu tutar" görünüyor. */
public record Fiyatlandirma(BigDecimal kdvliToplam, BigDecimal musteriIndirimi, BigDecimal kuponIndirimi,
                            BigDecimal kargo, BigDecimal odenecek) {
}
