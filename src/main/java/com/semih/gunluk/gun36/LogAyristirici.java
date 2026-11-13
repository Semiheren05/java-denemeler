package com.semih.gunluk.gun36;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bir erişim logu satırını ayrıştırır:
 * <pre>2026-11-12T10:15:02.481Z INFO  [http-1] GET /api/posts/12?sayfa=2 200 35ms</pre>
 * Desen bir kez derleniyor ({@code static final}); her satırda {@code Pattern.compile} pahalı olurdu.
 */
public final class LogAyristirici {

    private static final Pattern SATIR = Pattern.compile(
            "^(?<zaman>\\S+)\\s+(?<seviye>[A-Z]+)\\s+\\[[^]]*]\\s+(?<metot>GET|POST|PUT|PATCH|DELETE)\\s+"
                    + "(?<yol>/\\S*)\\s+(?<durum>\\d{3})\\s+(?<sure>\\d+)ms$");
    private static final Pattern SAYI_PARCASI = Pattern.compile("/\\d+(?=/|$)");

    private LogAyristirici() {
    }

    /** Biçime uymayan satır (yığın izi, boş satır, bozuk zaman) için boş döner; istisna yok. */
    public static Optional<LogKaydi> ayristir(String satir) {
        Matcher m = SATIR.matcher(satir);
        if (!m.matches()) {
            return Optional.empty();
        }
        try {
            return Optional.of(new LogKaydi(
                    Instant.parse(m.group("zaman")),
                    m.group("seviye"),
                    m.group("metot"),
                    normallestir(m.group("yol")),
                    Integer.parseInt(m.group("durum")),
                    Long.parseLong(m.group("sure"))));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    /** Sorgu dizesini atar, sayı olan parçaları {@code {id}} yapar; böylece aynı uç nokta tek satırda toplanır. */
    public static String normallestir(String yol) {
        int soru = yol.indexOf('?');
        String yalin = soru >= 0 ? yol.substring(0, soru) : yol;
        return SAYI_PARCASI.matcher(yalin).replaceAll("/{id}");
    }
}
