package com.semih.gunluk.gun29;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Stack;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KalitimVeBilesimTest {

    // --- Abstract class + şablon metot ---

    @Test
    void vadesizHesaptanUcretsizCekilir() {
        Hesap hesap = new VadesizHesap("TR-1");
        hesap.paraYatir(new BigDecimal("1000"));

        assertEquals(new BigDecimal("300"), hesap.paraCek(new BigDecimal("300")));
        assertEquals(new BigDecimal("700"), hesap.bakiye());
    }

    @Test
    void vadeliHesaptaCezaSablonMetotIcindeEklenir() {
        Hesap hesap = new VadeliHesap("TR-2", new BigDecimal("0.40"));
        hesap.paraYatir(new BigDecimal("1000"));

        assertEquals(new BigDecimal("303.00"), hesap.paraCek(new BigDecimal("300")));
        assertEquals(new BigDecimal("697.00"), hesap.bakiye());
    }

    @Test
    void bakiyeKontroluHerAltSinifIcinAyni() {
        // Kural üst sınıfta ve paraCek final: hiçbir alt sınıf kontrolü atlayamaz
        Hesap vadeli = new VadeliHesap("TR-3", new BigDecimal("0.40"));
        vadeli.paraYatir(new BigDecimal("100"));

        // 100 çekmek 1 TL ceza ile 101 eder: yetmez
        IllegalStateException hata = assertThrows(IllegalStateException.class,
                () -> vadeli.paraCek(new BigDecimal("100")));
        assertEquals("TR-3: yetersiz bakiye (100 var, 101.00 gerekli)", hata.getMessage());
    }

    @Test
    void cokBicimlilikAyniCagriFarkliDavranis() {
        List<Hesap> hesaplar = List.of(new VadesizHesap("A"), new VadeliHesap("B", new BigDecimal("0.40")));
        hesaplar.forEach(h -> h.paraYatir(new BigDecimal("500")));

        List<BigDecimal> dusenler = hesaplar.stream().map(h -> h.paraCek(new BigDecimal("100"))).toList();

        assertEquals(List.of(new BigDecimal("100"), new BigDecimal("101.00")), dusenler);
    }

    @Test
    void interfaceDefaultMetotOrtakHesabiVerir() {
        VadeliHesap hesap = new VadeliHesap("TR-4", new BigDecimal("0.48"));
        hesap.paraYatir(new BigDecimal("10000"));

        FaizGetiren faizli = hesap;   // aynı nesne, başka bir "yetenek" gözüyle
        assertEquals(new BigDecimal("400.00"), faizli.aylikFaiz());
    }

    // --- Kırılgan üst sınıf ---

    @Test
    void kalitimlaSayanKumeIkiKatSayar() {
        HataliSayanKume<String> kume = new HataliSayanKume<>();

        kume.addAll(List.of("Ayşe", "Ali", "Deniz"));

        // 3 bekleniyordu; HashSet.addAll içeride bizim add'imizi çağırdı
        assertEquals(6, kume.denemeSayisi());
    }

    @Test
    void bilesimleSayanKumeDogruSayar() {
        SayanKume<String> kume = new SayanKume<>();

        kume.hepsiniEkle(List.of("Ayşe", "Ali", "Deniz"));
        kume.ekle("Ali");   // zaten var: deneme sayılır, boyut artmaz

        assertEquals(4, kume.denemeSayisi());
        assertEquals(3, kume.boyut());
    }

    @Test
    void bilesimdeIcYapiDegistirilebilir() {
        SayanKume<String> sirali = new SayanKume<>(new TreeSet<>());

        sirali.hepsiniEkle(List.of("Deniz", "Ali"));

        assertTrue(sirali.iceriyorMu("Ali"));
        assertEquals(2, sirali.denemeSayisi());
    }

    // --- Liskov: JDK'nın kendi hatası ---

    @Test
    void stackVectordenTureyinceYiginKuraliDelinebilir() {
        // Stack "bir tür Vector" diye kalıtımla yazılmış; Vector'ün bütün metotları da gelmiş.
        // Yığına alttan, ortadan eleman sokulabiliyor: LIFO sözleşmesi korunamıyor.
        Stack<String> yigin = new Stack<>();
        yigin.push("birinci");
        yigin.push("ikinci");

        yigin.add(0, "araya-giren");   // Vector metodu

        assertEquals("ikinci", yigin.pop());
        assertEquals("birinci", yigin.pop());
        assertEquals("araya-giren", yigin.pop());
        // Bu yüzden yığın için ArrayDeque kullanılıyor (Gün 11): Deque'te böyle bir metot yok
    }

    @Test
    void gecersizTutarlarHerHesaptaReddedilir() {
        Hesap hesap = new VadesizHesap("TR-5");

        assertThrows(IllegalArgumentException.class, () -> hesap.paraYatir(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> hesap.paraCek(new BigDecimal("-5")));
    }
}
