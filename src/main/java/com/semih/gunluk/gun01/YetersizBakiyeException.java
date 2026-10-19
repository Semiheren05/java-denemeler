package com.semih.gunluk.gun01;

import java.math.BigDecimal;

/** Çekilmek istenen tutar bakiyeden fazla olduğunda fırlatılır. */
public class YetersizBakiyeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final BigDecimal bakiye;
    private final BigDecimal istenenTutar;

    public YetersizBakiyeException(String hesapNo, BigDecimal bakiye, BigDecimal istenenTutar) {
        super("Yetersiz bakiye (hesap " + hesapNo + "): bakiye " + bakiye + ", istenen " + istenenTutar);
        this.bakiye = bakiye;
        this.istenenTutar = istenenTutar;
    }

    public BigDecimal getBakiye() {
        return bakiye;
    }

    public BigDecimal getIstenenTutar() {
        return istenenTutar;
    }
}
