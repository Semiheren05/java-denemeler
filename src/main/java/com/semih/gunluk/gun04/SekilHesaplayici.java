package com.semih.gunluk.gun04;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Şekiller üzerinde hesaplamalar, Java 21'in {@code switch} pattern matching özelliğiyle.
 *
 * <p>Klasik yaklaşımda her şekil kendi {@code alan()} metodunu yazardı. Burada veri
 * ({@code record}) ile davranış ayrı tutuluyor: hesaplama tek yerde, ve {@link Sekil}
 * {@code sealed} olduğu için derleyici tüm şekillerin ele alındığını garanti ediyor.
 */
public final class SekilHesaplayici {

    private SekilHesaplayici() {
        // yardımcı sınıf, nesnesi oluşturulmaz
    }

    public static double alan(Sekil sekil) {
        return switch (sekil) {
            case Daire d -> Math.PI * d.yaricap() * d.yaricap();
            case Dikdortgen r -> r.en() * r.boy();
            case Kare k -> k.kenar() * k.kenar();
            case Ucgen u -> heron(u);
            // default yok: Sekil sealed olduğu için derleyici tüm durumları biliyor
        };
    }

    public static double cevre(Sekil sekil) {
        // Record pattern: alanlar doğrudan değişkenlere açılıyor (deconstruction)
        return switch (sekil) {
            case Daire(double r) -> 2 * Math.PI * r;
            case Dikdortgen(double en, double boy) -> 2 * (en + boy);
            case Kare(double kenar) -> 4 * kenar;
            case Ucgen(double a, double b, double c) -> a + b + c;
        };
    }

    /**
     * Şeklin Türkçe tarifi. {@code when} ile koşullu (guarded) desenler kullanılıyor;
     * Java desenleri yukarıdan aşağı denediği için özel durumlar genelden önce yazılmalı.
     */
    public static String tarif(Sekil sekil) {
        return switch (sekil) {
            case Daire(double r) when r >= 100 -> "büyük bir daire";
            case Daire d -> "daire";
            case Dikdortgen(double en, double boy) when en == boy -> "kare biçiminde dikdörtgen";
            case Dikdortgen r -> "dikdörtgen";
            case Kare k -> "kare";
            case Ucgen u when u.eskenarMi() -> "eşkenar üçgen";
            case Ucgen u when u.ikizkenarMi() -> "ikizkenar üçgen";
            case Ucgen u -> "çeşitkenar üçgen";
        };
    }

    /**
     * Her türlü nesneyi tanır. {@code Object} sealed olmadığı için burada {@code default}
     * zorunlu. Java 21'de {@code case null} ile null da switch içinde ele alınabiliyor.
     */
    public static String tanimla(Object nesne) {
        return switch (nesne) {
            case null -> "boş (null)";
            case Sekil s -> tarif(s) + ", alanı " + String.format(Locale.ROOT, "%.2f", alan(s));
            case Integer i when i < 0 -> "negatif tam sayı";
            case Integer i -> "tam sayı";
            case String str when str.isBlank() -> "boş metin";
            case String str -> "metin (" + str.length() + " karakter)";
            default -> "bilinmeyen: " + nesne.getClass().getSimpleName();
        };
    }

    public static double toplamAlan(List<? extends Sekil> sekiller) {
        return sekiller.stream()
                .mapToDouble(SekilHesaplayici::alan)
                .sum();
    }

    public static Optional<Sekil> enBuyuk(List<? extends Sekil> sekiller) {
        return sekiller.stream()
                .map(Sekil.class::cast)
                .max(Comparator.comparingDouble(SekilHesaplayici::alan));
    }

    /** Heron formülü: üç kenarı bilinen üçgenin alanı. */
    private static double heron(Ucgen u) {
        double s = (u.a() + u.b() + u.c()) / 2;
        return Math.sqrt(s * (s - u.a()) * (s - u.b()) * (s - u.c()));
    }
}
