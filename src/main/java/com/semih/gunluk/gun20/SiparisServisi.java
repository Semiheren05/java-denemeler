package com.semih.gunluk.gun20;

import java.math.BigDecimal;

/**
 * Bağımlılıklarını constructor'da istiyor; {@code new BellekSiparisDeposu()} gibi bir satır yok.
 * Testte istenen depo verilebiliyor, üretimde kapsayıcı veriyor.
 */
public class SiparisServisi {

    private final SiparisDeposu depo;
    private final BildirimServisi bildirimler;

    public SiparisServisi(SiparisDeposu depo, BildirimServisi bildirimler) {
        this.depo = depo;
        this.bildirimler = bildirimler;
    }

    public Siparis siparisVer(String musteri, String tutar) {
        Siparis siparis = depo.kaydet(musteri, new BigDecimal(tutar));
        bildirimler.gonder(musteri + ", " + siparis.no() + " numaralı siparişin alındı");
        return siparis;
    }
}
