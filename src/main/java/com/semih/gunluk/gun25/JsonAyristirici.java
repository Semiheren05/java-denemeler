package com.semih.gunluk.gun25;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Kütüphanesiz bir JSON ayrıştırıcı. Jackson'ın {@code readTree}'sinin küçük bir benzeri.
 *
 * <p><b>Özyinelemeli iniş (recursive descent)</b>: JSON'un dilbilgisindeki her kural için bir
 * metot var. {@code deger()} sıradaki karaktere bakıp hangi kuralın geldiğine karar veriyor:
 * <pre>
 * deger  = nesne | dizi | metin | sayi | "true" | "false" | "null"
 * nesne  = "{" [ metin ":" deger { "," metin ":" deger } ] "}"
 * dizi   = "[" [ deger { "," deger } ] "]"
 * </pre>
 * Nesnenin içinde yine {@code deger()} çağrılıyor; iç içe yapılar böylece kendiliğinden çözülüyor.
 *
 * <p>Her çağrı için yeni bir nesne: okuma konumu ({@code i}) nesnenin durumu, paylaşılmamalı.
 */
public final class JsonAyristirici {

    /** {@code [[[[...]]]]} gibi kötü niyetli bir girdi yığını taşırmasın diye. */
    static final int EN_FAZLA_DERINLIK = 200;

    // JSON'un sayı kuralı: başta sıfırsa tek sıfır, '+' yok, '.'dan sonra en az bir rakam
    private static final Pattern SAYI = Pattern.compile("-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?");

    private final String metin;
    private int i;
    private int derinlik;

    private JsonAyristirici(String metin) {
        this.metin = metin;
    }

    public static JsonDegeri ayristir(String json) {
        if (json == null) {
            throw new IllegalArgumentException("JSON boş olamaz");
        }
        JsonAyristirici ayristirici = new JsonAyristirici(json);
        ayristirici.bosluklariAtla();
        JsonDegeri deger = ayristirici.deger();
        ayristirici.bosluklariAtla();
        if (ayristirici.i < json.length()) {
            throw ayristirici.hata("Değerden sonra fazladan karakter: '" + json.charAt(ayristirici.i) + "'");
        }
        return deger;
    }

    private JsonDegeri deger() {
        if (i >= metin.length()) {
            throw hata("Değer bekleniyordu ama metin bitti");
        }
        char c = metin.charAt(i);
        return switch (c) {
            case '{' -> nesne();
            case '[' -> dizi();
            case '"' -> new JsonDegeri.Metin(metin());
            case 't' -> sabit("true", new JsonDegeri.Mantiksal(true));
            case 'f' -> sabit("false", new JsonDegeri.Mantiksal(false));
            case 'n' -> sabit("null", JsonDegeri.Bos.NULL);
            default -> {
                if (c == '-' || Character.isDigit(c)) {
                    yield sayi();
                }
                throw hata("Beklenmeyen karakter: '" + c + "'");
            }
        };
    }

    private JsonDegeri.Nesne nesne() {
        icineGir();
        i++;   // '{'
        Map<String, JsonDegeri> alanlar = new LinkedHashMap<>();
        bosluklariAtla();
        if (simdiki() == '}') {
            i++;
            cik();
            return new JsonDegeri.Nesne(alanlar);
        }
        while (true) {
            bosluklariAtla();
            if (simdiki() != '"') {
                throw hata("Alan adı (\"...\") bekleniyordu");
            }
            int adKonumu = i;
            String ad = metin();
            bosluklariAtla();
            bekle(':');
            bosluklariAtla();
            if (alanlar.put(ad, deger()) != null) {
                // JSON standardı bunu tanımsız bırakıyor; sessizce birini seçmek yerine reddediyorum
                i = adKonumu;
                throw hata("Aynı alan iki kez yazılmış: \"" + ad + "\"");
            }
            bosluklariAtla();
            char c = simdiki();
            i++;
            if (c == '}') {
                cik();
                return new JsonDegeri.Nesne(alanlar);
            }
            if (c != ',') {
                i--;
                throw hata("',' ya da '}' bekleniyordu");
            }
        }
    }

