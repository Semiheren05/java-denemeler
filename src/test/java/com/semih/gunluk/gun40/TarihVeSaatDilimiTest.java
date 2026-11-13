package com.semih.gunluk.gun40;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.semih.gunluk.gun40.ToplantiPlanlayici.ISTANBUL;
import static com.semih.gunluk.gun40.ToplantiPlanlayici.LONDRA;
import static com.semih.gunluk.gun40.ToplantiPlanlayici.NEW_YORK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TarihVeSaatDilimiTest {

    /** Örnek tatiller: 29 Ekim ve 1 Ocak sabit; 30 Ekim "köprü" günü şirket kararıyla izin. */
    private final IsGunuTakvimi takvim = new IsGunuTakvimi(Set.of(
            LocalDate.of(2026, 10, 29), LocalDate.of(2026, 10, 30), LocalDate.of(2027, 1, 1)));

    @Test
    void isGunuMu_haftaSonuVeTatil() {
        assertEquals(DayOfWeek.THURSDAY, LocalDate.of(2026, 10, 29).getDayOfWeek());
        assertFalse(takvim.isGunuMu(LocalDate.of(2026, 10, 29)));   // Cumhuriyet Bayramı
        assertFalse(takvim.isGunuMu(LocalDate.of(2026, 10, 31)));   // cumartesi
        assertTrue(takvim.isGunuMu(LocalDate.of(2026, 10, 28)));
    }

    @ParameterizedTest
    @CsvSource({
            "2026-10-27, 1, 2026-10-28",
            "2026-10-28, 1, 2026-11-02",    // perşembe tatil, cuma köprü, hafta sonu
            "2026-11-13, 1, 2026-11-16",    // cuma + 1 = pazartesi
            "2026-12-31, 1, 2027-01-04",    // yılbaşı cuma, sonra hafta sonu
            "2026-11-09, 5, 2026-11-16"})
    void isGunuEkle(String tarih, int gun, String beklenen) {
        assertEquals(LocalDate.parse(beklenen), takvim.isGunuEkle(LocalDate.parse(tarih), gun));
    }

    @Test
    void aradakiIsGunu() {
        assertEquals(20, takvim.aradakiIsGunu(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 11, 1)));   // 22 hafta içi − 2 tatil
        assertEquals(0, takvim.aradakiIsGunu(LocalDate.of(2026, 10, 31), LocalDate.of(2026, 11, 2)));
    }

    @Test
    void temporalAdjuster_sonrakiIsGunu_ayinSonIsGunu() {
        assertEquals(LocalDate.of(2026, 11, 2), LocalDate.of(2026, 10, 29).with(takvim.buYaDaSonrakiIsGunu()));
        assertEquals(LocalDate.of(2026, 10, 28), LocalDate.of(2026, 10, 1).with(takvim.ayinSonIsGunu()));   // 31 cmt, 30-29 tatil
        assertEquals(LocalDate.of(2026, 11, 30), LocalDate.of(2026, 11, 13).with(takvim.ayinSonIsGunu()));
        assertEquals(LocalDate.of(2026, 11, 2), LocalDate.of(2026, 11, 13).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)));
    }

    @ParameterizedTest
    @CsvSource({
            "2026-11-13T14:59, 2, 2026-11-17",   // cuma kesimden önce: cuma işlendi, +2 iş günü salı
            "2026-11-13T15:00, 2, 2026-11-18",   // kesim saati: pazartesi işlenir, +2 çarşamba
            "2026-11-14T10:00, 1, 2026-11-17",   // cumartesi: pazartesi işlenir
            "2026-10-28T16:00, 1, 2026-11-03"})  // çarşamba akşam: perşembe-cuma tatil, pazartesi işlenir
    void tahminiTeslim_kesimSaati(String siparis, int kargo, String beklenen) {
        assertEquals(LocalDate.parse(beklenen), takvim.tahminiTeslim(LocalDateTime.parse(siparis), kargo));
    }

    @Test
    void toplanti_yazSaatiBitinceFarkDegisir() {
        Instant ekim = ToplantiPlanlayici.an(LocalDateTime.of(2026, 10, 15, 14, 0), ISTANBUL);
        Instant kasim = ToplantiPlanlayici.an(LocalDateTime.of(2026, 11, 13, 14, 0), ISTANBUL);

        // Türkiye yaz saati uygulamıyor (hep +03:00); New York 1 Kasım 2026'da −04:00'ten −05:00'e geçti
        assertEquals(LocalDateTime.of(2026, 10, 15, 7, 0), LocalDateTime.ofInstant(ekim, NEW_YORK));
        assertEquals(LocalDateTime.of(2026, 11, 13, 6, 0), LocalDateTime.ofInstant(kasim, NEW_YORK));
        assertEquals(Map.of(
                "America/New_York", LocalDateTime.of(2026, 11, 13, 6, 0),
                "Europe/Istanbul", LocalDateTime.of(2026, 11, 13, 14, 0),
                "Europe/London", LocalDateTime.of(2026, 11, 13, 11, 0)),
                ToplantiPlanlayici.herkesIcin(kasim, List.of(ISTANBUL, NEW_YORK, LONDRA)));
    }

    @Test
    void yazSaatiBoslugu_olmayanSaatIleriKayar() {
        // 8 Mart 2026, New York: saat 02:00'de 03:00'e atlıyor; 02:30 hiç yaşanmıyor
        ZonedDateTime z = ToplantiPlanlayici.yerlestir(LocalDateTime.of(2026, 3, 8, 2, 30), NEW_YORK);

        assertEquals(LocalDateTime.of(2026, 3, 8, 3, 30), z.toLocalDateTime());
        assertEquals(ZoneOffset.ofHours(-4), z.getOffset());
    }

    @Test
    void yazSaatiCakismasi_ayniSaatIkiKezYasanir() {
        // 1 Kasım 2026, New York: 02:00'de saat 01:00'e geri alınıyor; 01:30 iki kez var
        ZonedDateTime ilk = LocalDateTime.of(2026, 11, 1, 1, 30).atZone(NEW_YORK).withEarlierOffsetAtOverlap();
        ZonedDateTime ikinci = ilk.withLaterOffsetAtOverlap();

        assertEquals(ilk.toLocalDateTime(), ikinci.toLocalDateTime());
        assertEquals(Duration.ofHours(1), Duration.between(ilk, ikinci));
        assertEquals(ZoneOffset.ofHours(-4), ilk.getOffset());
        assertEquals(ZoneOffset.ofHours(-5), ikinci.getOffset());
    }

    @Test
    void periodTakvimGunu_durationSaat() {
        ZonedDateTime gecistenOnce = ZonedDateTime.of(2026, 3, 7, 12, 0, 0, 0, NEW_YORK);

        ZonedDateTime birGun = gecistenOnce.plus(Period.ofDays(1));        // yarın aynı saat
        ZonedDateTime yirmiDortSaat = gecistenOnce.plus(Duration.ofHours(24));

        assertEquals(LocalDateTime.of(2026, 3, 8, 12, 0), birGun.toLocalDateTime());
        assertEquals(LocalDateTime.of(2026, 3, 8, 13, 0), yirmiDortSaat.toLocalDateTime());
        assertEquals(Duration.ofHours(23), Duration.between(gecistenOnce, birGun));   // o gün 23 saat sürdü
    }

    @Test
    void aySonuVeAyniAn() {
        assertEquals(LocalDate.of(2026, 2, 28), LocalDate.of(2026, 1, 31).plusMonths(1));   // 31 Şubat yok: ayın son günü
        assertEquals(LocalDate.of(2028, 2, 29), LocalDate.of(2028, 1, 31).plusMonths(1));   // artık yıl

        ZonedDateTime istanbul = ZonedDateTime.of(2026, 11, 13, 14, 0, 0, 0, ISTANBUL);
        ZonedDateTime londra = istanbul.withZoneSameInstant(LONDRA);
        assertNotEquals(istanbul, londra);            // equals bölgeyi de karşılaştırıyor
        assertTrue(istanbul.isEqual(londra));         // isEqual sadece anı
        assertEquals(istanbul.toInstant(), londra.toInstant());
    }
}
