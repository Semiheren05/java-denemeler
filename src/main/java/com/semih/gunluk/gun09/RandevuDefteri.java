package com.semih.gunluk.gun09;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Bir kuaförün randevu defteri. Mesai 09:00–18:00, pazar kapalı.
 *
 * <p>"Şu an" bilgisi {@code LocalDateTime.now()} ile değil, dışarıdan verilen bir
 * {@link Clock} ile alınıyor. Testte sabit bir saat verip "geçmiş zamana randevu
 * alınamaz" gibi kuralları her gün aynı sonucu verecek şekilde deneyebiliyorum.
 */
public class RandevuDefteri {

    static final LocalTime ACILIS = LocalTime.of(9, 0);
    static final LocalTime KAPANIS = LocalTime.of(18, 0);

    private final Clock saat;
    private final List<Randevu> randevular = new ArrayList<>();

    public RandevuDefteri(Clock saat) {
        this.saat = Objects.requireNonNull(saat, "Saat boş olamaz");
    }

    /**
     * @throws IllegalArgumentException geçmiş zaman, mesai dışı ya da pazar günü
     * @throws CakismaException         başka bir randevuyla çakışıyorsa; defter değişmez
     */
    public void ekle(Randevu yeni) {
        Objects.requireNonNull(yeni, "Randevu boş olamaz");
        if (yeni.baslangic().isBefore(simdi())) {
            throw new IllegalArgumentException("Geçmiş zamana randevu alınamaz: " + yeni.baslangic());
        }
        if (yeni.baslangic().getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Pazar günü kapalıyız");
        }
        if (!mesaiIcindeMi(yeni)) {
            throw new IllegalArgumentException("Randevu " + ACILIS + "–" + KAPANIS + " arasında olmalı");
        }
        for (Randevu mevcut : randevular) {
            if (mevcut.cakisirMi(yeni)) {
                throw new CakismaException(mevcut);
            }
        }
        randevular.add(yeni);
    }

    public boolean iptalEt(String musteri, LocalDateTime baslangic) {
        return randevular.removeIf(r -> r.musteri().equals(musteri) && r.baslangic().equals(baslangic));
    }

    public List<Randevu> gununRandevulari(LocalDate gun) {
        return randevular.stream()
                .filter(r -> r.baslangic().toLocalDate().equals(gun))
                .sorted(Comparator.comparing(Randevu::baslangic))
                .toList();
    }

    /** Mesai içindeki boşluklar; en az {@code enAzSure} uzunluğunda olanlar. */
    public List<BosAralik> bosAraliklar(LocalDate gun, Duration enAzSure) {
        List<BosAralik> bosluklar = new ArrayList<>();
        LocalTime imlec = ACILIS;
        for (Randevu randevu : gununRandevulari(gun)) {
            LocalTime randevuBasi = randevu.baslangic().toLocalTime();
            if (imlec.isBefore(randevuBasi)) {
                bosluklar.add(new BosAralik(imlec, randevuBasi));
            }
            imlec = randevu.bitis().toLocalTime();
        }
        if (imlec.isBefore(KAPANIS)) {
            bosluklar.add(new BosAralik(imlec, KAPANIS));
        }
        return bosluklar.stream()
                .filter(aralik -> aralik.sure().compareTo(enAzSure) >= 0)
                .toList();
    }

    /** Şu andan sonra başlayan ilk randevu. */
    public Optional<Randevu> siradakiRandevu() {
        LocalDateTime simdi = simdi();
        return randevular.stream()
                .filter(r -> !r.baslangic().isBefore(simdi))
                .min(Comparator.comparing(Randevu::baslangic));
    }

    /** Randevuya ne kadar kaldı; geçmişse negatif süre döner. */
    public Duration kalanSure(Randevu randevu) {
        return Duration.between(simdi(), randevu.baslangic());
    }

    private LocalDateTime simdi() {
        return LocalDateTime.now(saat);
    }

    private static boolean mesaiIcindeMi(Randevu randevu) {
        LocalTime bas = randevu.baslangic().toLocalTime();
        LocalDateTime bitis = randevu.bitis();
        // Bitiş ertesi güne taşıyorsa toLocalTime() yanıltır, önce tarihi kontrol et
        return !bas.isBefore(ACILIS)
                && bitis.toLocalDate().equals(randevu.baslangic().toLocalDate())
                && !bitis.toLocalTime().isAfter(KAPANIS);
    }
}
