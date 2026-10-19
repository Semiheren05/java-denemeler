package com.semih.gunluk.gun02;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Bir metindeki kelimeleri sayar ve en sık geçenleri bulur.
 *
 * <p>Kurallar:
 * <ul>
 *   <li>Büyük/küçük harf ayrımı yapılmaz ve Türkçe kurallarına göre küçültülür
 *       ("IŞIK" → "ışık", "İstanbul" → "istanbul").</li>
 *   <li>Harf ve rakam dışındaki her şey (boşluk, noktalama) kelime ayırıcıdır.</li>
 *   <li>Kesme işaretiyle ayrılan ekler ayrı kelime sayılmaz: "Semih'in" → "semih".</li>
 * </ul>
 */
public class KelimeSayaci {

    private static final Locale TURKCE = Locale.forLanguageTag("tr-TR");

    /** Kesme işareti ve ardından gelen ek: "Semih'in", "Ankara’ya". */
    private static final Pattern KESME_EKI = Pattern.compile("['’]\\p{L}*");

    /** Harf (\p{L}) ve rakam (\p{N}) dışındaki her karakter dizisi bir ayırıcıdır. */
    private static final Pattern AYIRICI = Pattern.compile("[^\\p{L}\\p{N}]+");

    /**
     * @return kelime → kaç kez geçtiği; boş ya da null metin için boş map
     */
    public Map<String, Integer> say(String metin) {
        if (metin == null || metin.isBlank()) {
            return Map.of();
        }
        Map<String, Integer> sayilar = new HashMap<>();
        String temiz = KESME_EKI.matcher(metin.toLowerCase(TURKCE)).replaceAll("");
        for (String kelime : AYIRICI.split(temiz)) {
            if (!kelime.isEmpty()) {
                sayilar.merge(kelime, 1, Integer::sum);
            }
        }
        return Map.copyOf(sayilar);
    }

    /**
     * En sık geçen {@code n} kelime, çoktan aza sıralı.
     * Eşit sayıda geçenler alfabetik sıralanır, böylece sonuç her zaman aynıdır.
     */
    public List<KelimeSayisi> enSikKelimeler(String metin, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n negatif olamaz: " + n);
        }
        return say(metin).entrySet().stream()
                .map(e -> new KelimeSayisi(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingInt(KelimeSayisi::sayi).reversed()
                        .thenComparing(KelimeSayisi::kelime))
                .limit(n)
                .toList();
    }

    /** Metindeki toplam kelime sayısı (tekrarlar dahil). */
    public int toplamKelime(String metin) {
        return say(metin).values().stream().mapToInt(Integer::intValue).sum();
    }
}
