package com.semih.gunluk.gun38;

import java.math.BigDecimal;

/** Otel fiyatı kendi para biriminde; TL'ye çevirmek için önce hangi birim olduğunu bilmek gerekiyor. */
public record Otel(String ad, BigDecimal geceFiyati, String paraBirimi) {
}
