package com.semih.gunluk.gun40;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Farklı şehirlerdeki katılımcılar için toplantı saati. Kural: an ({@link Instant}, UTC) saklanır,
 * yerel saat sadece gösterirken hesaplanır. "14:00" tek başına bir an değildir; "14:00 İstanbul" bir andır.
 */
public final class ToplantiPlanlayici {

    public static final ZoneId ISTANBUL = ZoneId.of("Europe/Istanbul");
    public static final ZoneId NEW_YORK = ZoneId.of("America/New_York");
    public static final ZoneId LONDRA = ZoneId.of("Europe/London");

    private ToplantiPlanlayici() {
    }

    public static Instant an(LocalDateTime yerelSaat, ZoneId bolge) {
        return yerelSaat.atZone(bolge).toInstant();
    }

    /** Aynı anın her katılımcının bölgesindeki yerel saati, bölge adına göre sıralı. */
    public static Map<String, LocalDateTime> herkesIcin(Instant an, List<ZoneId> bolgeler) {
        return bolgeler.stream().collect(Collectors.toMap(
                ZoneId::getId,
                b -> LocalDateTime.ofInstant(an, b),
                (a, b) -> a,
                TreeMap::new));
    }

    /** Yaz saati geçişinde var olmayan saat (ör. 02:30) ileri kayar; {@code atZone} bunu sessizce yapar. */
    public static ZonedDateTime yerlestir(LocalDateTime yerel, ZoneId bolge) {
        return yerel.atZone(bolge);
    }
}
