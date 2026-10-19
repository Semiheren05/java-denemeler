package com.semih.gunluk.gun01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankaHesabiTest {

    private BankaHesabi hesap;

    @BeforeEach
    void setUp() {
        hesap = new BankaHesabi("TR001", "Semih");
    }

    @Test
    @DisplayName("Yeni hesabın bakiyesi sıfırdır")
    void yeniHesapSifirBakiyeyleBaslar() {
        assertEquals(tl("0.00"), hesap.getBakiye());
        assertEquals(List.of(), hesap.getIslemGecmisi());
    }

    @Test
    void paraYatirmaBakiyeyiArtirir() {
        hesap.paraYatir(tl("100"));
        hesap.paraYatir(tl("50.25"));

        assertEquals(tl("150.25"), hesap.getBakiye());
    }

    @Test
    void paraCekmeBakiyeyiAzaltir() {
        hesap.paraYatir(tl("100"));
        hesap.paraCek(tl("30.50"));

        assertEquals(tl("69.50"), hesap.getBakiye());
    }

    @Test
    void bakiyeninTamaminiCekmekMumkundur() {
        hesap.paraYatir(tl("100"));
        hesap.paraCek(tl("100"));

        assertEquals(tl("0.00"), hesap.getBakiye());
    }

    @Test
    @DisplayName("Yetersiz bakiyede hata fırlatılır ve bakiye değişmez")
    void yetersizBakiyedeHataFirlatilir() {
        hesap.paraYatir(tl("50"));

        YetersizBakiyeException hata = assertThrows(YetersizBakiyeException.class,
                () -> hesap.paraCek(tl("80")));

        assertEquals(tl("50.00"), hata.getBakiye());
        assertEquals(tl("80.00"), hata.getIstenenTutar());
        assertEquals(tl("50.00"), hesap.getBakiye());
        assertEquals(1, hesap.getIslemGecmisi().size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-10", "0.001"})
    @DisplayName("Sıfır, negatif ya da kuruştan küçük tutar kabul edilmez")
    void gecersizTutarReddedilir(String tutar) {
        assertThrows(IllegalArgumentException.class, () -> hesap.paraYatir(new BigDecimal(tutar)));
        assertThrows(IllegalArgumentException.class, () -> hesap.paraCek(new BigDecimal(tutar)));
    }

    @Test
    void bosTutarReddedilir() {
        assertThrows(IllegalArgumentException.class, () -> hesap.paraYatir(null));
    }

    @Test
    @DisplayName("double ile olan 0.1 + 0.2 hatası BigDecimal'da yok")
    void ondalikliTutarlarKesinToplanir() {
        hesap.paraYatir(tl("0.10"));
        hesap.paraYatir(tl("0.20"));

        assertEquals(tl("0.30"), hesap.getBakiye());
    }

    @Test
    void islemGecmisiSiraylaTutulur() {
        hesap.paraYatir(tl("100"));
        hesap.paraCek(tl("40"));

        assertEquals(List.of(
                new Islem(IslemTuru.YATIRMA, tl("100.00"), tl("100.00")),
                new Islem(IslemTuru.CEKME, tl("40.00"), tl("60.00"))
        ), hesap.getIslemGecmisi());
    }

    @Test
    void islemGecmisiDisaridanDegistirilemez() {
        hesap.paraYatir(tl("10"));

        assertThrows(UnsupportedOperationException.class,
                () -> hesap.getIslemGecmisi().clear());
    }

    @Test
    void transferIkiHesabiDaGunceller() {
        BankaHesabi hedef = new BankaHesabi("TR002", "Ayşe");
        hesap.paraYatir(tl("200"));

        hesap.transferEt(hedef, tl("75"));

        assertEquals(tl("125.00"), hesap.getBakiye());
        assertEquals(tl("75.00"), hedef.getBakiye());
    }

    @Test
    @DisplayName("Bakiye yetmezse transfer hiçbir hesabı değiştirmez")
    void basarisizTransferHicbirSeyiDegistirmez() {
        BankaHesabi hedef = new BankaHesabi("TR002", "Ayşe");
        hesap.paraYatir(tl("20"));

        assertThrows(YetersizBakiyeException.class, () -> hesap.transferEt(hedef, tl("50")));

        assertEquals(tl("20.00"), hesap.getBakiye());
        assertEquals(tl("0.00"), hedef.getBakiye());
    }

    @Test
    void hesapKendisineTransferYapamaz() {
        hesap.paraYatir(tl("10"));

        assertThrows(IllegalArgumentException.class, () -> hesap.transferEt(hesap, tl("5")));
    }

    @Test
    void hesapNumarasiVeSahipZorunludur() {
        assertThrows(IllegalArgumentException.class, () -> new BankaHesabi(" ", "Semih"));
        assertThrows(IllegalArgumentException.class, () -> new BankaHesabi("TR001", null));
    }

    private static BigDecimal tl(String tutar) {
        return new BigDecimal(tutar);
    }
}
