package com.semih.gunluk.gun18;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UyeKaydiTest {

    private static final KayitFormu DOGRU = new KayitFormu(
            "  Ayşe   Yılmaz ", "10000000146", "tr33 0006 1005 1978 6457 8413 26", "0532 123 45 67", "1998");

    private UyeKaydi kayit;

    @BeforeEach
    void setUp() {
        kayit = new UyeKaydi(Year.of(2026));
    }

    @Test
    void dogruFormDuzenlenmisBilgilerleUyeOlur() {
        Sonuc<Uye> sonuc = kayit.kaydet(DOGRU);

        assertTrue(sonuc.basariliMi());
        assertEquals(new Uye("Ayşe Yılmaz", "10000000146", "TR33 0006 1005 1978 6457 8413 26", "+905321234567", 1998),
                sonuc.deger());
        assertEquals(1, kayit.uyeler().size());
    }

    @Test
    void butunAlanHatalariBirdenGosterilir() {
        Sonuc<Uye> sonuc = kayit.kaydet(new KayitFormu(" ", "10000000147", "TR00", "0212 123 45 67", "bin dokuz yüz"));

        assertEquals(List.of(
                "Ad boş olamaz",
                "T.C. kimlik numarası geçersiz",
                "IBAN geçersiz",
                "Cep telefonu numarası geçersiz",
                "Doğum yılı sayı olmalı"), hatalar(sonuc));
        assertTrue(kayit.uyeler().isEmpty());
    }

    @Test
    void onSekizYasindanKucukKaydolamaz() {
        Sonuc<Uye> sonuc = kayit.kaydet(new KayitFormu("Deniz", "12345678950", DOGRU.iban(), DOGRU.telefon(), "2010"));

        assertEquals(List.of("Üye olmak için 18 yaşında olmak gerekir"), hatalar(sonuc));
    }

    @Test
    void buYilOnSekizineGirenKaydolabilir() {
        assertTrue(kayit.kaydet(new KayitFormu("Ali", "12345678950", DOGRU.iban(), DOGRU.telefon(), "2008")).basariliMi());
    }

    @Test
    void ayniTcIleIkinciKayitOlmaz() {
        kayit.kaydet(DOGRU);

        Sonuc<Uye> ikinci = kayit.kaydet(new KayitFormu("Başka Biri", DOGRU.tcKimlikNo(), DOGRU.iban(), "05559876543", "1990"));

        assertEquals(List.of("Bu T.C. kimlik numarasıyla zaten bir üyelik var"), hatalar(ikinci));
        assertEquals(1, kayit.uyeler().size());
    }

    @Test
    void isKurallariIlkHatadaDurur() {
        // Hem reşit değil hem de TC zaten kayıtlı: sadece ilk kural (yaş) raporlanıyor
        kayit.kaydet(DOGRU);

        Sonuc<Uye> sonuc = kayit.kaydet(new KayitFormu("Can", DOGRU.tcKimlikNo(), DOGRU.iban(), DOGRU.telefon(), "2015"));

        assertEquals(List.of("Üye olmak için 18 yaşında olmak gerekir"), hatalar(sonuc));
    }

    @Test
    void hataliSonuctaMapFonksiyonuCagrilmaz() {
        int[] cagri = {0};
        Sonuc<Integer> hatali = Sonuc.hatali("bozuk");

        Sonuc<Integer> iki = hatali.map(x -> {
            cagri[0]++;
            return x * 2;
        });

        assertEquals(0, cagri[0]);
        assertEquals(List.of("bozuk"), hatalar(iki));
        assertEquals(Sonuc.basarili(10), Sonuc.basarili(5).map(x -> x * 2));
    }

    @Test
    void flatMapIlkHatadaZinciriKeser() {
        Sonuc<Integer> sonuc = Sonuc.basarili("42")
                .flatMap(metin -> Sonuc.dene(() -> Integer.parseInt(metin), "sayı değil"))
                .flatMap(sayi -> sayi > 100 ? Sonuc.basarili(sayi) : Sonuc.<Integer>hatali("100'den büyük olmalı"))
                .flatMap(sayi -> Sonuc.hatali("buraya hiç gelinmemeli"));

        assertEquals(List.of("100'den büyük olmalı"), hatalar(sonuc));
    }

    @Test
    void katlaIleIkiDurumTekCevabaDoner() {
        Sonuc<Uye> iyi = kayit.kaydet(DOGRU);
        Sonuc<Uye> kotu = kayit.kaydet(DOGRU);

        assertEquals("201 Ayşe Yılmaz", iyi.katla(uye -> "201 " + uye.ad(), h -> "400 " + h));
        assertEquals("400 [Bu T.C. kimlik numarasıyla zaten bir üyelik var]", kotu.katla(uye -> "201", h -> "400 " + h));
    }

    @Test
    void switchIleIkiDurumdaDefaultsuzElAlinir() {
        String mesaj = switch (kayit.kaydet(DOGRU)) {
            case Sonuc.Basarili<Uye>(Uye uye) -> "Hoş geldin " + uye.ad();
            case Sonuc.Hatali<Uye>(List<String> hatalar) -> hatalar.size() + " hata";
        };

        assertEquals("Hoş geldin Ayşe Yılmaz", mesaj);
    }

    @Test
    void hataliSonucunDegeriAlinamaz() {
        assertThrows(IllegalStateException.class, () -> Sonuc.hatali("bozuk").deger());
        assertThrows(IllegalArgumentException.class, () -> new Sonuc.Hatali<>(List.of()));
        assertFalse(Sonuc.hatali("bozuk").basariliMi());
    }

    private static List<String> hatalar(Sonuc<?> sonuc) {
        if (sonuc instanceof Sonuc.Hatali<?>(List<String> hatalar)) {
            return hatalar;
        }
        throw new AssertionError("Hatalı sonuç bekleniyordu: " + sonuc);
    }
}
