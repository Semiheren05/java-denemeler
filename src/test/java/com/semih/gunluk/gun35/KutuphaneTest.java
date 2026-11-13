package com.semih.gunluk.gun35;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KutuphaneTest {

    @TempDir
    Path klasor;

    private AyarlanabilirSaat saat;
    private Kutuphane kutuphane;

    @BeforeEach
    void hazirla() {
        saat = new AyarlanabilirSaat(Instant.parse("2026-11-02T10:00:00Z"));
        kutuphane = new Kutuphane(saat)
                .ekle(new Kitap("111", "Tutunamayanlar", "Oğuz Atay"))
                .ekle(new Kitap("222", "Tehlikeli Oyunlar", "Oğuz Atay"))
                .ekle(new Kitap("333", "Kürk Mantolu Madonna", "Sabahattin Ali"))
                .ekle(new Kitap("444", "İçimizdeki Şeytan", "Sabahattin Ali"))
                .ekle(new Kitap("555", "Saatleri Ayarlama Enstitüsü", "Ahmet Hamdi Tanpınar"));
    }

    private void gunGecir(int gun) {
        saat.ilerlet(Duration.ofDays(gun));
    }

    @Test
    void oduncVerince_kitapMusaitDegil_sonTeslim14GunSonra() {
        Odunc odunc = kutuphane.oduncVer("111", "U1");

        assertEquals(LocalDate.of(2026, 11, 16), odunc.sonTeslim());
        assertFalse(kutuphane.musaitMi("111"));
        assertTrue(kutuphane.musaitMi("222"));
    }

    @Test
    void ayniKitapIkinciKezVerilemez_olmayanKitapHata() {
        kutuphane.oduncVer("111", "U1");

        KutuphaneHatasi hata = assertThrows(KutuphaneHatasi.class, () -> kutuphane.oduncVer("111", "U2"));
        assertEquals("'Tutunamayanlar' şu an başka bir üyede", hata.getMessage());
        assertThrows(KutuphaneHatasi.class, () -> kutuphane.oduncVer("999", "U2"));
    }

    @Test
    void uyeEnFazlaUcKitapAlir() {
        kutuphane.oduncVer("111", "U1");
        kutuphane.oduncVer("222", "U1");
        kutuphane.oduncVer("333", "U1");

        assertThrows(KutuphaneHatasi.class, () -> kutuphane.oduncVer("444", "U1"));
        kutuphane.iadeEt("111");
        kutuphane.oduncVer("444", "U1");   // biri dönünce tekrar alabiliyor
    }

    @Test
    void gecikmeCezasi_gundeBes_tavanYuz() {
        kutuphane.oduncVer("111", "U1");
        kutuphane.oduncVer("222", "U2");
        kutuphane.oduncVer("333", "U3");

        gunGecir(14);
        assertEquals(BigDecimal.ZERO, kutuphane.iadeEt("111"));               // tam gününde
        gunGecir(3);
        assertEquals(new BigDecimal("15"), kutuphane.iadeEt("222"));          // 3 gün geç
        gunGecir(30);
        assertEquals(new BigDecimal("100"), kutuphane.iadeEt("333"));         // 33 gün: tavan
    }

    @Test
    void gecikmisKitabiOlanYeniKitapAlamaz() {
        kutuphane.oduncVer("111", "U1");
        gunGecir(15);

        KutuphaneHatasi hata = assertThrows(KutuphaneHatasi.class, () -> kutuphane.oduncVer("222", "U1"));

        assertEquals("U1 önce geciken kitabını iade etmeli", hata.getMessage());
    }

    @Test
    void uzatma_birKez_gecikmisseOlmaz() {
        kutuphane.oduncVer("111", "U1");
        kutuphane.oduncVer("222", "U2");
        gunGecir(10);

        assertEquals(LocalDate.of(2026, 11, 23), kutuphane.uzat("111").sonTeslim());
        assertThrows(KutuphaneHatasi.class, () -> kutuphane.uzat("111"));

        gunGecir(5);   // 222'nin süresi 16'sında doldu, bugün 17'si
        assertEquals("Gecikmiş ödünç uzatılamaz", assertThrows(KutuphaneHatasi.class, () -> kutuphane.uzat("222")).getMessage());
    }

    @Test
    void enCokOkunanYazarlar() {
        kutuphane.oduncVer("111", "U1");
        kutuphane.iadeEt("111");
        kutuphane.oduncVer("111", "U2");
        kutuphane.oduncVer("333", "U3");
        kutuphane.oduncVer("222", "U4");
        kutuphane.oduncVer("555", "U5");

        List<Map.Entry<String, Long>> ilkIki = kutuphane.enCokOkunanYazarlar(2);

        assertEquals(List.of(Map.entry("Oğuz Atay", 3L), Map.entry("Ahmet Hamdi Tanpınar", 1L)), ilkIki);
    }

    @Test
    void gecikenler_enCokGecikenOnce() {
        kutuphane.oduncVer("111", "U1");
        gunGecir(3);
        kutuphane.oduncVer("222", "U2");
        kutuphane.oduncVer("333", "U3");
        gunGecir(14);   // 111: 3 gün, 222 ve 333: 0 gün gecikmiş
        kutuphane.iadeEt("333");
        gunGecir(2);    // 111: 5 gün, 222: 2 gün

        List<Odunc> gecikenler = kutuphane.gecikenler();

        assertEquals(List.of("111", "222"), gecikenler.stream().map(Odunc::isbn).toList());
        assertEquals(5, gecikenler.get(0).gecikmeGunu(LocalDate.now(saat)));
    }

    @Test
    void dosyayaKaydedipGeriYukleyince_ayniDurum() throws Exception {
        kutuphane.oduncVer("111", "U1");
        kutuphane.oduncVer("333", "U2");
        kutuphane.uzat("333");
        kutuphane.iadeEt("111");
        Path dosya = klasor.resolve("kutuphane.txt");

        KutuphaneDosyasi.kaydet(kutuphane, dosya);
        Kutuphane yuklenen = KutuphaneDosyasi.yukle(dosya, saat);

        assertEquals(kutuphane.kitaplar(), yuklenen.kitaplar());
        assertEquals(kutuphane.oduncler(), yuklenen.oduncler());
        assertFalse(yuklenen.musaitMi("333"));
        assertTrue(Files.readAllLines(dosya).contains("O;111;U1;2026-11-02;2026-11-16;false;2026-11-02"));
        assertThrows(IllegalArgumentException.class, () -> new Kitap("666", "A;B", "C"));
    }

    @Test
    void komutYorumlayici() {
        KomutYorumlayici konsol = new KomutYorumlayici(kutuphane);

        assertEquals("Verildi, son teslim 2026-11-16", konsol.calistir("ver 111 U1"));
        assertEquals("Hata: 'Tutunamayanlar' şu an başka bir üyede", konsol.calistir("ver 111 U2"));
        assertEquals("Kullanım: ver <isbn> <üye>", konsol.calistir("ver 111"));
        gunGecir(16);
        assertEquals("Gecikenler: [111 U1]", konsol.calistir("geciken"));
        assertEquals("İade alındı, ceza 10 TL", konsol.calistir("  iade   111 "));
        assertEquals("Bilinmeyen komut: sil (ver, iade, geciken)", konsol.calistir("sil 111"));
    }
}
