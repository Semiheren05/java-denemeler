package com.semih.gunluk.gun41;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnicodeTest {

    private static final Locale TR = Locale.forLanguageTag("tr-TR");

    @Test
    void slug_turkceBaslik() {
        assertEquals("javada-coklu-is-parcacigi-ve-senkronizasyon",
                SlugUretici.slug("Java'da Çoklu İş Parçacığı ve Senkronizasyon!", 80));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "IŞIK HIZINDA ÖĞRENME|isik-hizinda-ogrenme",
            "Café & Crème Brûlée|cafe-creme-brulee",
            "  Spring   Boot 3.4 — Yenilikler  |spring-boot-3-4-yenilikler",
            "Merhaba 👋 Dünya 🌍|merhaba-dunya",
            "Ağaç, Yığın; Kuyruk?|agac-yigin-kuyruk",
            "Semih’in Notları|semihin-notlari"}, delimiter = '|')
    void slug_cesitliBasliklar(String baslik, String beklenen) {
        assertEquals(beklenen, SlugUretici.slug(baslik, 80));
    }

    @Test
    void slug_uzunlukSiniriKelimeOrtasindanKesmez() {
        String slug = SlugUretici.slug("Dağıtık Sistemlerde Tutarlılık Modelleri Üzerine Notlar", 30);

        assertEquals("dagitik-sistemlerde-tutarlilik", slug);   // 30 karakter, kelime sınırında
        assertTrue(slug.length() <= 30);
        assertThrows(IllegalArgumentException.class, () -> SlugUretici.slug("🔥🔥🔥", 80));
    }

    @Test
    void slug_alinmissaNumaraEkler() {
        assertEquals("java-notlari", SlugUretici.benzersiz("java-notlari", Set.of()));
        assertEquals("java-notlari-2", SlugUretici.benzersiz("java-notlari", Set.of("java-notlari")));
        assertEquals("java-notlari-4",
                SlugUretici.benzersiz("java-notlari", Set.of("java-notlari", "java-notlari-2", "java-notlari-3")));
    }

    @Test
    void uzunlukDortFarkliSey() {
        String bas = "👍🏽";                        // başparmak + ten rengi
        String aile = "👨‍👩‍👧";                       // üç kişi, aralarında görünmez birleştirici (ZWJ)

        assertEquals(4, bas.length());
        assertEquals(2, bas.codePointCount(0, bas.length()));
        assertEquals(1, MetinOlcer.grafemSayisi(bas));
        assertEquals(5, aile.codePointCount(0, aile.length()));
        assertEquals(1, MetinOlcer.grafemSayisi(aile));
        assertEquals(6, MetinOlcer.grafemSayisi("Ağaç 🌳"));
    }

    @Test
    void kisaltma_emojiyiBolmez() {
        String mesaj = "Merhaba👋 nasılsın";

        String hatali = MetinOlcer.hataliKisalt(mesaj, 8);
        assertTrue(Character.isHighSurrogate(hatali.charAt(7)));   // emojinin yarısı: ekranda � görünür
        assertEquals("Merhaba👋…", MetinOlcer.kisalt(mesaj, 8));
        assertEquals("Merhaba", MetinOlcer.kisalt("Merhaba", 8));
    }

    @Test
    void normallestirme_ayniGorunenFarkliMetin() {
        String birlesik = "Caf\u00e9";       // é tek kod noktası
        String ayrik = "Cafe\u0301";         // e + birleşen aksan

        assertNotEquals(birlesik, ayrik);   // ekranda aynı, equals false
        assertEquals(4, birlesik.length());
        assertEquals(5, ayrik.length());
        assertEquals(birlesik, Normalizer.normalize(ayrik, Normalizer.Form.NFC));   // karşılaştırmadan önce normalleştir
    }

    @Test
    void turkceBuyukKucukHarfTuzagi() {
        assertEquals("istanbul", "İSTANBUL".toLowerCase(TR));
        assertEquals("i\u0307stanbul", "İSTANBUL".toLowerCase(Locale.ROOT));   // i + birleşen nokta: 8 değil 9 char
        assertEquals("TİTLE", "title".toUpperCase(TR));                        // teknik anahtarda (HTTP başlığı, enum adı) yanlış
        assertEquals("TITLE", "title".toUpperCase(Locale.ROOT));
        assertTrue("sinir".equalsIgnoreCase("sınır"));   // dilden bağımsız: ı ve i ikisi de I olur, farklı kelimeler "eşit"
        assertEquals("ısık", "ISIK".toLowerCase(TR));
    }

    @ParameterizedTest
    @CsvSource({"a, 1", "ğ, 2", "€, 3", "🌍, 4", "Ağaç, 6"})
    void utf8BaytSayisi(String metin, int bayt) {
        assertEquals(bayt, MetinOlcer.utf8Bayt(metin));
    }

    @Test
    void rakamTuzagi_unicodeRakamlari() {
        String arapcaUc = "\u0663";   // ٣ (Arapça-Hint rakamı 3)

        assertTrue(Character.isDigit(arapcaUc.charAt(0)));
        assertEquals(3, Integer.parseInt(arapcaUc));             // parseInt Unicode rakamlarını kabul ediyor
        assertFalse(arapcaUc.matches("\\d"));                    // regex \d varsayılan olarak sadece 0-9
        assertFalse(arapcaUc.matches("[0-9]"));                  // doğrulamada açık aralık kullan
    }
}
