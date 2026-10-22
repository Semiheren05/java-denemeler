package com.semih.gunluk.gun09;

import java.time.Duration;
import java.time.LocalTime;

/** Gün içinde randevu alınabilecek boş bir zaman dilimi. */
public record BosAralik(LocalTime baslangic, LocalTime bitis) {

    public Duration sure() {
        return Duration.between(baslangic, bitis);
    }
}
