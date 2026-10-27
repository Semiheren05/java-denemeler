package com.semih.gunluk.gun16;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

/** Testte zamanı elle ileri sarmak için: {@code saat.ilerlet(Duration.ofMinutes(11))}. */
class AyarlanabilirSaat extends Clock {

    private Instant simdi;

    AyarlanabilirSaat(Instant baslangic) {
        this.simdi = baslangic;
    }

    void ilerlet(Duration sure) {
        simdi = simdi.plus(sure);
    }

    @Override
    public Instant instant() {
        return simdi;
    }

    @Override
    public ZoneId getZone() {
        return ZoneOffset.UTC;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        throw new UnsupportedOperationException();
    }
}
