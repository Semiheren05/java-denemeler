package com.semih.gunluk.gun06;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Hazır kargo stratejileri. Her metot bir {@link KargoUcretStratejisi} döner.
 */
public final class KargoStratejileri {

    private static final int KURUS_HANESI = 2;
    private static final int UZAK_MESAFE_KM = 500;

    private KargoStratejileri() {
        // yardımcı sınıf, nesnesi oluşturulmaz
    }

    /** 30 TL taban + kilogram başına 5 TL. */
    public static KargoUcretStratejisi standart() {
        return gonderi -> tl(30).add(tl(5).multiply(kg(gonderi))).setScale(KURUS_HANESI, RoundingMode.HALF_EVEN);
    }

    /** 50 TL taban + kilogram başına 8 TL; 500 km'den uzaksa 20 TL ek. */
    public static KargoUcretStratejisi hizli() {
        return gonderi -> {
            BigDecimal ucret = tl(50).add(tl(8).multiply(kg(gonderi)));
            if (gonderi.mesafeKm() > UZAK_MESAFE_KM) {
                ucret = ucret.add(tl(20));
            }
            return ucret.setScale(KURUS_HANESI, RoundingMode.HALF_EVEN);
        };
    }

    public static KargoUcretStratejisi sabit(BigDecimal ucret) {
        Objects.requireNonNull(ucret, "Ücret boş olamaz");
        BigDecimal yuvarlanmis = ucret.setScale(KURUS_HANESI, RoundingMode.HALF_EVEN);
        return gonderi -> yuvarlanmis;
    }

    /**
     * Başka bir stratejiyi sarar: sepet eşiği geçerse kargo bedava, geçmezse asıl
     * strateji ne diyorsa o. Asıl strateji değiştirilmeden yeni davranış eklenir.
     */
    public static KargoUcretStratejisi ucretsizEsigi(BigDecimal esik, KargoUcretStratejisi asil) {
        Objects.requireNonNull(esik, "Eşik boş olamaz");
        Objects.requireNonNull(asil, "Asıl strateji boş olamaz");
        return gonderi -> gonderi.sepetTutari().compareTo(esik) >= 0
                ? BigDecimal.ZERO.setScale(KURUS_HANESI)
                : asil.hesapla(gonderi);
    }

    private static BigDecimal tl(int tutar) {
        return BigDecimal.valueOf(tutar);
    }

    private static BigDecimal kg(Gonderi gonderi) {
        return BigDecimal.valueOf(gonderi.agirlikKg());
    }
}
