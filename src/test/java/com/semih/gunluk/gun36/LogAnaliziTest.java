package com.semih.gunluk.gun36;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogAnaliziTest {

    @TempDir
    Path klasor;

    private Path dosya(List<String> satirlar) throws Exception {
        return Files.write(klasor.resolve("erisim.log"), satirlar);
    }

    private static final List<String> ORNEK = List.of(
            "2026-11-12T09:58:00.100Z INFO  [http-1] GET /api/posts 200 20ms",
            "2026-11-12T10:01:00.000Z INFO  [http-2] GET /api/posts/12 200 40ms",
            "2026-11-12T10:05:00.000Z ERROR [http-3] POST /api/contact 503 1200ms",
            "java.net.SocketTimeoutException: Read timed out",
            "\tat java.base/sun.nio.ch.NioSocketImpl.timedRead(NioSocketImpl.java:288)",
            "2026-11-12T10:30:00.000Z INFO  [http-1] GET /api/posts/7?sayfa=2 200 60ms",
            "2026-11-12T10:45:00.000Z WARN  [http-2] GET /api/posts/99 404 5ms",
            "2026-11-12T11:10:00.000Z ERROR [http-4] POST /api/contact 500 900ms",
            "",
            "2026-11-12T11:20:00.000Z ERROR [http-4] GET /api/posts/3 502 300ms");

    @Test
    void gecerliSatirAyristirilir() {
        LogKaydi k = LogAyristirici.ayristir("2026-11-12T10:15:02.481Z INFO  [http-1] GET /api/posts/12?sayfa=2 200 35ms")
                .orElseThrow();

        assertEquals(Instant.parse("2026-11-12T10:15:02.481Z"), k.zaman());
        assertEquals("GET /api/posts/{id}", k.ucNokta());
        assertEquals(200, k.durum());
        assertEquals(35, k.sureMs());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "java.lang.NullPointerException",
            "2026-11-12T10:15:02Z INFO [http-1] FETCH /api 200 3ms",
            "2026-13-45T99:00:00Z INFO [http-1] GET /api 200 3ms",
            "2026-11-12T10:15:02Z INFO [http-1] GET /api 200 3 ms"}, delimiter = '|')
    void bozukSatirBosDoner(String satir) {
        assertTrue(LogAyristirici.ayristir(satir).isEmpty());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "/api/posts/12|/api/posts/{id}",
            "/api/users/5/posts/77|/api/users/{id}/posts/{id}",
            "/api/posts/12?sayfa=2&boyut=10|/api/posts/{id}",
            "/api/v2/posts|/api/v2/posts",
            "/api/posts/abc12|/api/posts/abc12"}, delimiter = '|')
    void yolNormallestirme(String ham, String beklenen) {
        assertEquals(beklenen, LogAyristirici.normallestir(ham));
    }

    @Test
    void yuzdelik_enYakinSira() {
        List<Long> sureler = new ArrayList<>();
        for (long i = 1; i <= 100; i++) {
            sureler.add(i * 10);   // 10, 20, ..., 1000 ms
        }

        assertEquals(950, LogAnalizi.yuzdelik(sureler, 95));
        assertEquals(500, LogAnalizi.yuzdelik(sureler, 50));
        assertEquals(1000, LogAnalizi.yuzdelik(sureler, 100));
        assertEquals(7, LogAnalizi.yuzdelik(List.of(7L), 95));
    }

    @Test
    void rapor_toplamBozukOrtalama() throws Exception {
        Rapor r = LogAnalizi.analizEt(dosya(ORNEK), 3);

        assertEquals(7, r.kayit());
        assertEquals(3, r.bozukSatir());   // yığın izinin iki satırı ve boş satır
        assertEquals(1200, r.enUzunMs());
        assertEquals((20 + 40 + 1200 + 60 + 5 + 900 + 300) / 7.0, r.ortalamaMs(), 0.0001);
    }

    @Test
    void rapor_saatlikSunucuHatalari() throws Exception {
        Rapor r = LogAnalizi.analizEt(dosya(ORNEK), 3);

        assertEquals(Map.of(
                LocalDateTime.of(2026, 11, 12, 10, 0), 1L,
                LocalDateTime.of(2026, 11, 12, 11, 0), 2L), r.saatlikHatalar());
        assertEquals(LocalDateTime.of(2026, 11, 12, 10, 0), r.saatlikHatalar().firstKey());
    }

    @Test
    void rapor_enYavasUcNoktalarVeDurumSiniflari() throws Exception {
        Rapor r = LogAnalizi.analizEt(dosya(ORNEK), 2);

        assertEquals(List.of(
                Map.entry("POST /api/contact", 1200L),
                Map.entry("GET /api/posts/{id}", 300L)), r.enYavaslar());
        assertEquals(Map.of("2xx", 3L, "4xx", 1L, "5xx", 3L), r.durumSiniflari());
    }

    @Test
    void tembelOkuma_ilkHatadaDurur() throws Exception {
        List<String> satirlar = new ArrayList<>(ORNEK.subList(0, 3));   // üçüncü satır 503
        for (int i = 0; i < 50_000; i++) {
            satirlar.add("2026-11-12T12:00:00.000Z INFO  [http-1] GET /api/posts 200 10ms");
        }
        AtomicLong okunan = new AtomicLong();

        LogKaydi ilk = LogAnalizi.ilkSunucuHatasi(dosya(satirlar), okunan).orElseThrow();

        assertEquals(503, ilk.durum());
        assertEquals(3, okunan.get());
    }

    @Test
    void buyukDosya_satirSatirIslenir() throws Exception {
        Path buyuk = klasor.resolve("buyuk.log");
        try (var yazici = Files.newBufferedWriter(buyuk)) {
            for (int i = 0; i < 200_000; i++) {
                int durum = i % 100 == 0 ? 500 : 200;
                yazici.write("2026-11-12T" + String.format("%02d", i % 24) + ":00:00.000Z INFO  [http-1] GET /api/posts/"
                        + i + " " + durum + " " + (i % 50) + "ms");
                yazici.newLine();
            }
        }

        Rapor r = LogAnalizi.analizEt(buyuk, 1);

        assertEquals(200_000, r.kayit());
        assertEquals(2_000, r.durumSiniflari().get("5xx"));
        assertEquals(List.of(Map.entry("GET /api/posts/{id}", 47L)), r.enYavaslar());   // 200.000 farklı yol tek uç nokta oldu
    }

    @Test
    void olmayanDosya_anlamliHata() {
        LogOkumaHatasi hata = assertThrows(LogOkumaHatasi.class,
                () -> LogAnalizi.analizEt(klasor.resolve("yok.log"), 3));

        assertEquals("Log okunamadı: yok.log", hata.getMessage());
        assertInstanceOf(NoSuchFileException.class, hata.getCause());
    }
}
