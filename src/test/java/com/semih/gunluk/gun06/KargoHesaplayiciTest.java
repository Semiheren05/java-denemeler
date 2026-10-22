package com.semih.gunluk.gun06;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KargoHesaplayiciTest {

    private KargoHesaplayici hesaplayici;

    @BeforeEach
    void setUp() {
        hesaplayici = new KargoHesaplayici();
    }

    @Test
    void standartKargoTabanArtiKilogram() {
        BigDecimal ucret = KargoStratejileri.standart().hesapla(gonderi(2, 100, "50"));

        assertEquals(tl("40.00"), ucret);
    }

    @Test
    void hizliKargoKisaMesafede() {
        BigDecimal ucret = KargoStratejileri.hizli().hesapla(gonderi(1.5, 300, "50"));

        assertEquals(tl("62.00"), ucret);
    }

    @Test
    void hizliKargoUzakMesafedeEkUcretAlir() {
        BigDecimal ucret = KargoStratejileri.hizli().hesapla(gonderi(1.5, 900, "50"));

        assertEquals(tl("82.00"), ucret);
    }

    @Test
    void sabitUcretGonderidenBagimsizdir() {
        KargoUcretStratejisi sabit = KargoStratejileri.sabit(tl("45"));

        assertEquals(tl("45.00"), sabit.hesapla(gonderi(1, 10, "0")));
        assertEquals(tl("45.00"), sabit.hesapla(gonderi(20, 1000, "999")));
    }

    @Test
    @DisplayName("Sepet eşiği geçince kargo bedava, geçmezse asıl strateji")
    void ucretsizKargoEsigi() {
        KargoUcretStratejisi kampanya = KargoStratejileri.ucretsizEsigi(tl("500"), KargoStratejileri.standart());

        assertEquals(tl("0.00"), kampanya.hesapla(gonderi(2, 100, "500")));
        assertEquals(tl("40.00"), kampanya.hesapla(gonderi(2, 100, "499.99")));
    }

    @Test
    void stratejiLambdaIleDeYazilabilir() {
        KargoUcretStratejisi kmBasina = g -> BigDecimal.valueOf(g.mesafeKm()).multiply(tl("0.10"));

        assertEquals(tl("25.00"), kmBasina.hesapla(gonderi(1, 250, "0")));
    }

    @Test
    void kayitliFirmadanTeklifAlinir() {
        hesaplayici.firmaEkle("Standart", KargoStratejileri.standart());

        Teklif teklif = hesaplayici.teklif("Standart", gonderi(2, 100, "50"));

        assertEquals(new Teklif("Standart", tl("40.00")), teklif);
    }

    @Test
    void tekliflerUcuzdanPahaliyaSiralanir() {
        hesaplayici.firmaEkle("Hızlı", KargoStratejileri.hizli());
        hesaplayici.firmaEkle("Standart", KargoStratejileri.standart());
        hesaplayici.firmaEkle("Sabit", KargoStratejileri.sabit(tl("40")));

        List<Teklif> teklifler = hesaplayici.tumTeklifler(gonderi(2, 100, "50"));

        assertEquals(List.of(
                new Teklif("Sabit", tl("40.00")),
                new Teklif("Standart", tl("40.00")),
                new Teklif("Hızlı", tl("66.00"))
        ), teklifler);
    }

    @Test
    void enUcuzTeklif() {
        hesaplayici.firmaEkle("Hızlı", KargoStratejileri.hizli());
        hesaplayici.firmaEkle("Kampanya",
                KargoStratejileri.ucretsizEsigi(tl("300"), KargoStratejileri.standart()));

        assertEquals(Optional.of(new Teklif("Kampanya", tl("0.00"))),
                hesaplayici.enUcuz(gonderi(5, 100, "350")));
    }

    @Test
    void firmaYoksaEnUcuzBostur() {
        assertEquals(Optional.empty(), hesaplayici.enUcuz(gonderi(1, 10, "10")));
    }

    @Test
    void bilinmeyenFirmaHataVerir() {
        assertThrows(IllegalArgumentException.class, () -> hesaplayici.teklif("Yok", gonderi(1, 10, "10")));
    }

    @Test
    void ayniFirmaIkiKezEklenemez() {
        hesaplayici.firmaEkle("Standart", KargoStratejileri.standart());

        assertThrows(IllegalArgumentException.class,
                () -> hesaplayici.firmaEkle("Standart", KargoStratejileri.hizli()));
    }

    @Test
    void gecersizGonderiReddedilir() {
        assertThrows(IllegalArgumentException.class, () -> gonderi(0, 10, "10"));
        assertThrows(IllegalArgumentException.class, () -> gonderi(1, -5, "10"));
        assertThrows(IllegalArgumentException.class, () -> gonderi(1, 10, "-1"));
    }

    private static Gonderi gonderi(double kg, int km, String sepet) {
        return new Gonderi(kg, km, tl(sepet));
    }

    private static BigDecimal tl(String tutar) {
        return new BigDecimal(tutar);
    }
}
