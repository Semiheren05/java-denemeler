package com.semih.gunluk.gun23;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Gerçek bir sunucu açıp Java'nın kendi {@link HttpClient}'ıyla istek atarak test ediyor. */
class NotDefteriSunucusuTest {

    private NotDefteriSunucusu sunucu;
    private HttpClient istemci;

    @BeforeEach
    void setUp() throws Exception {
        sunucu = new NotDefteriSunucusu(0).baslat();
        istemci = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown() {
        sunucu.close();
    }

    @Test
    void notEklenince201VeAdresiDoner() throws Exception {
        HttpResponse<String> cevap = gonder("POST", "/notlar", "Spring Boot'a çalış");

        assertEquals(201, cevap.statusCode());
        assertEquals(Optional.of("/notlar/1"), cevap.headers().firstValue("Location"));
        assertEquals("{\"id\":1,\"metin\":\"Spring Boot'a çalış\"}", cevap.body());
        assertEquals(Optional.of("application/json; charset=utf-8"), cevap.headers().firstValue("Content-Type"));
    }

    @Test
    void listeEklemeSirasiylaGelir() throws Exception {
        gonder("POST", "/notlar", "Birinci");
        gonder("POST", "/notlar", "İkinci");

        HttpResponse<String> cevap = gonder("GET", "/notlar", null);

        assertEquals(200, cevap.statusCode());
        assertEquals("[{\"id\":1,\"metin\":\"Birinci\"},{\"id\":2,\"metin\":\"İkinci\"}]", cevap.body());
    }

    @Test
    void tekNotGetirilirOlmayanIcin404() throws Exception {
        gonder("POST", "/notlar", "Ekmek al");

        assertEquals(200, gonder("GET", "/notlar/1", null).statusCode());
        HttpResponse<String> yok = gonder("GET", "/notlar/99", null);
        assertEquals(404, yok.statusCode());
        assertEquals("{\"hata\":\"99 numaralı not yok\"}", yok.body());
    }

    @Test
    void silinenNotArtikBulunmaz() throws Exception {
        gonder("POST", "/notlar", "Geçici");

        HttpResponse<String> silme = gonder("DELETE", "/notlar/1", null);

        assertEquals(204, silme.statusCode());
        assertEquals("", silme.body());
        assertEquals(404, gonder("GET", "/notlar/1", null).statusCode());
        assertEquals(404, gonder("DELETE", "/notlar/1", null).statusCode());
    }

    @Test
    void bosVeCokUzunNotReddedilir() throws Exception {
        assertEquals(400, gonder("POST", "/notlar", "   ").statusCode());
        assertEquals(413, gonder("POST", "/notlar", "a".repeat(NotDefteriSunucusu.EN_UZUN_NOT + 1)).statusCode());
        assertEquals("[]", gonder("GET", "/notlar", null).body());
    }

    @Test
    void desteklenmeyenMetot405VeAllowBasligi() throws Exception {
        HttpResponse<String> cevap = gonder("PUT", "/notlar", "x");

        assertEquals(405, cevap.statusCode());
        assertEquals(Optional.of("GET, POST"), cevap.headers().firstValue("Allow"));
        assertEquals(Optional.of("GET, DELETE"), gonder("PATCH", "/notlar/1", "x").headers().firstValue("Allow"));
    }

    @Test
    void gecersizNumaraVeYabanciAdres() throws Exception {
        assertEquals(400, gonder("GET", "/notlar/abc", null).statusCode());
        assertEquals(404, gonder("GET", "/notlarim", null).statusCode());
        assertEquals(404, gonder("GET", "/baska", null).statusCode());   // hiçbir bağlama uymuyor
    }

    @Test
    void ozelKarakterlerJsonuBozmaz() throws Exception {
        HttpResponse<String> cevap = gonder("POST", "/notlar", "Ayşe \"tamam\" dedi\n\\ Şişli");

        assertEquals("{\"id\":1,\"metin\":\"Ayşe \\\"tamam\\\" dedi\\n\\\\ Şişli\"}", cevap.body());
    }

    @Test
    void ayniAndaGelenIstekleriNumaralarCakismadanKaydeder() throws Exception {
        // sendAsync: istemci de beklemeden 100 isteği birden gönderiyor
        List<CompletableFuture<HttpResponse<String>>> istekler = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> istemci.sendAsync(istek("POST", "/notlar", "Not " + i), HttpResponse.BodyHandlers.ofString()))
                .toList();

        Set<Long> numaralar = new HashSet<>();
        Pattern idDeseni = Pattern.compile("\"id\":(\\d+)");
        for (CompletableFuture<HttpResponse<String>> istek : istekler) {
            HttpResponse<String> cevap = istek.join();
            assertEquals(201, cevap.statusCode());
            Matcher m = idDeseni.matcher(cevap.body());
            assertTrue(m.find());
            numaralar.add(Long.parseLong(m.group(1)));
        }

        assertEquals(100, numaralar.size());
        assertTrue(numaralar.stream().allMatch(id -> id >= 1 && id <= 100));
    }

    private HttpResponse<String> gonder(String metot, String yol, String govde) throws Exception {
        return istemci.send(istek(metot, yol, govde), HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest istek(String metot, String yol, String govde) {
        HttpRequest.BodyPublisher yayinci = govde == null
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(govde);
        return HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + sunucu.port() + yol))
                .method(metot, yayinci)
                .build();
    }
}
