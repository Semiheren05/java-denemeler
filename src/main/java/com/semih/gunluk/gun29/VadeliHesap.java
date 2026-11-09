package com.semih.gunluk.gun29;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Vadesi bozulunca çekilen tutarın %1'i ceza. Hem bir {@link Hesap} hem {@link FaizGetiren}. */
public class VadeliHesap extends Hesap implements FaizGetiren {

    private static final BigDecimal CEZA_ORANI = new BigDecimal("0.01");

    private final BigDecimal yillikFaizOrani;

    public VadeliHesap(String no, BigDecimal yillikFaizOrani) {
        super(no);
        this.yillikFaizOrani = yillikFaizOrani;
    }

    @Override
    protected BigDecimal cekimUcreti(BigDecimal tutar) {
        return tutar.multiply(CEZA_ORANI).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal yillikFaizOrani() {
        return yillikFaizOrani;
    }
}
