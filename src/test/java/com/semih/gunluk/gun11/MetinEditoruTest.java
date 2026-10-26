package com.semih.gunluk.gun11;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MetinEditoruTest {

    private MetinEditoru editor;

    @BeforeEach
    void setUp() {
        editor = new MetinEditoru(100);
    }

    @Test
    void yazilanlarSiraylaEklenir() {
        editor.yaz("Merhaba");
        editor.yaz(" dünya");

        assertEquals("Merhaba dünya", editor.metin());
    }

    @Test
    void geriAlSonDuzenlemeyiKaldirir() {
        editor.yaz("Merhaba");
        editor.yaz(" dünya");

        assertTrue(editor.geriAl());

        assertEquals("Merhaba", editor.metin());
    }

    @Test
    void geriAlinanIleriAlinabilir() {
        editor.yaz("Merhaba");
        editor.yaz(" dünya");
        editor.geriAl();

        assertTrue(editor.ileriAl());

        assertEquals("Merhaba dünya", editor.metin());
    }

    @Test
    void silinenYaziGeriAlinincaAyniYerineGelir() {
        editor.yaz("Java çok güzel");
        editor.sil(4, 4);   // " çok"
        assertEquals("Java güzel", editor.metin());

        editor.geriAl();

        assertEquals("Java çok güzel", editor.metin());
    }

    @Test
    void ortayaEkleme() {
        editor.yaz("Semih Eren");
        editor.ekle(5, " Ali");

        assertEquals("Semih Ali Eren", editor.metin());
        editor.geriAl();
        assertEquals("Semih Eren", editor.metin());
    }

    @Test
    void hepsiGeriAlinincaBosMetinKalir() {
        editor.yaz("bir");
        editor.yaz(" iki");
        editor.sil(0, 3);

        while (editor.geriAl()) {
            // yığın boşalana kadar
        }

        assertEquals("", editor.metin());
        assertEquals(3, editor.ileriAlinabilecekSayisi());
    }

    @Test
    void yeniDuzenlemeIleriAlGecmisiniSiler() {
        editor.yaz("a");
        editor.yaz("b");
        editor.geriAl();

        editor.yaz("c");

        assertEquals("ac", editor.metin());
        assertEquals(0, editor.ileriAlinabilecekSayisi());
        assertFalse(editor.ileriAl());
    }

    @Test
    void bosYigindaGeriVeIleriAlHicbirSeyYapmaz() {
        assertFalse(editor.geriAl());
        assertFalse(editor.ileriAl());
        assertEquals("", editor.metin());
    }

    @Test
    void gecmisSiniriAsilincaEnEskiDuzenlemeUnutulur() {
        MetinEditoru kucuk = new MetinEditoru(2);
        kucuk.yaz("1");
        kucuk.yaz("2");
        kucuk.yaz("3");

        assertEquals(2, kucuk.geriAlinabilecekSayisi());
        kucuk.geriAl();
        kucuk.geriAl();

        // "1" yazmak artık geri alınamıyor
        assertFalse(kucuk.geriAl());
        assertEquals("1", kucuk.metin());
    }

    @Test
    void metinDisindaSilmeYaDaEklemeYapilamaz() {
        editor.yaz("abc");

        assertThrows(IndexOutOfBoundsException.class, () -> editor.sil(2, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> editor.ekle(4, "x"));
        assertThrows(IndexOutOfBoundsException.class, () -> editor.sil(-1, 1));
        assertEquals("abc", editor.metin());
        assertEquals(1, editor.geriAlinabilecekSayisi());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -3})
    void silmeUzunluguPozitifOlmali(int uzunluk) {
        editor.yaz("abc");

        assertThrows(IllegalArgumentException.class, () -> editor.sil(0, uzunluk));
    }

    @Test
    void herDuzenleminTersiKendisiniGeriAlir() {
        StringBuilder metin = new StringBuilder("kod");
        Duzenleme ekleme = new Duzenleme.Ekleme(3, "lama");

        ekleme.uygula(metin);
        assertEquals("kodlama", metin.toString());
        ekleme.tersi().uygula(metin);
        assertEquals("kod", metin.toString());
        assertEquals(ekleme, ekleme.tersi().tersi());
    }
}
