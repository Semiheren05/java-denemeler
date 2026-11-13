package com.semih.gunluk.gun35;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/** Bir ödünç kaydı. İade edilince yeni bir kayıt üretiliyor (değiştirilemez). */
public record Odunc(String isbn, String uyeNo, LocalDate alis, LocalDate sonTeslim, boolean uzatildi, LocalDate iade) {

    public boolean acik() {
        return iade == null;
    }

    public Optional<LocalDate> iadeTarihi() {
        return Optional.ofNullable(iade);
    }

    /** Bugüne (ya da iade gününe) göre kaç gün gecikti; gecikmediyse 0. */
    public long gecikmeGunu(LocalDate bugun) {
        LocalDate bitis = acik() ? bugun : iade;
        return Math.max(0, ChronoUnit.DAYS.between(sonTeslim, bitis));
    }

    Odunc iadeEdildi(LocalDate tarih) {
        return new Odunc(isbn, uyeNo, alis, sonTeslim, uzatildi, tarih);
    }

    Odunc uzatildi(int gun) {
        return new Odunc(isbn, uyeNo, alis, sonTeslim.plusDays(gun), true, iade);
    }
}
