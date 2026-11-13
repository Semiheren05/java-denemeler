package com.semih.gunluk.gun34;

public class YetersizBakiye extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public YetersizBakiye(long hesapId, long bakiye, long tutar) {
        super("Hesap " + hesapId + ": bakiye " + bakiye + ", istenen " + tutar);
    }
}
