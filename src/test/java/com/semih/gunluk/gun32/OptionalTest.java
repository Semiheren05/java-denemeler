package com.semih.gunluk.gun32;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionalTest {

    private MusteriDeposu depo;
    private KargoAdresi kargo;

    @BeforeEach
    void hazirla() {
        depo = new MusteriDeposu()
                .ekle(new Musteri("M1", "Ayşe", new Adres("Ankara", "06100")))
                .ekle(new Musteri("M2", "Mehmet", null))
                .ekle(new Musteri("M3", "Zeynep", new Adres("  ", null)))
                .ekle(new Musteri("M4", "Can", new Adres(null, "34000")));
        kargo = new KargoAdresi(depo);
    }

    @Test
    void zincir_adresVarsaSehriVerir() {
        assertEquals("Ankara", kargo.sehir("M1"));
    }

    @Test
    void zincir_herBosAdimdaVarsayilanaDuser() {
        assertEquals(KargoAdresi.BILINMIYOR, kargo.sehir("YOK"));   // müşteri yok
        assertEquals(KargoAdresi.BILINMIYOR, kargo.sehir("M2"));    // adres yok
        assertEquals(KargoAdresi.BILINMIYOR, kargo.sehir("M3"));    // şehir boşluk (filter)
        assertEquals(KargoAdresi.BILINMIYOR, kargo.sehir("M4"));    // şehir null (map boş Optional verir)
    }

    @ParameterizedTest
    @CsvSource({"M1", "M2", "M3", "M4", "YOK"})
    void eskiVeYeniYazimAyniSonucuVerir(String id) {
        assertEquals(kargo.sehirEski(id), kargo.sehir(id));
    }

    @Test
    void orElse_doluOlsaBileVarsayilaniHesaplar_orElseGetHesaplamaz() {
        AtomicInteger uretilen = new AtomicInteger();
        Optional<Adres> dolu = Optional.of(new Adres("Izmir", "35000"));

        KargoAdresi.varsayilanla(dolu, () -> {
            uretilen.incrementAndGet();
            return new Adres("Merkez depo", "00000");
        }, false);
        KargoAdresi.varsayilanla(dolu, () -> {
            uretilen.incrementAndGet();
            return new Adres("Merkez depo", "00000");
        }, true);

        assertEquals(1, uretilen.get());   // sadece orElse çağrısı üretti; pahalı bir sorgu olsaydı boşa gidecekti
    }

    @Test
    void yanlisKullanimlar_get_ve_of() {
        Optional<Musteri> bos = depo.bul("YOK");

        assertThrows(NoSuchElementException.class, bos::get);                // kontrolsüz get
        assertThrows(NullPointerException.class, () -> Optional.of(null));   // null olabilecekse ofNullable
        assertTrue(Optional.ofNullable(null).isEmpty());
    }

    @Test
    void orElseThrow_anlamliHataVerir() {
        MusteriBulunamadi hata = assertThrows(MusteriBulunamadi.class, () -> kargo.zorunluBul("M99"));

        assertEquals("Müşteri bulunamadı: M99", hata.getMessage());
        assertEquals("Ayşe", kargo.zorunluBul("M1").ad());
    }

    @Test
    void optionalDondurenMetotNullDondururse_cagiranPatlar() {
        assertThrows(NullPointerException.class, () -> depo.hataliBul("YOK").isPresent());
    }

    @Test
    void or_yedekKaynagaSadeceGerekinceGider() {
        Musteri onbellekteki = new Musteri("M1", "Ayşe (önbellek)", null);

        Musteri sonuc = kargo.onbellektenYaDaDepodan(Optional.of(onbellekteki), "M1").orElseThrow();
        assertSame(onbellekteki, sonuc);
        assertEquals(0, depo.sorguSayisi());

        assertEquals("Mehmet", kargo.onbellektenYaDaDepodan(Optional.empty(), "M2").orElseThrow().ad());
        assertEquals(1, depo.sorguSayisi());
    }

    @Test
    void stream_bosOlanlariAtar() {
        assertEquals(List.of("Ayşe", "Can"), kargo.bulunanAdlar(List.of("M1", "YOK", "M4", "M99")));
    }

    @Test
    void optionalInt_veIfPresentOrElse() {
        OptionalInt enYuksek = KargoAdresi.enYuksekPuan(List.of(70, 95, 82));
        StringBuilder mesaj = new StringBuilder();

        KargoAdresi.enYuksekPuan(List.of()).ifPresentOrElse(
                p -> mesaj.append("en yüksek ").append(p),
                () -> mesaj.append("henüz puan yok"));

        assertEquals(95, enYuksek.getAsInt());
        assertFalse(KargoAdresi.enYuksekPuan(List.of()).isPresent());
        assertEquals("henüz puan yok", mesaj.toString());
    }
}
