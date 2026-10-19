package com.semih.gunluk.gun01;

import java.math.BigDecimal;

/**
 * Hesap geçmişindeki tek bir kayıt.
 *
 * @param sonBakiye işlemden hemen sonraki bakiye
 */
public record Islem(IslemTuru tur, BigDecimal tutar, BigDecimal sonBakiye) {
}
