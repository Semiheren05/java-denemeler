package com.semih.gunluk.gun25;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonAyristiriciTest {

    @Test
    void temelDegerler() {
        assertEquals(new JsonDegeri.Mantiksal(true), JsonAyristirici.ayristir("true"));
        assertEquals(JsonDegeri.Bos.NULL, JsonAyristirici.ayristir(" null "));
        assertEquals(new JsonDegeri.Metin("merhaba"), JsonAyristirici.ayristir("\"merhaba\""));
        assertEquals(new JsonDegeri.Sayi(new BigDecimal("-0.5e3")), JsonAyristirici.ayristir("-0.5e3"));
        assertEquals(new JsonDegeri.Dizi(List.of()), JsonAyristirici.ayristir("[ ]"));
    }

    @Test
    void icIceYapiKendiligindenCozulur() {
        JsonDegeri kok = JsonAyristirici.ayristir("""
                {
                  "ad": "Ayşe",
                  "yas": 27,
                  "adres": { "sehir": "İzmir", "postaKodu": "35210" },
                  "diller": ["Java", "SQL"],
                  "aktif": true,
                  "yonetici": null
                }
                """);

        JsonDegeri.Nesne kullanici = (JsonDegeri.Nesne) kok;
        // Record pattern ile iç içe alana inmek
        String sehir = switch (kullanici.alan("adres").orElseThrow()) {
            case JsonDegeri.Nesne adres when adres.alan("sehir").orElseThrow() instanceof JsonDegeri.Metin(String s) -> s;
            default -> "?";
        };
        assertEquals("İzmir", sehir);
        assertEquals(new JsonDegeri.Dizi(List.of(new JsonDegeri.Metin("Java"), new JsonDegeri.Metin("SQL"))),
                kullanici.alan("diller").orElseThrow());
        assertEquals(List.of("ad", "yas", "adres", "diller", "aktif", "yonetici"),
                List.copyOf(kullanici.alanlar().keySet()));   // yazıldığı sırayla
    }

    @Test
    void kacisDizileriCozulur() {
        JsonDegeri deger = JsonAyristirici.ayristir("\"Ayşe \\\"tamam\\\" dedi\\n\\u015fi\\u015fli \\\\ \\/\"");

        assertEquals(new JsonDegeri.Metin("Ayşe \"tamam\" dedi\nşişli \\ /"), deger);
    }

    @Test
    void sayilarHassasiyetiniKorur() {
        // double olsaydı ikincisi 12345678901234567000 olurdu
        assertEquals("0.1", JsonYazici.yaz(JsonAyristirici.ayristir("0.1")));
        assertEquals("12345678901234567890", JsonYazici.yaz(JsonAyristirici.ayristir("12345678901234567890")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"01", "+1", "1.", ".5", "-", "1.e5", "--1", "0x1F"})
    void jsonaUymayanSayilarReddedilir(String girdi) {
        assertThrows(JsonHatasi.class, () -> JsonAyristirici.ayristir(girdi));
    }

    @Test
    void hataSatirVeSutunuyleBildirilir() {
        JsonHatasi hata = assertThrows(JsonHatasi.class, () -> JsonAyristirici.ayristir("""
                {
                  "ad": "Ayşe",
                  "yas": 27
                  "sehir": "İzmir"
                }"""));

        assertEquals("4. satır, 3. sütun: ',' ya da '}' bekleniyordu", hata.getMessage());
        assertEquals(4, hata.satir());
    }

    @ParameterizedTest
    @ValueSource(strings = {"[1, 2,]", "{\"a\": 1,}", "{\"a\" 1}", "{a: 1}", "[1 2]", "\"kapanmayan", "tru", "{} x", ""})
    void bozukJsonlarReddedilir(String girdi) {
        assertThrows(JsonHatasi.class, () -> JsonAyristirici.ayristir(girdi));
    }

    @Test
    void ayniAlanIkiKezYazilamaz() {
        JsonHatasi hata = assertThrows(JsonHatasi.class, () -> JsonAyristirici.ayristir("{\"a\": 1, \"a\": 2}"));

        assertEquals("1. satır, 10. sütun: Aynı alan iki kez yazılmış: \"a\"", hata.getMessage());
    }

    @Test
    void metinIcindeKacissizSatirSonuOlamaz() {
        assertThrows(JsonHatasi.class, () -> JsonAyristirici.ayristir("\"iki\nsatır\""));
    }

    @Test
    void cokDerinIcIceGecmeReddedilir() {
        int sinir = JsonAyristirici.EN_FAZLA_DERINLIK;

        JsonAyristirici.ayristir("[".repeat(sinir) + "]".repeat(sinir));
        assertThrows(JsonHatasi.class, () -> JsonAyristirici.ayristir("[".repeat(sinir + 1) + "]".repeat(sinir + 1)));
        // Sınır olmasaydı bu, StackOverflowError ile bütün uygulamayı düşürebilirdi
        assertThrows(JsonHatasi.class, () -> JsonAyristirici.ayristir("[".repeat(100_000)));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"ad\":\"Ayşe\",\"yas\":27,\"etiketler\":[\"a\",\"b\"],\"bos\":null,\"oran\":-0.25}",
            "[[],{},[{\"x\":[1,2,3]}]]",
            "\"satır\\nsonu ve \\\"tırnak\\\"\""
    })
    void yaziciAyristiricininTersi(String json) {
        assertEquals(json, JsonYazici.yaz(JsonAyristirici.ayristir(json)));
    }
}
