package com.semih.gunluk.gun20;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KapsayiciTest {

    private Kapsayici kapsayici;

    @BeforeEach
    void setUp() {
        kapsayici = new Kapsayici().bagla(SiparisDeposu.class, BellekSiparisDeposu.class);
    }

    @Test
    void bagimliliklarZincirlemeKurulur() {
        SiparisServisi servis = kapsayici.getir(SiparisServisi.class);

        servis.siparisVer("Ayşe", "250.00");

        assertEquals(List.of("Ayşe, 1 numaralı siparişin alındı"),
                kapsayici.getir(BildirimServisi.class).gonderilenler());
        assertEquals(1, kapsayici.getir(SiparisDeposu.class).hepsi().size());
    }

    @Test
    void herTipBirKezKurulur() {
        SiparisServisi birinci = kapsayici.getir(SiparisServisi.class);
        SiparisServisi ikinci = kapsayici.getir(SiparisServisi.class);

        assertSame(birinci, ikinci);
        // Servis, depo, bildirim: üç nesne
        assertEquals(3, kapsayici.kurulanSayisi());
    }

    @Test
    void servisVeTestAyniDepoNesnesiniGorur() {
        kapsayici.getir(SiparisServisi.class).siparisVer("Ali", "99.90");

        SiparisDeposu depo = kapsayici.getir(SiparisDeposu.class);

        assertEquals("Ali", depo.hepsi().get(0).musteri());
    }

    @Test
    void bagliOlmayanArayuzAnlasilirHataVerir() {
        KapsayiciHatasi hata = assertThrows(KapsayiciHatasi.class,
                () -> new Kapsayici().getir(SiparisServisi.class));

        assertTrue(hata.getMessage().contains("SiparisDeposu için bağlama yok"), hata.getMessage());
    }

    @Test
    void donguselBagimlilikYoluylaRaporlanir() {
        KapsayiciHatasi hata = assertThrows(KapsayiciHatasi.class, () -> new Kapsayici().getir(Tavuk.class));

        assertEquals("Döngüsel bağımlılık: Tavuk → Yumurta → Tavuk", hata.getMessage());
    }

    @Test
    void birdenFazlaConstructordaEnjekteIsaretliOlanKullanilir() {
        IkiConstructorlu nesne = new Kapsayici().getir(IkiConstructorlu.class);

        assertEquals("enjekte", nesne.hangisi);
    }

    @Test
    void isaretsizBirdenFazlaConstructorReddedilir() {
        assertThrows(KapsayiciHatasi.class, () -> new Kapsayici().getir(Kararsiz.class));
    }

    @Test
    void elleKaydedilenNesneKullanilir() {
        Clock sabit = Clock.fixed(Instant.parse("2026-11-04T10:00:00Z"), ZoneOffset.UTC);
        kapsayici.kaydet(Clock.class, sabit);

        assertEquals(Instant.parse("2026-11-04T10:00:00Z"), kapsayici.getir(Saatli.class).simdi());
    }

    @Test
    void constructorHatasiNedeniyleBirlikteSarilir() {
        KapsayiciHatasi hata = assertThrows(KapsayiciHatasi.class, () -> new Kapsayici().getir(Bozuk.class));

        assertEquals("Bozuk kurulamadı: Ayar eksik", hata.getMessage());
        assertTrue(hata.getCause() instanceof IllegalStateException);
    }

    @Test
    void testteKapsayicisizElleDeKurulabilir() {
        // DI'ın asıl kazancı: servis kapsayıcıya bağlı değil, testte istenen parçalarla kurulabiliyor
        BildirimServisi bildirimler = new BildirimServisi();
        SiparisServisi servis = new SiparisServisi(new BellekSiparisDeposu(), bildirimler);

        servis.siparisVer("Deniz", "10");

        assertEquals(1, bildirimler.gonderilenler().size());
    }

    public static class Tavuk {
        public Tavuk(Yumurta yumurta) {
        }
    }

    public static class Yumurta {
        public Yumurta(Tavuk tavuk) {
        }
    }

    public static class IkiConstructorlu {
        final String hangisi;

        public IkiConstructorlu() {
            hangisi = "boş";
        }

        @Enjekte
        public IkiConstructorlu(BildirimServisi bildirimler) {
            hangisi = "enjekte";
        }
    }

    public static class Kararsiz {
        public Kararsiz() {
        }

        public Kararsiz(BildirimServisi bildirimler) {
        }
    }

    public static class Saatli {
        private final Clock saat;

        public Saatli(Clock saat) {
            this.saat = saat;
        }

        Instant simdi() {
            return saat.instant();
        }
    }

    public static class Bozuk {
        public Bozuk() {
            throw new IllegalStateException("Ayar eksik");
        }
    }
}
