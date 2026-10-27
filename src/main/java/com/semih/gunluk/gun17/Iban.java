package com.semih.gunluk.gun17;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * IBAN doğrulaması (ISO 13616). Kontrol, uluslararası "mod 97" algoritmasıyla yapılıyor:
 * ilk dört karakter sona alınıyor, harfler sayıya çevriliyor (A=10 … Z=35), ortaya çıkan
 * çok büyük sayının 97'ye bölümünden kalan 1 olmalı.
 *
 * <p>Türk IBAN'ı 26 karakter: "TR", iki kontrol hanesi, 5 haneli banka kodu, 1 rezerv hane,
 * 16 haneli hesap numarası.
 */
public final class Iban {

    private static final Pattern BICIM = Pattern.compile("[A-Z]{2}\\d{2}[A-Z0-9]{11,30}");
    private static final int TR_UZUNLUK = 26;

    private Iban() {
    }

    public static boolean gecerliMi(String iban) {
        if (iban == null) {
            return false;
        }
        String temiz = temizle(iban);
        if (!BICIM.matcher(temiz).matches()) {
            return false;
        }
        if (temiz.startsWith("TR") && temiz.length() != TR_UZUNLUK) {
            return false;
        }
        return mod97(temiz.substring(4) + temiz.substring(0, 4)) == 1;
    }

    /** Dörtlü gruplar: "TR33 0006 1005 1978 6457 8413 26". */
    public static String bicimle(String iban) {
        if (!gecerliMi(iban)) {
            throw new IllegalArgumentException("Geçersiz IBAN: " + iban);
        }
        return temizle(iban).replaceAll("(.{4})(?!$)", "$1 ");
    }

    /**
     * 30 haneli bir sayı {@code long}'a sığmaz. {@code BigInteger} kullanmak yerine sayıyı
     * soldan sağa okuyup her adımda kalanı tutuyorum: (kalan × 10 + rakam) mod 97.
     * Elle uzun bölme yapmakla aynı şey; ara değer hiçbir zaman 10.000'i geçmiyor.
     */
    static int mod97(String metin) {
        int kalan = 0;
        for (int i = 0; i < metin.length(); i++) {
            int deger = Character.digit(metin.charAt(i), 36);   // '7' → 7, 'A' → 10, 'Z' → 35
            kalan = deger < 10 ? (kalan * 10 + deger) % 97 : (kalan * 100 + deger) % 97;
        }
        return kalan;
    }

    private static String temizle(String iban) {
        return iban.replace(" ", "").toUpperCase(Locale.ROOT);
    }
}
