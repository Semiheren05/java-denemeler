package com.semih.gunluk.gun26;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/** Yönetici paneli için özetler. */
public final class SiparisPaneli {

    private SiparisPaneli() {
    }

    /**
     * Her durumda kaç sipariş var; hiç olmayan durumlar da 0 ile. {@link EnumMap} içeride bir
     * dizi: anahtar enum'un sırası ({@code ordinal}), {@code HashMap}'ten hızlı ve tanım sırasıyla dolaşılıyor.
     */
    public static Map<SiparisDurumu, Integer> durumSayilari(Collection<Siparis> siparisler) {
        Map<SiparisDurumu, Integer> sayilar = new EnumMap<>(SiparisDurumu.class);
        for (SiparisDurumu durum : SiparisDurumu.values()) {
            sayilar.put(durum, 0);
        }
        siparisler.forEach(s -> sayilar.merge(s.durum(), 1, Integer::sum));
        return sayilar;
    }

    /** Hâlâ ilgilenilmesi gerekenler: son durumda olmayanlar. {@code EnumSet.complementOf} ile "geri kalanlar". */
    public static Set<SiparisDurumu> acikDurumlar() {
        return EnumSet.complementOf(EnumSet.of(SiparisDurumu.IADE_EDILDI, SiparisDurumu.IPTAL_EDILDI,
                SiparisDurumu.TESLIM_EDILDI));
    }
}
