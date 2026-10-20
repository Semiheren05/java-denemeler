package com.semih.gunluk.gun03;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.TreeMap;

import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

/**
 * Bir sınıfın not listesi üzerinde Streams API ile istatistikler.
 * Hiçbir metot listeyi değiştirmez; her biri yeni bir sonuç üretir.
 */
public class NotAnalizi {

    /** En yüksek puan önce; puanlar eşitse ada göre alfabetik. */
    private static final Comparator<Ogrenci> BASARI_SIRASI =
            Comparator.comparingInt(Ogrenci::puan).reversed()
                    .thenComparing(Ogrenci::ad);

    private final List<Ogrenci> ogrenciler;

    public NotAnalizi(List<Ogrenci> ogrenciler) {
        if (ogrenciler == null) {
            throw new IllegalArgumentException("Öğrenci listesi boş olamaz (null)");
        }
        // Dışarıdaki liste sonradan değişse de analiz etkilenmesin diye kopyalanır.
        this.ogrenciler = List.copyOf(ogrenciler);
    }

    /** Sınıf ortalaması; liste boşsa değer yoktur. */
    public OptionalDouble ortalama() {
        return ogrenciler.stream()
                .mapToInt(Ogrenci::puan)
                .average();
    }

    public Optional<Ogrenci> enYuksek() {
        return ogrenciler.stream().min(BASARI_SIRASI);
    }

    public Optional<Ogrenci> enDusuk() {
        return ogrenciler.stream().max(BASARI_SIRASI);
    }

    /** En başarılı {@code n} öğrenci, yüksekten düşüğe. */
    public List<Ogrenci> ilkN(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n negatif olamaz: " + n);
        }
        return ogrenciler.stream()
                .sorted(BASARI_SIRASI)
                .limit(n)
                .toList();
    }

    /**
     * Harf notu → o notu alan öğrencilerin adları.
     * {@link EnumMap} sayesinde anahtarlar AA'dan FF'ye sıralı gelir.
     */
    public Map<HarfNotu, List<String>> harfNotunaGoreGrupla() {
        return ogrenciler.stream()
                .sorted(BASARI_SIRASI)
                .collect(groupingBy(
                        Ogrenci::harfNotu,
                        () -> new EnumMap<>(HarfNotu.class),
                        mapping(Ogrenci::ad, toList())));
    }

    /** Harf notu → kaç öğrenci aldığı. Hiç alınmayan notlar sonuçta yer almaz. */
    public Map<HarfNotu, Long> harfNotuDagilimi() {
        return ogrenciler.stream()
                .collect(groupingBy(
                        Ogrenci::harfNotu,
                        () -> new EnumMap<>(HarfNotu.class),
                        counting()));
    }

    /** Bölüm → bölümün ortalaması, bölüm adına göre sıralı. */
    public Map<String, Double> bolumOrtalamalari() {
        return ogrenciler.stream()
                .collect(groupingBy(
                        Ogrenci::bolum,
                        TreeMap::new,
                        averagingInt(Ogrenci::puan)));
    }

    /** {@code true} → geçenler, {@code false} → kalanlar. İki anahtar da her zaman vardır. */
    public Map<Boolean, List<Ogrenci>> gecenVeKalanlar() {
        return ogrenciler.stream()
                .collect(partitioningBy(Ogrenci::gectiMi));
    }

    /** En düşük, en yüksek, ortalama, toplam ve sayı tek geçişte. */
    public IntSummaryStatistics istatistikler() {
        return ogrenciler.stream()
                .mapToInt(Ogrenci::puan)
                .summaryStatistics();
    }
}
