package com.semih.gunluk.gun16;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HavaDurumuServisiTest {

    private AyarlanabilirSaat saat;
    private List<Integer> sorulanPlakalar;

    @BeforeEach
    void setUp() {
        saat = new AyarlanabilirSaat(Instant.parse("2026-10-28T09:00:00Z"));
        sorulanPlakalar = new ArrayList<>();
    }

    /** Her çağrıyı kaydeden sahte yabancı API. */
    private YabanciHavaApi sahteApi(String cevap) {
        return plaka -> {
            sorulanPlakalar.add(plaka);
            return cevap;
        };
    }

    @ParameterizedTest
    @CsvSource({
            "32F,   0.0",
            "212F,  100.0",
            "72.5F, 22.5",
            "-4F,   -20.0"
    })
    void adaptorFahrenheitiSantigradaCevirir(String cevap, double beklenen) {
        HavaDurumuServisi servis = new YabanciApiAdaptoru(sahteApi(cevap));

        assertEquals(beklenen, servis.sicaklik("İstanbul"));
        assertEquals(List.of(34), sorulanPlakalar);
    }

    @Test
    void adaptorSehirAdiniTurkceKurallarlaKucultur() {
        HavaDurumuServisi servis = new YabanciApiAdaptoru(sahteApi("50F"));

        servis.sicaklik("İZMİR");
        servis.sicaklik(" ankara ");

        assertEquals(List.of(35, 6), sorulanPlakalar);
    }

    @Test
    void adaptorBilinmeyenSehirVeBozukCevabiReddeder() {
        assertThrows(IllegalArgumentException.class, () -> new YabanciApiAdaptoru(sahteApi("50F")).sicaklik("Paris"));
        assertThrows(IllegalStateException.class, () -> new YabanciApiAdaptoru(sahteApi("50C")).sicaklik("Ankara"));
    }

    @Test
    void onbellekSuresiDolmadanIcServiseGidilmez() {
        HavaDurumuServisi servis = new OnbellekliServis(
                new YabanciApiAdaptoru(sahteApi("68F")), Duration.ofMinutes(10), saat);

        servis.sicaklik("Ankara");
        saat.ilerlet(Duration.ofMinutes(9));
        servis.sicaklik("Ankara");
        assertEquals(1, sorulanPlakalar.size());

        saat.ilerlet(Duration.ofMinutes(1));   // tam 10. dakika: artık geçersiz
        assertEquals(20.0, servis.sicaklik("Ankara"));
        assertEquals(2, sorulanPlakalar.size());
    }

    @Test
    void onbellekSehirBazinda() {
        HavaDurumuServisi servis = new OnbellekliServis(
                new YabanciApiAdaptoru(sahteApi("68F")), Duration.ofMinutes(10), saat);

        servis.sicaklik("Ankara");
        servis.sicaklik("İzmir");
        servis.sicaklik("Ankara");

        assertEquals(List.of(6, 35), sorulanPlakalar);
    }

    @Test
    void tekrarDeneyenIkiHatadanSonraBasarir() {
        int[] cagri = {0};
        HavaDurumuServisi bazenBozuk = sehir -> {
            if (++cagri[0] < 3) {
                throw new IllegalStateException("Zaman aşımı " + cagri[0]);
            }
            return 18.5;
        };

        assertEquals(18.5, new TekrarDeneyenServis(bazenBozuk, 3).sicaklik("Ankara"));
        assertEquals(3, cagri[0]);
    }

    @Test
    void hepsiBasarisizsaSonHataOncekilerleBirlikteFirlatilir() {
        int[] cagri = {0};
        HavaDurumuServisi hepBozuk = sehir -> {
            throw new IllegalStateException("Zaman aşımı " + ++cagri[0]);
        };

        IllegalStateException hata = assertThrows(IllegalStateException.class,
                () -> new TekrarDeneyenServis(hepBozuk, 3).sicaklik("Ankara"));

        assertEquals("Zaman aşımı 3", hata.getMessage());
        assertEquals("Zaman aşımı 2", hata.getSuppressed()[0].getMessage());
        assertEquals("Zaman aşımı 1", hata.getSuppressed()[0].getSuppressed()[0].getMessage());
    }

    @Test
    void bilinmeyenSehirTekrarDenenmez() {
        int[] cagri = {0};
        HavaDurumuServisi servis = sehir -> {
            cagri[0]++;
            throw new IllegalArgumentException("Bilinmeyen şehir: " + sehir);
        };

        assertThrows(IllegalArgumentException.class, () -> new TekrarDeneyenServis(servis, 5).sicaklik("Paris"));
        assertEquals(1, cagri[0]);
    }

    @Test
    void fabrikaZinciriDogruSirayaKurar() {
        int[] cagri = {0};
        YabanciHavaApi ilkSeferBozuk = plaka -> {
            if (++cagri[0] == 1) {
                throw new IllegalStateException("Bağlantı koptu");
            }
            return "77F";
        };
        HavaDurumuServisi servis = HavaDurumuServisleri.uretim(ilkSeferBozuk, saat);

        assertEquals(25.0, servis.sicaklik("Antalya"));   // ilk deneme koptu, ikincisi geldi
        assertEquals(25.0, servis.sicaklik("Antalya"));   // önbellekten
        assertEquals(2, cagri[0]);
    }

    @Test
    void denemeSayisiEnAzBir() {
        assertThrows(IllegalArgumentException.class, () -> new TekrarDeneyenServis(sehir -> 0, 0));
    }
}
