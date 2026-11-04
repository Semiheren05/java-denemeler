package com.semih.gunluk.gun19;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DogrulayiciTest {

    private final Dogrulayici dogrulayici = new Dogrulayici();

    @Test
    void kurallaraUyanFormdaIhlalYok() {
        assertTrue(dogrulayici.dogrula(new UyelikFormu("Ayşe", "ayse@example.com", 27, "34710")).isEmpty());
    }

    @Test
    void butunIhlallerAlanSirasiylaRaporlanir() {
        List<Ihlal> ihlaller = dogrulayici.dogrula(new UyelikFormu("A", "ayse@", 15, "347"));

        assertEquals(List.of(
                new Ihlal("ad", "2 ile 50 karakter arasında olmalı"),
                new Ihlal("eposta", "geçerli bir e-posta olmalı"),
                new Ihlal("yas", "18 ile 120 arasında olmalı"),
                new Ihlal("postaKodu", "5 haneli olmalı")), ihlaller);
    }

    @Test
    void bosZorunluAlandaDigerKurallaraBakilmaz() {
        List<Ihlal> ihlaller = dogrulayici.dogrula(new UyelikFormu("  ", null, 30, null));

        // "  " hem boş hem 2 karakterden kısa; sadece "boş olamaz" deniyor
        assertEquals(List.of(new Ihlal("ad", "boş olamaz"), new Ihlal("eposta", "boş olamaz")), ihlaller);
    }

    @Test
    void zorunluOlmayanAlanBosBirakilabilir() {
        // postaKodu null: @Desen var ama @Zorunlu yok
        assertTrue(dogrulayici.dogrula(new UyelikFormu("Ali", "ali@example.com", 40, null)).isEmpty());
    }

    @Test
    void siradanSinifinPrivateAlanlariDaOkunur() {
        assertEquals(List.of(new Ihlal("ad", "boş olamaz"), new Ihlal("stok", "0 ile 10000 arasında olmalı")),
                dogrulayici.dogrula(new Urun(null, -5)));
        assertTrue(dogrulayici.dogrula(new Urun("Klavye", 12)).isEmpty());
    }

    @Test
    void anotasyonParametreleriReflectionlaOkunur() throws Exception {
        Uzunluk uzunluk = UyelikFormu.class.getDeclaredField("ad").getAnnotation(Uzunluk.class);

        assertEquals(2, uzunluk.min());
        assertEquals(50, uzunluk.max());
    }

    @Test
    void retentionClassOlanAnotasyonCalisirkenGorunmez() throws Exception {
        // Varsayılan retention CLASS: anotasyon .class dosyasına yazılıyor ama JVM onu yüklemiyor
        assertNull(Notlu.class.getDeclaredField("not").getAnnotation(DerlemeNotu.class));
        assertNotNull(Notlu.class.getDeclaredField("not").getAnnotation(CalismaNotu.class));
    }

    @Test
    void anotasyonsuzSinifHicKuralTasimaz() {
        assertTrue(dogrulayici.dogrula("herhangi bir metin").isEmpty());
        assertThrows(IllegalArgumentException.class, () -> dogrulayici.dogrula(null));
    }

    @Test
    void herSinifinAlanlariBirKezTaranir() {
        for (int i = 0; i < 1_000; i++) {
            dogrulayici.dogrula(new UyelikFormu("Ayşe", "ayse@example.com", 27, "34710"));
        }
        dogrulayici.dogrula(new Urun("Klavye", 1));

        // 1.001 doğrulama, iki farklı sınıf: reflection sadece iki kez çalıştı
        assertEquals(2, dogrulayici.taranmisSinifSayisi());
    }

    static class Urun {
        @Zorunlu
        private final String ad;
        @Aralik(min = 0, max = 10_000)
        private final int stok;
        // Kural yok: hiç okunmuyor
        private final String aciklama = "";

        Urun(String ad, int stok) {
            this.ad = ad;
            this.stok = stok;
        }
    }

    @interface DerlemeNotu {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface CalismaNotu {
    }

    static class Notlu {
        @DerlemeNotu
        @CalismaNotu
        String not;
    }
}
