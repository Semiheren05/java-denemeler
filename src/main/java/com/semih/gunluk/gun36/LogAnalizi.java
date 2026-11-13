package com.semih.gunluk.gun36;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Log dosyasını {@link Files#lines} ile satır satır, tembel okur: dosya ne kadar büyük olursa olsun
 * bellekte bir anda tek satır var. {@code Files.readAllLines} 2 GB'lık logda belleği bitirirdi.
 * Akış bir dosya tutuyor; bu yüzden try-with-resources ile kapatılıyor.
 */
public final class LogAnalizi {

    private LogAnalizi() {
    }

    public static Rapor analizEt(Path dosya, int enYavasAdet) {
        AtomicLong bozuk = new AtomicLong();
        List<LogKaydi> kayitlar = new ArrayList<>();
        satirlar(dosya, s -> s.forEach(satir -> LogAyristirici.ayristir(satir)
                .ifPresentOrElse(kayitlar::add, bozuk::incrementAndGet)));

        double ortalama = kayitlar.stream().mapToLong(LogKaydi::sureMs).average().orElse(0);
        long enUzun = kayitlar.stream().mapToLong(LogKaydi::sureMs).max().orElse(0);

        TreeMap<LocalDateTime, Long> saatlik = kayitlar.stream()
                .filter(LogKaydi::sunucuHatasi)
                .collect(Collectors.groupingBy(
                        k -> LocalDateTime.ofInstant(k.zaman(), ZoneOffset.UTC).truncatedTo(ChronoUnit.HOURS),
                        TreeMap::new, Collectors.counting()));

        List<Map.Entry<String, Long>> enYavaslar = kayitlar.stream()
                .collect(Collectors.groupingBy(LogKaydi::ucNokta,
                        Collectors.mapping(LogKaydi::sureMs, Collectors.toList())))
                .entrySet().stream()
                .map(e -> Map.entry(e.getKey(), yuzdelik(e.getValue(), 95)))
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(enYavasAdet)
                .toList();

        TreeMap<String, Long> siniflar = kayitlar.stream()
                .collect(Collectors.groupingBy(k -> k.durum() / 100 + "xx", TreeMap::new, Collectors.counting()));

        return new Rapor(kayitlar.size(), bozuk.get(), ortalama, enUzun, saatlik, enYavaslar, siniflar);
    }

    /** İlk sunucu hatasını bulur; {@code findFirst} bulunca okumayı bırakıyor, dosyanın kalanı okunmuyor. */
    public static Optional<LogKaydi> ilkSunucuHatasi(Path dosya, AtomicLong okunanSatir) {
        List<Optional<LogKaydi>> sonuc = new ArrayList<>();
        satirlar(dosya, s -> sonuc.add(s
                .peek(satir -> okunanSatir.incrementAndGet())
                .map(LogAyristirici::ayristir)
                .flatMap(Optional::stream)
                .filter(LogKaydi::sunucuHatasi)
                .findFirst()));
        return sonuc.get(0);
    }

    /**
     * Yüzdelik, "en yakın sıra" yöntemiyle: sıralı listede ⌈p/100 · n⌉. eleman.
     * p95 = isteklerin %95'i bu süreden kısa. Ortalama tek bir çok yavaş isteği gizler; p95 gizlemez.
     */
    public static long yuzdelik(List<Long> degerler, int p) {
        if (degerler.isEmpty()) {
            throw new IllegalArgumentException("Boş liste");
        }
        List<Long> sirali = degerler.stream().sorted(Comparator.naturalOrder()).toList();
        int sira = (int) Math.ceil(p / 100.0 * sirali.size());
        return sirali.get(Math.max(sira, 1) - 1);
    }

    private static void satirlar(Path dosya, Consumer<Stream<String>> is) {
        try (Stream<String> satirlar = Files.lines(dosya, StandardCharsets.UTF_8)) {
            is.accept(satirlar);
        } catch (IOException | UncheckedIOException e) {
            throw new LogOkumaHatasi("Log okunamadı: " + dosya.getFileName(), e);
        }
    }
}
