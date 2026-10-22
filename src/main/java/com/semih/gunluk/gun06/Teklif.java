package com.semih.gunluk.gun06;

import java.math.BigDecimal;

/** Bir kargo seçeneğinin belirli bir gönderi için ücreti. */
public record Teklif(String firma, BigDecimal ucret) {
}
