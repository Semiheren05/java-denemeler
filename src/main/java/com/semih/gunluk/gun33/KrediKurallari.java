package com.semih.gunluk.gun33;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/** Hazır fonksiyonel arayüzlerin bir arada kullanıldığı kural kataloğu. */
public final class KrediKurallari {

    private KrediKurallari() {
    }

    public static final Predicate<Basvuru> RESIT = b -> b.yas() >= 18;
    public static final Predicate<Basvuru> IYI_KREDI_NOTU = b -> b.krediNotu() >= 1500;
    public static final Predicate<Basvuru> YUKSEK_GELIR = b -> b.aylikGelir() >= 50_000;
    public static final Predicate<Basvuru> DENEYIMLI = b -> b.calismaYili() >= 3;

    /** Borç, gelirin yarısını geçmesin. */
    public static final Predicate<Basvuru> BORC_ORANI_UYGUN = b -> b.aylikBorc() * 2 <= b.aylikGelir();

    /** {@code and}/{@code or} birleşimi: notu iyi olan ya da yüksek gelirli ve deneyimli olan. */
    public static final Predicate<Basvuru> GUVENILIR = IYI_KREDI_NOTU.or(YUKSEK_GELIR.and(DENEYIMLI));

    public static KuralMotoru standart() {
        return new KuralMotoru()
                .ekle(new Kural("ad", b -> b.ad() != null && Predicate.not(String::isBlank).test(b.ad()), "Ad boş olamaz"))
                .ekle(new Kural("yas", RESIT, "18 yaşından küçükler başvuramaz"))
                .ekle(new Kural("guven", GUVENILIR, "Kredi notu düşük ve gelir/deneyim yetersiz"))
                .ekle(new Kural("borc", BORC_ORANI_UYGUN, "Mevcut borç gelirin yarısını aşıyor"));
    }

    /** {@code Function}: başvurudan ham puan. */
    public static final Function<Basvuru, Integer> HAM_PUAN = b -> b.krediNotu() / 10;

    /** {@code UnaryOperator}: puana uygulanan düzeltmeler; listedeki sırayla zincirleniyor. */
    public static UnaryOperator<Integer> duzeltmeler(List<UnaryOperator<Integer>> adimlar) {
        return adimlar.stream()
                .reduce(UnaryOperator.identity(), (once, sonra) -> x -> sonra.apply(once.apply(x)));
    }

    /** {@code BiFunction}: puan ve vade (ay) → yıllık faiz, yüzde olarak. */
    public static final BiFunction<Integer, Integer, Double> FAIZ = (puan, vadeAy) -> {
        double taban = puan >= 180 ? 38.0 : puan >= 150 ? 42.0 : 48.0;
        return vadeAy > 24 ? taban + 2.0 : taban;
    };
}
