package com.semih.gunluk.gun26;

import java.util.EnumSet;
import java.util.Set;

/**
 * Bir siparişin geçebileceği durumlar ve aralarındaki izinli geçişler: bir <b>durum makinesi</b>.
 * Hangi durumdan hangisine gidilebileceği dağınık {@code if}'lerde değil, burada tek yerde.
 *
 * <p>Geçişler {@code switch} ile bir metotta: enum sabitleri tanımlanırken henüz sonraki
 * sabitler oluşmadığı için {@code KARGODA(EnumSet.of(TESLIM_EDILDI))} gibi bir constructor
 * parametresi derlenmez (ileri referans).
 */
public enum SiparisDurumu {

    OLUSTURULDU("Sipariş alındı"),
    ODENDI("Ödeme onaylandı"),
    HAZIRLANIYOR("Hazırlanıyor"),
    KARGODA("Kargoya verildi"),
    TESLIM_EDILDI("Teslim edildi"),
    IADE_EDILDI("İade edildi"),
    IPTAL_EDILDI("İptal edildi") {
        // Sabite özel gövde: sadece bu sabitte farklı davranan metot
        @Override
        public String musteriMesaji(String siparisNo) {
            return siparisNo + " numaralı siparişin iptal edildi; ödeme yaptıysan iaden 3 iş günü içinde hesabında.";
        }
    };

    /** İptal ancak kargoya verilmeden önce mümkün. {@code EnumSet.range}: sıradaki ilk üç durum. */
    public static final Set<SiparisDurumu> IPTAL_EDILEBILIR = EnumSet.range(OLUSTURULDU, HAZIRLANIYOR);

    private final String gorunenAd;

    SiparisDurumu(String gorunenAd) {
        this.gorunenAd = gorunenAd;
    }

    /** Bu durumdan gidilebilecek durumlar. {@code EnumSet}: içeride tek bir {@code long}'un bitleri. */
    public Set<SiparisDurumu> sonrakiler() {
        return switch (this) {
            case OLUSTURULDU -> EnumSet.of(ODENDI, IPTAL_EDILDI);
            case ODENDI -> EnumSet.of(HAZIRLANIYOR, IPTAL_EDILDI);
            case HAZIRLANIYOR -> EnumSet.of(KARGODA, IPTAL_EDILDI);
            case KARGODA -> EnumSet.of(TESLIM_EDILDI);
            case TESLIM_EDILDI -> EnumSet.of(IADE_EDILDI);
            case IADE_EDILDI, IPTAL_EDILDI -> EnumSet.noneOf(SiparisDurumu.class);
        };
    }

    /** Son durum: buradan hiçbir yere gidilmez. */
    public boolean sonMu() {
        return sonrakiler().isEmpty();
    }

    public boolean gecebilirMi(SiparisDurumu hedef) {
        return sonrakiler().contains(hedef);
    }

    public String gorunenAd() {
        return gorunenAd;
    }

    public String musteriMesaji(String siparisNo) {
        return siparisNo + " numaralı siparişin durumu: " + gorunenAd;
    }
}
