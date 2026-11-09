package com.semih.gunluk.gun29;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <b>Interface</b>: bir yetenek ("faiz getirebilir"). Durum tutmuyor ama {@code default} metotla
 * ortak bir hesap sunabiliyor. Bir sınıf tek bir sınıftan türeyebilir, istediği kadar interface
 * uygulayabilir.
 */
public interface FaizGetiren {

    /** Yıllık faiz oranı, ör. 0.40 = %40. */
    BigDecimal yillikFaizOrani();

    BigDecimal bakiye();

    default BigDecimal aylikFaiz() {
        return bakiye().multiply(yillikFaizOrani()).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }
}
