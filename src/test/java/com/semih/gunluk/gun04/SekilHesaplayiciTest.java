package com.semih.gunluk.gun04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static com.semih.gunluk.gun04.SekilHesaplayici.alan;
import static com.semih.gunluk.gun04.SekilHesaplayici.cevre;
import static com.semih.gunluk.gun04.SekilHesaplayici.enBuyuk;
import static com.semih.gunluk.gun04.SekilHesaplayici.tanimla;
import static com.semih.gunluk.gun04.SekilHesaplayici.tarif;
import static com.semih.gunluk.gun04.SekilHesaplayici.toplamAlan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SekilHesaplayiciTest {

    private static final double HASSASIYET = 1e-9;

    @Test
    void daireninAlaniVeCevresi() {
        Daire daire = new Daire(2);

        assertEquals(Math.PI * 4, alan(daire), HASSASIYET);
        assertEquals(Math.PI * 4, cevre(daire), HASSASIYET);
    }

    @Test
    void dikdortgeninAlaniVeCevresi() {
        Dikdortgen dikdortgen = new Dikdortgen(3, 5);

        assertEquals(15, alan(dikdortgen), HASSASIYET);
        assertEquals(16, cevre(dikdortgen), HASSASIYET);
    }

    @Test
    void karininAlaniVeCevresi() {
        Kare kare = new Kare(4);

        assertEquals(16, alan(kare), HASSASIYET);
        assertEquals(16, cevre(kare), HASSASIYET);
    }

    @Test
    @DisplayName("3-4-5 üçgeninin alanı Heron formülüyle 6 çıkar")
    void ucgeninAlaniHeronFormuluyle() {
        Ucgen ucgen = new Ucgen(3, 4, 5);

        assertEquals(6, alan(ucgen), HASSASIYET);
        assertEquals(12, cevre(ucgen), HASSASIYET);
    }

    @Test
    void eskenarUcgeninAlani() {
        // Kenarı a olan eşkenar üçgenin alanı: (√3 / 4) · a²
        assertEquals(Math.sqrt(3) / 4 * 36, alan(new Ucgen(6, 6, 6)), HASSASIYET);
    }

    @Test
    @DisplayName("Üçgen eşitsizliğini sağlamayan kenarlar reddedilir")
    void gecersizUcgenReddedilir() {
        assertThrows(IllegalArgumentException.class, () -> new Ucgen(1, 2, 10));
        // Düz çizgi: 1 + 2 = 3, alanı sıfır olan "üçgen" de kabul edilmez
        assertThrows(IllegalArgumentException.class, () -> new Ucgen(1, 2, 3));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -1, Double.NaN, Double.POSITIVE_INFINITY})
    void gecersizOlculerReddedilir(double deger) {
        assertThrows(IllegalArgumentException.class, () -> new Daire(deger));
        assertThrows(IllegalArgumentException.class, () -> new Kare(deger));
        assertThrows(IllegalArgumentException.class, () -> new Dikdortgen(1, deger));
        assertThrows(IllegalArgumentException.class, () -> new Ucgen(deger, 1, 1));
    }

    @Test
    @DisplayName("Record'lar değere göre eşittir")
    void recordlarDegereGoreEsittir() {
        assertEquals(new Kare(2), new Kare(2));
        assertEquals(new Dikdortgen(1, 2).hashCode(), new Dikdortgen(1, 2).hashCode());
    }

    @Test
    @DisplayName("when koşullu desenler doğru dalı seçer")
    void tarifKosulluDesenleriKullanir() {
        assertEquals("büyük bir daire", tarif(new Daire(150)));
        assertEquals("daire", tarif(new Daire(1)));
        assertEquals("kare biçiminde dikdörtgen", tarif(new Dikdortgen(4, 4)));
        assertEquals("dikdörtgen", tarif(new Dikdortgen(4, 5)));
        assertEquals("kare", tarif(new Kare(4)));
        assertEquals("eşkenar üçgen", tarif(new Ucgen(5, 5, 5)));
        assertEquals("ikizkenar üçgen", tarif(new Ucgen(5, 5, 8)));
        assertEquals("çeşitkenar üçgen", tarif(new Ucgen(3, 4, 5)));
    }

    @Test
    @DisplayName("tanimla null dahil her türlü nesneyi ele alır")
    void tanimlaHerTurluNesneyiTanir() {
        assertEquals("boş (null)", tanimla(null));
        assertEquals("kare, alanı 9.00", tanimla(new Kare(3)));
        assertEquals("negatif tam sayı", tanimla(-5));
        assertEquals("tam sayı", tanimla(42));
        assertEquals("boş metin", tanimla("   "));
        assertEquals("metin (5 karakter)", tanimla("Semih"));
        assertEquals("bilinmeyen: Double", tanimla(3.14));
    }

    @Test
    void farkliSekillerinToplamAlani() {
        List<Sekil> sekiller = List.of(new Kare(2), new Dikdortgen(2, 3), new Ucgen(3, 4, 5));

        assertEquals(4 + 6 + 6, toplamAlan(sekiller), HASSASIYET);
        assertEquals(0, toplamAlan(List.of()), HASSASIYET);
    }

    @Test
    void enBuyukSekliBulur() {
        Daire buyukDaire = new Daire(3); // alanı ≈ 28.27
        List<Sekil> sekiller = List.of(new Kare(5), buyukDaire, new Dikdortgen(2, 10));

        assertEquals(Optional.of(buyukDaire), enBuyuk(sekiller));
        assertEquals(Optional.empty(), enBuyuk(List.of()));
    }
}
