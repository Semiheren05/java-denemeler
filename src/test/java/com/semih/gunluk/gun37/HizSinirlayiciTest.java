package com.semih.gunluk.gun37;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HizSinirlayiciTest {

    private static final Duration DAKIKA = Duration.ofMinutes(1);

    private final AyarlanabilirSaat saat = new AyarlanabilirSaat(Instant.parse("2026-11-13T10:00:00Z"));

    private static int izinSay(HizSinirlayici sinirlayici, String anahtar, int istek) {
        int izin = 0;
        for (int i = 0; i < istek; i++) {
            if (sinirlayici.izinVer(anahtar)) {
                izin++;
            }
        }
        return izin;
    }

    @Test
    void sabitPencere_limitKadarIzin() {
        SabitPencere sp = new SabitPencere(saat, 5, DAKIKA);

        assertEquals(5, izinSay(sp, "ip-1", 8));
        assertFalse(sp.izinVer("ip-1"));
    }

    @Test
    void sabitPencere_yeniDakikadaSifirlanir() {
        SabitPencere sp = new SabitPencere(saat, 5, DAKIKA);
        izinSay(sp, "ip-1", 5);

        saat.ilerlet(Duration.ofSeconds(59));
        assertFalse(sp.izinVer("ip-1"));
        saat.ilerlet(Duration.ofSeconds(1));
        assertTrue(sp.izinVer("ip-1"));
    }

    @Test
    void sabitPencere_sinirdaIkiKatiGecer() {
        SabitPencere sp = new SabitPencere(saat, 5, DAKIKA);

        saat.ilerlet(Duration.ofMillis(59_900));
        int sonAn = izinSay(sp, "ip-1", 5);         // 10:00:59.9
        saat.ilerlet(Duration.ofMillis(200));
        int ilkAn = izinSay(sp, "ip-1", 5);         // 10:01:00.1, yeni pencere

        assertEquals(10, sonAn + ilkAn);            // 200 ms içinde 10 istek: "dakikada 5" kuralı delindi
    }

    @Test
    void kayanPencere_ayniSenaryodaDelinmez() {
        KayanPencere kp = new KayanPencere(saat, 5, DAKIKA);

        saat.ilerlet(Duration.ofMillis(59_900));
        int sonAn = izinSay(kp, "ip-1", 5);
        saat.ilerlet(Duration.ofMillis(200));
        int ilkAn = izinSay(kp, "ip-1", 5);

        assertEquals(5, sonAn);
        assertEquals(0, ilkAn);
    }

    @Test
    void kayanPencere_eskiIsteklerDusunceYerAcilir() {
        KayanPencere kp = new KayanPencere(saat, 3, DAKIKA);
        kp.izinVer("ip-1");                          // 10:00:00
        saat.ilerlet(Duration.ofSeconds(30));
        kp.izinVer("ip-1");                          // 10:00:30
        kp.izinVer("ip-1");
        assertFalse(kp.izinVer("ip-1"));

        saat.ilerlet(Duration.ofSeconds(30));        // 10:01:00: ilk istek pencereden çıktı
        assertTrue(kp.izinVer("ip-1"));
        assertFalse(kp.izinVer("ip-1"));
    }

    @Test
    void jetonKovasi_basktaPatlamaSonraRed() {
        JetonKovasi kova = new JetonKovasi(saat, 10, 1.0);

        assertEquals(10, izinSay(kova, "ip-1", 15));
    }

    @Test
    void jetonKovasi_saniyedeDolarKesirliJetonYetmez() {
        JetonKovasi kova = new JetonKovasi(saat, 10, 2.0);   // saniyede 2 jeton
        izinSay(kova, "ip-1", 10);

        saat.ilerlet(Duration.ofMillis(250));                // yarım jeton
        assertFalse(kova.izinVer("ip-1"));
        saat.ilerlet(Duration.ofMillis(250));                // bir jeton
        assertTrue(kova.izinVer("ip-1"));
        saat.ilerlet(Duration.ofHours(1));                   // kova taşmaz: en fazla 10
        assertEquals(10, izinSay(kova, "ip-1", 20));
    }

    @Test
    void jetonKovasi_tekrarDenemeSuresi() {
        JetonKovasi kova = new JetonKovasi(saat, 2, 0.5);    // 2 saniyede bir jeton
        izinSay(kova, "ip-1", 2);

        assertEquals(2_000, kova.beklemeMs("ip-1"));
        saat.ilerlet(Duration.ofMillis(1_500));
        assertEquals(500, kova.beklemeMs("ip-1"));
        assertEquals(0, kova.beklemeMs("hic-gelmemis"));
    }

    @Test
    void anahtarlarBirbirindenBagimsiz() {
        JetonKovasi kova = new JetonKovasi(saat, 3, 1.0);

        assertEquals(3, izinSay(kova, "ip-1", 5));
        assertEquals(3, izinSay(kova, "ip-2", 5));
    }

    @Test
    void eszamanliIsteklerde_limitAsilmaz() throws Exception {
        JetonKovasi kova = new JetonKovasi(saat, 100, 1.0);
        KayanPencere kp = new KayanPencere(saat, 100, DAKIKA);
        AtomicInteger kovaIzin = new AtomicInteger();
        AtomicInteger pencereIzin = new AtomicInteger();

        try (ExecutorService havuz = Executors.newFixedThreadPool(16)) {
            for (int i = 0; i < 2_000; i++) {
                havuz.submit(() -> {
                    if (kova.izinVer("ip-1")) {
                        kovaIzin.incrementAndGet();
                    }
                    if (kp.izinVer("ip-1")) {
                        pencereIzin.incrementAndGet();
                    }
                });
            }
        }

        assertEquals(100, kovaIzin.get());      // saat durduğu için dolum yok: tam kapasite
        assertEquals(100, pencereIzin.get());
    }
}
