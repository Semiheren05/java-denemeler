package com.semih.gunluk.gun16;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>Decorator</b>: aynı arayüzü uyguluyor ve içinde başka bir {@link HavaDurumuServisi}
 * taşıyor. Cevabı bir süre saklıyor; süre dolmadan aynı şehir sorulursa içteki servise hiç gitmiyor.
 * İçteki servis değişmeden ona yeni bir davranış eklenmiş oluyor.
 */
public class OnbellekliServis implements HavaDurumuServisi {

    private final HavaDurumuServisi ic;
    private final Duration omur;
    private final Clock saat;
    private final Map<String, Kayit> onbellek = new HashMap<>();

    public OnbellekliServis(HavaDurumuServisi ic, Duration omur, Clock saat) {
        this.ic = ic;
        this.omur = omur;
        this.saat = saat;
    }

    @Override
    public double sicaklik(String sehir) {
        Instant simdi = saat.instant();
        Kayit kayit = onbellek.get(sehir);
        if (kayit != null && simdi.isBefore(kayit.gecerlilikSonu())) {
            return kayit.deger();
        }
        double deger = ic.sicaklik(sehir);
        onbellek.put(sehir, new Kayit(deger, simdi.plus(omur)));
        return deger;
    }

    private record Kayit(double deger, Instant gecerlilikSonu) {
    }
}
