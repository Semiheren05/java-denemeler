package com.semih.gunluk.gun03;

/**
 * Türk üniversitelerinde yaygın kullanılan harf notu sistemi.
 * Her not, o harfi almak için gereken en düşük puanı taşır.
 * Sıralama yüksekten düşüğe; {@link #puandan(int)} bu sırayla arar.
 */
public enum HarfNotu {
    AA(90),
    BA(85),
    BB(80),
    CB(75),
    CC(70),
    DC(65),
    DD(60),
    FD(50),
    FF(0);

    private final int altSinir;

    HarfNotu(int altSinir) {
        this.altSinir = altSinir;
    }

    public int altSinir() {
        return altSinir;
    }

    /** DD ve üstü geçer, FD ve FF kalır. */
    public boolean gecerMi() {
        return altSinir >= DD.altSinir;
    }

    public static HarfNotu puandan(int puan) {
        if (puan < 0 || puan > 100) {
            throw new IllegalArgumentException("Puan 0 ile 100 arasında olmalı: " + puan);
        }
        for (HarfNotu not : values()) {
            if (puan >= not.altSinir) {
                return not;
            }
        }
        throw new IllegalStateException("Ulaşılamaz: FF her puanı kapsar");
    }
}