    private JsonDegeri.Dizi dizi() {
        icineGir();
        i++;   // '['
        List<JsonDegeri> elemanlar = new ArrayList<>();
        bosluklariAtla();
        if (simdiki() == ']') {
            i++;
            cik();
            return new JsonDegeri.Dizi(elemanlar);
        }
        while (true) {
            bosluklariAtla();
            elemanlar.add(deger());
            bosluklariAtla();
            char c = simdiki();
            i++;
            if (c == ']') {
                cik();
                return new JsonDegeri.Dizi(elemanlar);
            }
            if (c != ',') {
                i--;
                throw hata("',' ya da ']' bekleniyordu");
            }
        }
    }

    private String metin() {
        i++;   // açılış tırnağı
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (i >= metin.length()) {
                throw hata("Metin kapanmadan bitti (eksik \")");
            }
            char c = metin.charAt(i++);
            if (c == '"') {
                return sb.toString();
            }
            if (c < 0x20) {
                i--;
                throw hata("Metnin içinde kaçışsız kontrol karakteri (satır sonu \\n olarak yazılmalı)");
            }
            if (c != '\\') {
                sb.append(c);
                continue;
            }
            if (i >= metin.length()) {
                throw hata("Kaçış dizisi yarım kaldı");
            }
            char kacis = metin.charAt(i++);
            switch (kacis) {
                case '"' -> sb.append('"');
                case '\\' -> sb.append('\\');
                case '/' -> sb.append('/');
                case 'b' -> sb.append('\b');
                case 'f' -> sb.append('\f');
                case 'n' -> sb.append('\n');
                case 'r' -> sb.append('\r');
                case 't' -> sb.append('\t');
                case 'u' -> sb.append(unicode());
                default -> {
                    i--;
                    throw hata("Geçersiz kaçış: \\" + kacis);
                }
            }
        }
    }

    /** {@code ş} → 'ş': dört onaltılık hane. */
    private char unicode() {
        if (i + 4 > metin.length()) {
            throw hata("\\u'dan sonra dört onaltılık hane bekleniyordu");
        }
        String hex = metin.substring(i, i + 4);
        try {
            char c = (char) Integer.parseInt(hex, 16);
            i += 4;
            return c;
        } catch (NumberFormatException e) {
            throw hata("Geçersiz \\u kaçışı: " + hex);
        }
    }

    private JsonDegeri.Sayi sayi() {
        Matcher m = SAYI.matcher(metin).region(i, metin.length());
        // lookingAt: buradan başlayan en uzun eşleşme, metnin sonuna kadar gitmesi gerekmiyor
        if (!m.lookingAt()) {
            throw hata("Geçersiz sayı");
        }
        int son = m.end();
        // "01" ya da "1.": desen "0"ı ya da "1"i alıp bırakırdı; hemen ardından rakam ya da nokta gelmemeli
        if (son < metin.length() && (Character.isDigit(metin.charAt(son)) || metin.charAt(son) == '.')) {
            throw hata("Geçersiz sayı");
        }
        BigDecimal deger = new BigDecimal(metin.substring(i, son));
        i = son;
        return new JsonDegeri.Sayi(deger);
    }

    private JsonDegeri sabit(String kelime, JsonDegeri deger) {
        if (!metin.startsWith(kelime, i)) {
            throw hata("'" + kelime + "' bekleniyordu");
        }
        i += kelime.length();
        return deger;
    }

    private void icineGir() {
        if (++derinlik > EN_FAZLA_DERINLIK) {
            throw hata("İç içe geçme " + EN_FAZLA_DERINLIK + " seviyeyi aşıyor");
        }
    }

    private void cik() {
        derinlik--;
    }

    private void bekle(char beklenen) {
        if (simdiki() != beklenen) {
            throw hata("'" + beklenen + "' bekleniyordu");
        }
        i++;
    }

    private char simdiki() {
        if (i >= metin.length()) {
            throw hata("Metin beklenmedik yerde bitti");
        }
        return metin.charAt(i);
    }

    private void bosluklariAtla() {
        while (i < metin.length() && " \t\n\r".indexOf(metin.charAt(i)) >= 0) {
            i++;
        }
    }

    /** Konumu (karakter sırası) insanın okuyacağı satır ve sütuna çevirir. */
    private JsonHatasi hata(String mesaj) {
        int satir = 1;
        int sutun = 1;
        for (int k = 0; k < Math.min(i, metin.length()); k++) {
            if (metin.charAt(k) == '\n') {
                satir++;
                sutun = 1;
            } else {
                sutun++;
            }
        }
        return new JsonHatasi(mesaj, satir, sutun);
    }
}
