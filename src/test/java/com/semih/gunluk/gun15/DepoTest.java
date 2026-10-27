package com.semih.gunluk.gun15;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DepoTest {

    private OlayYayini<StokOlayi> olaylar;
    private Depo depo;
    private List<StokOlayi> hepsi;

    @BeforeEach
    void setUp() {
        olaylar = new OlayYayini<>();
        depo = new Depo(olaylar, 5);
        hepsi = new ArrayList<>();
        olaylar.aboneOl(StokOlayi.class, hepsi::add);
        depo.ekle("Klavye", 10);
    }

    @Test
    void kritikSeviyeninAltinaInilinceBirKezHaberVerilir() {
        List<StokOlayi.StokAzaldi> uyarilar = new ArrayList<>();
        olaylar.aboneOl(StokOlayi.StokAzaldi.class, uyarilar::add);

        depo.sat("Klavye", 4);   // 10 → 6: hâlâ eşiğin üstünde
        depo.sat("Klavye", 2);   // 6 → 4: eşiğin altına indi
        depo.sat("Klavye", 1);   // 4 → 3: zaten altındaydı, tekrar haber yok

        assertEquals(List.of(new StokOlayi.StokAzaldi("Klavye", 4, 5)), uyarilar);
    }

    @Test
    void stokBitinceBitmeOlayiGelir() {
        List<String> bitenler = new ArrayList<>();
        olaylar.aboneOl(StokOlayi.StokBitti.class, olay -> bitenler.add(olay.urun()));

        depo.sat("Klavye", 10);

        assertEquals(List.of("Klavye"), bitenler);
        assertEquals(0, depo.stok("Klavye"));
    }

    @Test
    void ustTipeAboneOlanHerOlayiAlir() {
        depo.sat("Klavye", 6);
        depo.sat("Klavye", 4);

        assertEquals(List.of(
                new StokOlayi.StokEklendi("Klavye", 10, 10),
                new StokOlayi.StokAzaldi("Klavye", 4, 5),
                new StokOlayi.StokBitti("Klavye")), hepsi);
    }

    @Test
    void dinleyicilerAboneOlduklariSiraylaCagrilir() {
        List<String> sira = new ArrayList<>();
        olaylar.aboneOl(StokOlayi.StokEklendi.class, olay -> sira.add("panel"));
        olaylar.aboneOl(StokOlayi.StokEklendi.class, olay -> sira.add("e-posta"));

        depo.ekle("Mouse", 3);

        assertEquals(List.of("panel", "e-posta"), sira);
    }

    @Test
    void aboneligiKapatanArtikOlayAlmaz() {
        List<StokOlayi> gecici = new ArrayList<>();
        try (Abonelik abonelik = olaylar.aboneOl(StokOlayi.class, gecici::add)) {
            depo.ekle("Mouse", 1);
        }

        depo.ekle("Mouse", 1);

        assertEquals(1, gecici.size());
        assertEquals(1, olaylar.aboneSayisi());
    }

    @Test
    void hataVerenDinleyiciSatisiVeDigerDinleyicileriBozmaz() {
        List<StokOlayi> ikinci = new ArrayList<>();
        olaylar.aboneOl(StokOlayi.StokBitti.class, olay -> {
            throw new IllegalStateException("E-posta sunucusu kapalı");
        });
        olaylar.aboneOl(StokOlayi.StokBitti.class, ikinci::add);

        List<RuntimeException> hatalar = depo.sat("Klavye", 10);

        assertEquals(0, depo.stok("Klavye"));
        assertEquals(1, ikinci.size());
        assertEquals(1, hatalar.size());
        assertEquals("E-posta sunucusu kapalı", hatalar.get(0).getMessage());
    }

    @Test
    void olaySirasindaAbonelikAcmakHataVermez() {
        // Düz ArrayList'te ConcurrentModificationException olurdu
        olaylar.aboneOl(StokOlayi.StokEklendi.class, olay -> olaylar.aboneOl(StokOlayi.class, o -> { }));

        List<RuntimeException> hatalar = olaylar.yayinla(new StokOlayi.StokEklendi("Mouse", 1, 1));

        assertTrue(hatalar.isEmpty());
        assertEquals(3, olaylar.aboneSayisi());
    }

    @Test
    void dinleyiciKendiAboneliginiKapatabilir() {
        List<StokOlayi> tekSeferlik = new ArrayList<>();
        Abonelik[] abonelik = new Abonelik[1];
        abonelik[0] = olaylar.aboneOl(StokOlayi.class, olay -> {
            tekSeferlik.add(olay);
            abonelik[0].close();
        });

        depo.ekle("Mouse", 1);
        depo.ekle("Mouse", 1);

        assertEquals(1, tekSeferlik.size());
    }

    @Test
    void yetersizStoktaHicbirSeyDegismezOlayYayinlanmaz() {
        int oncekiOlaySayisi = hepsi.size();

        assertThrows(IllegalStateException.class, () -> depo.sat("Klavye", 11));
        assertThrows(IllegalStateException.class, () -> depo.sat("Olmayan", 1));

        assertEquals(10, depo.stok("Klavye"));
        assertEquals(oncekiOlaySayisi, hepsi.size());
    }

    @Test
    void gecersizAdetlerKabulEdilmez() {
        assertThrows(IllegalArgumentException.class, () -> depo.ekle("Klavye", 0));
        assertThrows(IllegalArgumentException.class, () -> depo.sat("Klavye", -1));
        assertThrows(IllegalArgumentException.class, () -> new Depo(olaylar, 0));
    }
}
