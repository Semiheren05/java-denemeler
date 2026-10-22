package com.semih.gunluk.gun07;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KatalogTest {

    private static final Kitap CALIKUSU = new Kitap("978-975-0000-01-1", "Çalıkuşu", "Reşat Nuri Güntekin", 1922);
    private static final Kitap CEVDET_BEY = new Kitap("978-975-0000-02-8", "Cevdet Bey ve Oğulları", "Orhan Pamuk", 1982);
    private static final Kitap BENIM_ADIM = new Kitap("978-975-0000-03-5", "Benim Adım Kırmızı", "Orhan Pamuk", 1998);
    private static final Kitap DOKUZUNCU = new Kitap("978-975-0000-04-2", "Dokuzuncu Hariciye Koğuşu", "Peyami Safa", 1930);

    private Katalog katalog;

    @BeforeEach
    void setUp() {
        katalog = new Katalog();
    }

    @Test
    @DisplayName("Aynı ISBN'li iki nesne eşittir ve aynı hashCode'u verir")
    void ayniIsbnEsittir() {
        Kitap tireli = new Kitap("978-975-0000-01-1", "Çalıkuşu", "Reşat Nuri Güntekin", 1922);
        Kitap tiresiz = new Kitap("9789750000011", "Çalıkuşu (yeni baskı)", "R. N. Güntekin", 2020);

        assertEquals(tireli, tiresiz);
        assertEquals(tireli.hashCode(), tiresiz.hashCode());
    }

    @Test
    void farkliIsbnEsitDegildir() {
        assertNotEquals(CALIKUSU, CEVDET_BEY);
        assertNotEquals(CALIKUSU, null);
        assertNotEquals(CALIKUSU, "Çalıkuşu");
    }

    @Test
    void ayniKitapIkinciKezEklenemez() {
        assertTrue(katalog.ekle(CALIKUSU));
        assertFalse(katalog.ekle(new Kitap("9789750000011", "Çalıkuşu", "Reşat Nuri Güntekin", 1922)));

        assertEquals(1, katalog.boyut());
    }

    @Test
    @DisplayName("hashCode yazılmazsa HashSet aynı kitabı iki kez tutar")
    void hashCodeOlmadanHashSetBozulur() {
        Set<HashCodesuzKitap> set = new HashSet<>();

        set.add(new HashCodesuzKitap("9789750000011"));
        set.add(new HashCodesuzKitap("9789750000011"));

        // equals true dese de nesneler büyük ihtimalle farklı kovalara düşer
        assertEquals(2, set.size());
    }

    @Test
    @DisplayName("compareTo equals ile tutarlı: TreeSet de tekrarı kabul etmez")
    void treeSetDeTekrariKabulEtmez() {
        Set<Kitap> sirali = new TreeSet<>();

        sirali.add(CALIKUSU);
        sirali.add(new Kitap("9789750000011", "Başka başlık", "Başka yazar", 2000));

        assertEquals(1, sirali.size());
    }

    @Test
    @DisplayName("Başlık sıralaması Türkçe alfabeye göre (Ç, C'den sonra gelir)")
    void basligaGoreTurkceSiralama() {
        katalog.hepsiniEkle(List.of(DOKUZUNCU, CALIKUSU, BENIM_ADIM, CEVDET_BEY));

        assertEquals(List.of(BENIM_ADIM, CEVDET_BEY, CALIKUSU, DOKUZUNCU), katalog.sirala(Kitap.BASLIGA_GORE));
    }

    @Test
    void yilaGoreSiralama() {
        katalog.hepsiniEkle(List.of(BENIM_ADIM, CALIKUSU, CEVDET_BEY, DOKUZUNCU));

        assertEquals(List.of(CALIKUSU, DOKUZUNCU, CEVDET_BEY, BENIM_ADIM), katalog.sirala(Kitap.YILA_GORE));
    }

    @Test
    void yazaraGoreGruplama() {
        katalog.hepsiniEkle(List.of(BENIM_ADIM, CALIKUSU, CEVDET_BEY));

        Map<String, List<Kitap>> gruplar = katalog.yazaraGore();

        assertEquals(List.of("Orhan Pamuk", "Reşat Nuri Güntekin"), List.copyOf(gruplar.keySet()));
        assertEquals(List.of(CEVDET_BEY, BENIM_ADIM), gruplar.get("Orhan Pamuk"));
    }

    @Test
    void isbnIleBulunur() {
        katalog.ekle(CEVDET_BEY);

        assertEquals(Optional.of(CEVDET_BEY), katalog.bul("978 975 0000 02 8"));
        assertEquals(Optional.empty(), katalog.bul("9789750000035"));
    }

    @Test
    void cikarilanKitapKatalogdanGider() {
        katalog.ekle(CALIKUSU);

        assertTrue(katalog.cikar("978-975-0000-01-1"));
        assertFalse(katalog.cikar("978-975-0000-01-1"));
        assertFalse(katalog.iceriyor(CALIKUSU));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "123", "978-975-0000-01", "97897500000AB"})
    void gecersizIsbnReddedilir(String isbn) {
        assertThrows(IllegalArgumentException.class, () -> new Kitap(isbn, "Başlık", "Yazar", 2000));
    }

    @Test
    void baslikVeYazarZorunludur() {
        assertThrows(IllegalArgumentException.class, () -> new Kitap("9789750000011", " ", "Yazar", 2000));
        assertThrows(IllegalArgumentException.class, () -> new Kitap("9789750000011", "Başlık", null, 2000));
    }

    /** Hatalı örnek: equals var ama hashCode yok. Bunu gerçek kodda asla yapma. */
    private static final class HashCodesuzKitap {

        private final String isbn;

        HashCodesuzKitap(String isbn) {
            this.isbn = isbn;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof HashCodesuzKitap diger && isbn.equals(diger.isbn);
        }
    }
}
