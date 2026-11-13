package com.semih.gunluk.gun37;

import java.time.Clock;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Jeton kovası (token bucket): kova en fazla {@code kapasite} jeton alıyor ve saniyede {@code dolum}
 * jeton doluyor; her istek bir jeton harcıyor. Kısa patlamalara (kapasite kadar) izin verip uzun
 * vadede ortalamayı sınırlıyor. Dolum bir zamanlayıcıyla değil, istek geldiğinde geçen süreden
 * hesaplanıyor ("tembel dolum"): binlerce anahtar için binlerce zamanlayıcı gerekmiyor.
 */
public class JetonKovasi implements HizSinirlayici {

    private record Kova(double jeton, long sonMs) {
    }

    private final Clock saat;
    private final int kapasite;
    private final double saniyedeDolum;
    private final ConcurrentHashMap<String, Kova> kovalar = new ConcurrentHashMap<>();

    public JetonKovasi(Clock saat, int kapasite, double saniyedeDolum) {
        this.saat = saat;
        this.kapasite = kapasite;
        this.saniyedeDolum = saniyedeDolum;
    }

    @Override
    public boolean izinVer(String anahtar) {
        boolean[] izin = new boolean[1];
        kovalar.compute(anahtar, (k, kova) -> {
            Kova dolu = doldur(kova);
            izin[0] = dolu.jeton() >= 1;
            return izin[0] ? new Kova(dolu.jeton() - 1, dolu.sonMs()) : dolu;
        });
        return izin[0];
    }

    /** Bir sonraki jetona kaç ms var: HTTP 429 yanıtındaki {@code Retry-After} için. */
    public long beklemeMs(String anahtar) {
        Kova kova = kovalar.computeIfPresent(anahtar, (k, v) -> doldur(v));
        if (kova == null || kova.jeton() >= 1) {
            return 0;
        }
        return (long) Math.ceil((1 - kova.jeton()) / saniyedeDolum * 1000);
    }

    private Kova doldur(Kova kova) {
        long simdi = saat.millis();
        if (kova == null) {
            return new Kova(kapasite, simdi);
        }
        double eklenen = (simdi - kova.sonMs()) / 1000.0 * saniyedeDolum;
        return new Kova(Math.min(kapasite, kova.jeton() + eklenen), simdi);
    }
}
