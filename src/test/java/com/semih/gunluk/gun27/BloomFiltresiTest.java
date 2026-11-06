package com.semih.gunluk.gun27;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BloomFiltresiTest {

    @ParameterizedTest
    @CsvSource({
            "1000,    0.01,  9586,    7",
            "1000,    0.001, 14378,   10",
            "1000000, 0.01,  9585059, 7"
    })
    void boyutVeHashSayisiFormuldenGelir(int eleman, double oran, int bit, int hash) {
        BloomFiltresi filtre = BloomFiltresi.icin(eleman, oran);

        assertEquals(bit, filtre.bitSayisi());
        assertEquals(hash, filtre.hashSayisi());
    }

    @Test
    void eklenenHicbirKelimeKaybolmaz() {
        BloomFiltresi filtre = BloomFiltresi.icin(10_000, 0.01);
        for (int i = 0; i < 10_000; i++) {
            filtre.ekle("kullanici" + i);
        }

        for (int i = 0; i < 10_000; i++) {
            assertTrue(filtre.belkiVar("kullanici" + i), "kullanici" + i);
        }
    }

    @Test
    void olculenYanilmaOraniHedefeYakin() {
        BloomFiltresi filtre = BloomFiltresi.icin(10_000, 0.01);
        for (int i = 0; i < 10_000; i++) {
            filtre.ekle("kullanici" + i);
        }

        int yanlis = 0;
        int deneme = 100_000;
        for (int i = 0; i < deneme; i++) {
            if (filtre.belkiVar("hic-eklenmedi-" + i)) {
                yanlis++;
            }
        }
        double oran = (double) yanlis / deneme;

        // Hedef %1. Sıfır değil (Bloom filtresi yanılır), ama hedefin çok üstünde de değil
        assertTrue(oran > 0.005 && oran < 0.015, "Ölçülen oran: " + oran);
        assertEquals(0.01, filtre.tahminiYanlisPozitifOrani(), 0.001);
    }

    @Test
    void bosFiltreHerSeyeHayirDer() {
        BloomFiltresi filtre = BloomFiltresi.icin(100, 0.01);

        assertFalse(filtre.belkiVar("semih"));
        assertEquals(0.0, filtre.dolulukOrani());
        assertEquals(0.0, filtre.tahminiYanlisPozitifOrani());
    }

    @Test
    void kapasiteAsilincaYanilmaHizlaArtar() {
        BloomFiltresi filtre = BloomFiltresi.icin(1_000, 0.01);
        for (int i = 0; i < 5_000; i++) {   // beklenenin beş katı
            filtre.ekle("k" + i);
        }

        assertTrue(filtre.tahminiYanlisPozitifOrani() > 0.5, "" + filtre.tahminiYanlisPozitifOrani());
        assertTrue(filtre.dolulukOrani() > 0.95, "" + filtre.dolulukOrani());
    }

    @Test
    void dogruDolulukYaklasikYari() {
        // En uygun k seçildiyse, kapasite dolduğunda bitlerin yaklaşık yarısı 1 olur
        BloomFiltresi filtre = BloomFiltresi.icin(10_000, 0.01);
        for (int i = 0; i < 10_000; i++) {
            filtre.ekle("kullanici" + i);
        }

        assertEquals(0.5, filtre.dolulukOrani(), 0.02);
    }

    @Test
    void birlesimIkisinindeKelimeleriniIcerir() {
        BloomFiltresi istanbul = BloomFiltresi.icin(100, 0.01);
        BloomFiltresi ankara = BloomFiltresi.icin(100, 0.01);
        istanbul.ekle("ayse");
        ankara.ekle("mehmet");

        BloomFiltresi hepsi = istanbul.birlestir(ankara);

        assertTrue(hepsi.belkiVar("ayse"));
        assertTrue(hepsi.belkiVar("mehmet"));
        assertFalse(istanbul.belkiVar("mehmet"));   // asıllar değişmedi
        assertThrows(IllegalArgumentException.class, () -> istanbul.birlestir(BloomFiltresi.icin(1_000, 0.01)));
    }

    @Test
    void turkceHarflerFarkliKelimeSayilir() {
        BloomFiltresi filtre = BloomFiltresi.icin(100, 0.001);
        filtre.ekle("şifre");

        assertTrue(filtre.belkiVar("şifre"));
        assertFalse(filtre.belkiVar("sifre"));
    }

    @Test
    void hashKolayCakismaz() {
        // String.hashCode()'da "Aa" ile "BB" aynı (2112); 64 bitlik hash'te değil
        assertEquals("Aa".hashCode(), "BB".hashCode());
        assertNotEquals(BloomFiltresi.hash64("Aa"), BloomFiltresi.hash64("BB"));
    }

    @Test
    void gecersizAyarlarReddedilir() {
        assertThrows(IllegalArgumentException.class, () -> BloomFiltresi.icin(0, 0.01));
        assertThrows(IllegalArgumentException.class, () -> BloomFiltresi.icin(100, 0));
        assertThrows(IllegalArgumentException.class, () -> BloomFiltresi.icin(100, 1));
        assertThrows(IllegalArgumentException.class, () -> BloomFiltresi.icin(100, Double.NaN));
    }
}
