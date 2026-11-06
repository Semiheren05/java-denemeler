package com.semih.gunluk.gun26;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Durumu sadece izinli geçişlerle değişebilen bir sipariş. {@code setDurum(...)} yok;
 * {@link #gec} her değişikliği kurala göre kontrol edip geçmişe yazıyor.
 */
public class Siparis {

    /** Teslimden sonra iade süresi. */
    static final Duration IADE_SURESI = Duration.ofDays(14);

    private final String no;
    private final Clock saat;
    private final List<DurumDegisimi> gecmis = new ArrayList<>();
    private SiparisDurumu durum = SiparisDurumu.OLUSTURULDU;

    public Siparis(String no, Clock saat) {
        this.no = no;
        this.saat = saat;
        gecmis.add(new DurumDegisimi(null, durum, saat.instant(), "Sipariş oluşturuldu"));
    }

    public void gec(SiparisDurumu hedef, String aciklama) {
        if (!durum.gecebilirMi(hedef)) {
            throw new GecersizGecisHatasi(no + ": " + durum.gorunenAd() + " → " + hedef.gorunenAd()
                    + " olmaz. Buradan gidilebilecekler: " + durum.sonrakiler());
        }
        // Geçişin izinli olması yetmeyebilir: bazı geçişlerin ek kuralı var
        if (hedef == SiparisDurumu.IADE_EDILDI) {
            Instant teslim = durumaGirisZamani(SiparisDurumu.TESLIM_EDILDI).orElseThrow();
            if (Duration.between(teslim, saat.instant()).compareTo(IADE_SURESI) > 0) {
                throw new GecersizGecisHatasi(no + ": iade süresi (" + IADE_SURESI.toDays() + " gün) doldu");
            }
        }
        gecmis.add(new DurumDegisimi(durum, hedef, saat.instant(), aciklama));
        durum = hedef;
    }

    public void iptalEt(String neden) {
        if (!SiparisDurumu.IPTAL_EDILEBILIR.contains(durum)) {
            throw new GecersizGecisHatasi(no + ": " + durum.gorunenAd() + " durumundaki sipariş iptal edilemez");
        }
        gec(SiparisDurumu.IPTAL_EDILDI, neden);
    }

    public Optional<Instant> durumaGirisZamani(SiparisDurumu aranan) {
        return gecmis.stream().filter(d -> d.yeni() == aranan).map(DurumDegisimi::zaman).findFirst();
    }

    public SiparisDurumu durum() {
        return durum;
    }

    public String no() {
        return no;
    }

    public List<DurumDegisimi> gecmis() {
        return List.copyOf(gecmis);
    }
}
