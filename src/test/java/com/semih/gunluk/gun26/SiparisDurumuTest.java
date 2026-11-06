package com.semih.gunluk.gun26;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.semih.gunluk.gun26.SiparisDurumu.HAZIRLANIYOR;
import static com.semih.gunluk.gun26.SiparisDurumu.IADE_EDILDI;
import static com.semih.gunluk.gun26.SiparisDurumu.IPTAL_EDILDI;
import static com.semih.gunluk.gun26.SiparisDurumu.KARGODA;
import static com.semih.gunluk.gun26.SiparisDurumu.ODENDI;
import static com.semih.gunluk.gun26.SiparisDurumu.OLUSTURULDU;
import static com.semih.gunluk.gun26.SiparisDurumu.TESLIM_EDILDI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SiparisDurumuTest {

    private static final Instant BASLANGIC = Instant.parse("2026-11-07T09:00:00Z");

    private AyarlanabilirSaat saat;
    private Siparis siparis;

    @BeforeEach
    void setUp() {
        saat = new AyarlanabilirSaat(BASLANGIC);
        siparis = new Siparis("SE-1001", saat);
    }

    @Test
    void siparisMutluYoluBastanSonaIzler() {
        siparis.gec(ODENDI, "Kartla ödendi");
        siparis.gec(HAZIRLANIYOR, "Depoya iletildi");
        siparis.gec(KARGODA, "Takip no: 123456");
        siparis.gec(TESLIM_EDILDI, "Kapıya bırakıldı");

        assertEquals(TESLIM_EDILDI, siparis.durum());
        assertEquals(5, siparis.gecmis().size());
    }

    @Test
    void izinsizGecisAnlasilirHataVerir() {
        GecersizGecisHatasi hata = assertThrows(GecersizGecisHatasi.class,
                () -> siparis.gec(KARGODA, "Atlayalım"));

        assertEquals("SE-1001: Sipariş alındı → Kargoya verildi olmaz. Buradan gidilebilecekler: [ODENDI, IPTAL_EDILDI]",
                hata.getMessage());
        assertEquals(OLUSTURULDU, siparis.durum());
        assertEquals(1, siparis.gecmis().size());
    }

    @Test
    void kargodakiSiparisIptalEdilemez() {
        siparis.gec(ODENDI, "");
        siparis.iptalEt("Vazgeçtim");
        assertEquals(IPTAL_EDILDI, siparis.durum());

        Siparis kargoda = yolaCikmis();
        assertThrows(GecersizGecisHatasi.class, () -> kargoda.iptalEt("Vazgeçtim"));
    }

    @Test
    void sonDurumlardanCikisYok() {
        for (SiparisDurumu durum : EnumSet.of(IADE_EDILDI, IPTAL_EDILDI)) {
            assertTrue(durum.sonMu(), durum.name());
        }
        assertFalse(TESLIM_EDILDI.sonMu());   // buradan hâlâ iade var
    }

    @Test
    void iadeTeslimdenSonra14GunIcinde() {
        Siparis teslim = yolaCikmis();
        teslim.gec(TESLIM_EDILDI, "");

        saat.ilerlet(Duration.ofDays(14));
        teslim.gec(IADE_EDILDI, "Beden olmadı");

        assertEquals(IADE_EDILDI, teslim.durum());
    }

    @Test
    void iadeSuresiDolunca() {
        Siparis teslim = yolaCikmis();
        teslim.gec(TESLIM_EDILDI, "");

        saat.ilerlet(Duration.ofDays(14).plusMinutes(1));

        GecersizGecisHatasi hata = assertThrows(GecersizGecisHatasi.class, () -> teslim.gec(IADE_EDILDI, ""));
        assertEquals("SE-2002: iade süresi (14 gün) doldu", hata.getMessage());
    }

    @Test
    void gecmisZamanVeAciklamaTutar() {
        saat.ilerlet(Duration.ofMinutes(5));
        siparis.gec(ODENDI, "Kartla ödendi");

        DurumDegisimi son = siparis.gecmis().get(1);
        assertEquals(new DurumDegisimi(OLUSTURULDU, ODENDI, BASLANGIC.plus(Duration.ofMinutes(5)), "Kartla ödendi"), son);
        assertEquals(BASLANGIC, siparis.durumaGirisZamani(OLUSTURULDU).orElseThrow());
    }

    @Test
    void herDurumaBaslangictanUlasilabilir() {
        // Durum makinesinde "ölü" durum olmamalı: grafı BFS ile geziyorum (Gün 21)
        Set<SiparisDurumu> ulasilan = EnumSet.of(OLUSTURULDU);
        Deque<SiparisDurumu> kuyruk = new ArrayDeque<>(List.of(OLUSTURULDU));
        while (!kuyruk.isEmpty()) {
            for (SiparisDurumu sonraki : kuyruk.poll().sonrakiler()) {
                if (ulasilan.add(sonraki)) {
                    kuyruk.add(sonraki);
                }
            }
        }

        assertEquals(EnumSet.allOf(SiparisDurumu.class), ulasilan);
    }

    @Test
    void sabiteOzelGovdeFarkliMesajVerir() {
        assertEquals("SE-1001 numaralı siparişin durumu: Kargoya verildi", KARGODA.musteriMesaji("SE-1001"));
        assertTrue(IPTAL_EDILDI.musteriMesaji("SE-1001").contains("iaden 3 iş günü"));
        // Gövdeli sabit kendi anonim alt sınıfı: getClass() farklı, getDeclaringClass() aynı
        assertTrue(IPTAL_EDILDI.getClass() != SiparisDurumu.class);
        assertEquals(SiparisDurumu.class, IPTAL_EDILDI.getDeclaringClass());
    }

    @Test
    void panelHerDurumuSayarVeAciklariAyirir() {
        Siparis odenmis = new Siparis("SE-3", saat);
        odenmis.gec(ODENDI, "");
        Map<SiparisDurumu, Integer> sayilar = SiparisPaneli.durumSayilari(List.of(siparis, odenmis, yolaCikmis()));

        assertEquals(List.of(OLUSTURULDU, ODENDI, HAZIRLANIYOR, KARGODA, TESLIM_EDILDI, IADE_EDILDI, IPTAL_EDILDI),
                List.copyOf(sayilar.keySet()));   // EnumMap tanım sırasıyla
        assertEquals(List.of(1, 1, 0, 1, 0, 0, 0), List.copyOf(sayilar.values()));
        assertEquals(EnumSet.of(OLUSTURULDU, ODENDI, HAZIRLANIYOR, KARGODA), SiparisPaneli.acikDurumlar());
    }

    private Siparis yolaCikmis() {
        Siparis yeni = new Siparis("SE-2002", saat);
        yeni.gec(ODENDI, "");
        yeni.gec(HAZIRLANIYOR, "");
        yeni.gec(KARGODA, "");
        return yeni;
    }
}
