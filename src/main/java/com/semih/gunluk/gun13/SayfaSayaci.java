package com.semih.gunluk.gun13;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Aynı anda birçok thread'den çağrılabilen sayfa ziyaret sayacı.
 *
 * <p>Düz bir {@code HashMap<String, Long>} ile {@code sayilar.put(s, sayilar.get(s) + 1)}
 * yazsaydım, iki thread aynı değeri okuyup ikisi de "+1" yazabilir ve bir ziyaret kaybolurdu
 * (yarış durumu). Burada iki parça birlikte çalışıyor:
 * <ul>
 *   <li>{@link ConcurrentHashMap#computeIfAbsent}: bir sayfanın sayacı tam olarak bir kez oluşturulur.</li>
 *   <li>{@link AtomicLong#incrementAndGet}: "oku, bir ekle, yaz" tek ve bölünmez bir işlem.</li>
 * </ul>
 * Hiçbir yerde {@code synchronized} yok; thread'ler birbirini kilitle beklemiyor.
 */
public class SayfaSayaci {

    private final Map<String, AtomicLong> sayilar = new ConcurrentHashMap<>();
    private final AtomicLong toplam = new AtomicLong();

    public void kaydet(String sayfa) {
        if (sayfa == null || sayfa.isBlank()) {
            throw new IllegalArgumentException("Sayfa adı boş olamaz");
        }
        sayilar.computeIfAbsent(sayfa, s -> new AtomicLong()).incrementAndGet();
        toplam.incrementAndGet();
    }

    public long sayi(String sayfa) {
        AtomicLong sayac = sayilar.get(sayfa);
        return sayac == null ? 0 : sayac.get();
    }

    public long toplam() {
        return toplam.get();
    }

    /** En çok ziyaret edilen {@code adet} sayfa; eşitlikte alfabetik. */
    public List<String> enCokZiyaretEdilenler(int adet) {
        return sayilar.entrySet().stream()
                .sorted(Comparator.comparingLong((Map.Entry<String, AtomicLong> e) -> e.getValue().get()).reversed()
                        .thenComparing(Map.Entry::getKey))
                .limit(adet)
                .map(Map.Entry::getKey)
                .toList();
    }
}
