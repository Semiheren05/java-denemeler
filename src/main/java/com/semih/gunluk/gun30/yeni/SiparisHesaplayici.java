package com.semih.gunluk.gun30.yeni;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Aynı iş kuralları, temizlenmiş hâli. {@link #hesapla} bir içindekiler sayfası gibi okunuyor;
 * her adım adını taşıyan küçük bir metotta. Kuponlar dışarıdan veriliyor (bağımlılık
 * enjeksiyonu): yeni kupon eklemek için bu sınıf açılmıyor.
 */
public class SiparisHesaplayici {

    private final Map<String, Kupon> kuponlar;

    public SiparisHesaplayici(Map<String, Kupon> kuponlar) {
        this.kuponlar = Map.copyOf(kuponlar);
    }

    public Fiyatlandirma hesapla(Siparis siparis) {
        BigDecimal kdvliToplam = kdvliToplam(siparis);
        BigDecimal musteriIndirimi = kdvliToplam.multiply(siparis.musteriTipi().indirimOrani());
        BigDecimal indirimli = kdvliToplam.subtract(musteriIndirimi);
        BigDecimal kuponIndirimi = kuponIndirimi(siparis, indirimli);
        BigDecimal kuponlu = indirimli.subtract(kuponIndirimi);
        BigDecimal kargo = KargoUcreti.hesapla(kuponlu, siparis.sehir());
        return new Fiyatlandirma(
                kurus(kdvliToplam), kurus(musteriIndirimi), kurus(kuponIndirimi), kargo, kurus(kuponlu.add(kargo)));
    }

    private static BigDecimal kdvliToplam(Siparis siparis) {
        return siparis.kalemler().stream().map(SiparisKalemi::kdvliTutar).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Bilinmeyen kupon kodu eskisi gibi sessizce yok sayılıyor (davranış korundu). */
    private BigDecimal kuponIndirimi(Siparis siparis, BigDecimal tutar) {
        return siparis.kuponKodu()
                .map(kuponlar::get)
                .map(kupon -> kupon.indirim(tutar))
                .orElse(BigDecimal.ZERO);
    }

    private static BigDecimal kurus(BigDecimal tutar) {
        return tutar.setScale(2, RoundingMode.HALF_UP);
    }
}
