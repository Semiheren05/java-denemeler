package com.semih.gunluk.gun41;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Başlıktan URL'de kullanılacak kısa ad: "Java'da Çoklu İş Parçacığı!" → {@code javada-coklu-is-parcacigi}.
 *
 * <p>Sıra önemli: önce Türkçe harfler elle çevriliyor (çünkü {@code ı} ayrıştırılınca {@code i}'ye
 * dönmüyor, harfin kendisi farklı), sonra NFD ile harf ve aksan ayrılıp aksanlar ({@code \p{M}})
 * atılıyor (é → e), en son küçük harfe {@link Locale#ROOT} ile çevriliyor.
 */
public final class SlugUretici {

    private static final Pattern AKSAN = Pattern.compile("\\p{M}+");
    private static final Pattern KESME = Pattern.compile("['’]");
    private static final Pattern HARF_RAKAM_DISI = Pattern.compile("[^a-z0-9]+");

    private SlugUretici() {
    }

    public static String slug(String baslik, int enFazla) {
        String s = turkceCevir(baslik);
        s = AKSAN.matcher(Normalizer.normalize(s, Normalizer.Form.NFD)).replaceAll("");
        s = s.toLowerCase(Locale.ROOT);
        s = KESME.matcher(s).replaceAll("");                    // Java'da → javada, java-da değil
        s = HARF_RAKAM_DISI.matcher(s).replaceAll("-");
        s = kenarTireleriniAt(s);
        if (s.length() > enFazla) {
            int kesim = s.lastIndexOf('-', enFazla);             // kelimenin ortasından kesme
            s = kenarTireleriniAt(kesim > 0 ? s.substring(0, kesim) : s.substring(0, enFazla));
        }
        if (s.isEmpty()) {
            throw new IllegalArgumentException("Başlıktan slug üretilemedi: " + baslik);
        }
        return s;
    }

    /** Alınmışsa sonuna -2, -3... ekler. */
    public static String benzersiz(String slug, Set<String> mevcutlar) {
        if (!mevcutlar.contains(slug)) {
            return slug;
        }
        int n = 2;
        while (mevcutlar.contains(slug + "-" + n)) {
            n++;
        }
        return slug + "-" + n;
    }

    private static String turkceCevir(String metin) {
        StringBuilder sb = new StringBuilder(metin.length());
        for (char c : metin.toCharArray()) {
            sb.append(switch (c) {
                case 'ı', 'İ' -> 'i';
                case 'ş', 'Ş' -> 's';
                case 'ğ', 'Ğ' -> 'g';
                case 'ç', 'Ç' -> 'c';
                case 'ö', 'Ö' -> 'o';
                case 'ü', 'Ü' -> 'u';
                default -> c;
            });
        }
        return sb.toString();
    }

    private static String kenarTireleriniAt(String s) {
        int bas = 0;
        int son = s.length();
        while (bas < son && s.charAt(bas) == '-') {
            bas++;
        }
        while (son > bas && s.charAt(son - 1) == '-') {
            son--;
        }
        return s.substring(bas, son);
    }
}
