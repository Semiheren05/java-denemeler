package com.semih.gunluk.gun09;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandevuDefteriTest {

    // 22 Ekim 2026 Perşembe, İstanbul saatiyle 10:00 (UTC+3)
    private static final Clock SAAT = Clock.fixed(Instant.parse("2026-10-22T07:00:00Z"), ZoneId.of("Europe/Istanbul"));
    private static final LocalDate BUGUN = LocalDate.of(2026, 10, 22);
    private static final LocalDate YARIN = BUGUN.plusDays(1);

    private RandevuDefteri defter;

    @BeforeEach
    void setUp() {
        defter = new RandevuDefteri(SAAT);
    }

    @Test
    void randevuBitisiBaslangicArtiSure() {
        Randevu randevu = randevu("Semih", YARIN, "14:30", 45);

        assertEquals(LocalDateTime.of(2026, 10, 23, 15, 15), randevu.bitis());
    }

    @Test
    @DisplayName("Uç uca randevular çakışmaz, iç içe olanlar çakışır")
    void cakismaKurali() {
        Randevu ilk = randevu("Semih", YARIN, "10:00", 60);

        assertFalse(ilk.cakisirMi(randevu("Ayşe", YARIN, "11:00", 30)));
        assertTrue(ilk.cakisirMi(randevu("Ayşe", YARIN, "10:30", 60)));
        assertTrue(ilk.cakisirMi(randevu("Ayşe", YARIN, "09:30", 240)));
    }

    @Test
    void gununRandevulariSaateGoreSiralanir() {
        Randevu oglen = randevu("Ali", YARIN, "12:00", 30);
        Randevu sabah = randevu("Ayşe", YARIN, "09:00", 30);
        defter.ekle(oglen);
        defter.ekle(sabah);
        defter.ekle(randevu("Deniz", YARIN.plusDays(1), "09:00", 30));

        assertEquals(List.of(sabah, oglen), defter.gununRandevulari(YARIN));
    }

    @Test
    @DisplayName("Çakışan randevu reddedilir ve defter değişmez")
    void cakisanRandevuReddedilir() {
        Randevu mevcut = randevu("Semih", YARIN, "14:00", 60);
        defter.ekle(mevcut);

        CakismaException hata = assertThrows(CakismaException.class,
                () -> defter.ekle(randevu("Ayşe", YARIN, "14:30", 30)));

        assertEquals(mevcut, hata.getMevcut());
        assertEquals(List.of(mevcut), defter.gununRandevulari(YARIN));
    }

    @Test
    void gecmisZamanaRandevuAlinamaz() {
        assertThrows(IllegalArgumentException.class, () -> defter.ekle(randevu("Semih", BUGUN, "09:30", 30)));
    }

    @Test
    void bugunIleriBirSaateRandevuAlinabilir() {
        defter.ekle(randevu("Semih", BUGUN, "10:00", 30));

        assertEquals(1, defter.gununRandevulari(BUGUN).size());
    }

    @Test
    void pazarGunuKapali() {
        LocalDate pazar = LocalDate.of(2026, 10, 25);

        assertThrows(IllegalArgumentException.class, () -> defter.ekle(randevu("Semih", pazar, "10:00", 30)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"08:30", "17:45", "18:00", "23:30"})
    @DisplayName("Mesai dışına taşan randevular reddedilir (gece yarısını geçen dahil)")
    void mesaiDisiReddedilir(String saat) {
        assertThrows(IllegalArgumentException.class, () -> defter.ekle(randevu("Semih", YARIN, saat, 60)));
    }

    @Test
    void kapanisaTamDenkGelenRandevuKabulEdilir() {
        defter.ekle(randevu("Semih", YARIN, "17:00", 60));

        assertEquals(1, defter.gununRandevulari(YARIN).size());
    }

    @Test
    void bosAraliklarRandevularinArasindanCikar() {
        defter.ekle(randevu("Ayşe", YARIN, "09:00", 60));
        defter.ekle(randevu("Ali", YARIN, "12:00", 30));
        defter.ekle(randevu("Deniz", YARIN, "12:45", 15));

        List<BosAralik> bosluklar = defter.bosAraliklar(YARIN, Duration.ofMinutes(30));

        assertEquals(List.of(
                new BosAralik(saat("10:00"), saat("12:00")),
                new BosAralik(saat("13:00"), saat("18:00"))
        ), bosluklar);
    }

    @Test
    void randevusuzGunTamamenBostur() {
        assertEquals(List.of(new BosAralik(saat("09:00"), saat("18:00"))),
                defter.bosAraliklar(YARIN, Duration.ofMinutes(15)));
    }

    @Test
    void siradakiRandevuVeKalanSure() {
        Randevu yarinki = randevu("Ayşe", YARIN, "09:00", 30);
        Randevu bugunku = randevu("Semih", BUGUN, "15:30", 30);
        defter.ekle(yarinki);
        defter.ekle(bugunku);

        assertEquals(Optional.of(bugunku), defter.siradakiRandevu());
        assertEquals(Duration.ofHours(5).plusMinutes(30), defter.kalanSure(bugunku));
    }

    @Test
    void iptalEdilenRandevuSilinir() {
        Randevu randevu = randevu("Semih", YARIN, "11:00", 30);
        defter.ekle(randevu);

        assertTrue(defter.iptalEt("Semih", randevu.baslangic()));
        assertFalse(defter.iptalEt("Semih", randevu.baslangic()));
        assertEquals(Optional.empty(), defter.siradakiRandevu());
    }

    @Test
    void gecersizRandevuOlusturulamaz() {
        LocalDateTime zaman = LocalDateTime.of(YARIN, saat("10:00"));

        assertThrows(IllegalArgumentException.class, () -> new Randevu(" ", zaman, Duration.ofMinutes(30)));
        assertThrows(IllegalArgumentException.class, () -> new Randevu("Semih", zaman, Duration.ZERO));
        assertThrows(IllegalArgumentException.class, () -> new Randevu("Semih", null, Duration.ofMinutes(30)));
    }

    private static Randevu randevu(String musteri, LocalDate gun, String saat, int dakika) {
        return new Randevu(musteri, LocalDateTime.of(gun, saat(saat)), Duration.ofMinutes(dakika));
    }

    private static LocalTime saat(String saat) {
        return LocalTime.parse(saat);
    }
}
