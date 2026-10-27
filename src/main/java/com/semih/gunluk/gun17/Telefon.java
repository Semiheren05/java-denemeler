package com.semih.gunluk.gun17;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Türkiye cep telefonu numaralarını tek biçime getirir. Kullanıcılar aynı numarayı
 * "0532 123 45 67", "+90 (532) 123-4567", "5321234567" diye farklı yazıyor; veritabanına
 * hep {@code +905321234567} (E.164) olarak giriyor.
 *
 * <p>Regex'te <b>adlandırılmış gruplar</b> var: {@code (?<operator>...)}. Parçaları
 * {@code m.group(1)} gibi sıra numarasıyla değil adıyla alıyorum; desene yeni bir grup
 * eklense bile kod bozulmuyor.
 */
public final class Telefon {

    private static final Pattern CEP = Pattern.compile(
            "(?:(?:\\+|00)90|0)?(?<operator>5\\d{2})(?<ilkUc>\\d{3})(?<ikinciIki>\\d{2})(?<sonIki>\\d{2})");
    // Boşluk, tire, nokta ve parantezler yazım alışkanlığı; numaranın parçası değil
    private static final Pattern SUS = Pattern.compile("[\\s\\-.()]");

    private Telefon() {
    }

    /** @return E.164 biçimi ({@code +905321234567}); cep numarası değilse boş */
    public static Optional<String> normalle(String girdi) {
        return eslesme(girdi).map(m ->
                "+90" + m.group("operator") + m.group("ilkUc") + m.group("ikinciIki") + m.group("sonIki"));
    }

    /** Okunabilir biçim: {@code 0532 123 45 67}. */
    public static Optional<String> okunur(String girdi) {
        return eslesme(girdi).map(m -> "0%s %s %s %s".formatted(
                m.group("operator"), m.group("ilkUc"), m.group("ikinciIki"), m.group("sonIki")));
    }

    private static Optional<Matcher> eslesme(String girdi) {
        if (girdi == null) {
            return Optional.empty();
        }
        Matcher m = CEP.matcher(SUS.matcher(girdi).replaceAll(""));
        // matches(): bütün metin desene uymalı; find() olsaydı uzun bir sayının içindeki parça da geçerdi
        return m.matches() ? Optional.of(m) : Optional.empty();
    }
}
