package com.semih.gunluk.gun36;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * @param saatlikHatalar saat başı (UTC) → 5xx sayısı, zamana göre sıralı
 * @param enYavaslar     uç nokta → p95 süresi (ms), yavaştan hızlıya
 * @param durumSiniflari "2xx", "4xx", "5xx" → adet
 */
public record Rapor(long kayit, long bozukSatir, double ortalamaMs, long enUzunMs,
                    SortedMap<LocalDateTime, Long> saatlikHatalar,
                    List<Map.Entry<String, Long>> enYavaslar,
                    SortedMap<String, Long> durumSiniflari) {
}
