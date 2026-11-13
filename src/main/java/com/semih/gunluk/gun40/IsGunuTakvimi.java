package com.semih.gunluk.gun40;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Set;

/**
 * Hafta sonu ve tatil bilen takvim. Tatiller dışarıdan veriliyor: dini bayramlar her yıl başka
 * günlere denk geliyor, kodun içine gömülemez (gerçek bir sistemde veritabanından ya da ayardan gelir).
 */
public class IsGunuTakvimi {

    /** Bu saatten sonra verilen sipariş ertesi iş günü işleme giriyor. */
    public static final LocalTime KESIM_SAATI = LocalTime.of(15, 0);

    private final Set<LocalDate> tatiller;

    public IsGunuTakvimi(Set<LocalDate> tatiller) {
        this.tatiller = Set.copyOf(tatiller);
    }

    public boolean isGunuMu(LocalDate tarih) {
        DayOfWeek gun = tarih.getDayOfWeek();
        return gun != DayOfWeek.SATURDAY && gun != DayOfWeek.SUNDAY && !tatiller.contains(tarih);
    }

    public LocalDate isGunuEkle(LocalDate tarih, int gun) {
        LocalDate sonuc = tarih;
        int eklenen = 0;
        while (eklenen < gun) {
            sonuc = sonuc.plusDays(1);
            if (isGunuMu(sonuc)) {
                eklenen++;
            }
        }
        return sonuc;
    }

    /** [baslangic, bitis) aralığındaki iş günü sayısı. */
    public long aradakiIsGunu(LocalDate baslangic, LocalDate bitis) {
        return baslangic.datesUntil(bitis).filter(this::isGunuMu).count();
    }

    /** {@code TemporalAdjuster}: {@code tarih.with(sonrakiIsGunu())} — tarih iş günüyse kendisi. */
    public TemporalAdjuster buYaDaSonrakiIsGunu() {
        return t -> {
            LocalDate tarih = LocalDate.from(t);
            while (!isGunuMu(tarih)) {
                tarih = tarih.plusDays(1);
            }
            return t.with(tarih);
        };
    }

    /** Maaş günü: ayın son iş günü. */
    public TemporalAdjuster ayinSonIsGunu() {
        return t -> {
            LocalDate tarih = LocalDate.from(t).with(TemporalAdjusters.lastDayOfMonth());
            while (!isGunuMu(tarih)) {
                tarih = tarih.minusDays(1);
            }
            return t.with(tarih);
        };
    }

    /** Kesim saatinden önceki sipariş bugün (iş günüyse) işleme giriyor, sonrası ertesi iş günü; sonra {@code kargoGunu} iş günü. */
    public LocalDate tahminiTeslim(LocalDateTime siparis, int kargoGunu) {
        LocalDate islemGunu = siparis.toLocalTime().isBefore(KESIM_SAATI)
                ? siparis.toLocalDate()
                : siparis.toLocalDate().plusDays(1);
        return isGunuEkle(islemGunu.with(buYaDaSonrakiIsGunu()), kargoGunu);
    }
}
