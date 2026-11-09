package com.semih.gunluk.gun29;

import java.math.BigDecimal;

/** Ücretsiz çekim. */
public class VadesizHesap extends Hesap {

    public VadesizHesap(String no) {
        super(no);
    }

    @Override
    protected BigDecimal cekimUcreti(BigDecimal tutar) {
        return BigDecimal.ZERO;
    }
}
