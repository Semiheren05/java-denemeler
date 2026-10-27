package com.semih.gunluk.gun18;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Ya bir değer ya da hata listesi. Beklenen hatalar (yanlış girilmiş bir form alanı) için
 * istisna fırlatmak yerine dönüş tipiyle söylüyor: imzaya bakan herkes "bu başarısız olabilir"
 * diye görüyor ve derleyici iki durumu da ele almaya zorluyor.
 *
 * <p>{@code sealed}: sadece {@link Basarili} ve {@link Hatali} olabilir. {@code switch} ile
 * ikisi de ele alındıysa {@code default} gerekmiyor; üçüncü bir tür eklenirse derleme bozuluyor.
 *
 * @param <T> başarılıysa taşınan değerin tipi
 */
public sealed interface Sonuc<T> permits Sonuc.Basarili, Sonuc.Hatali {

    record Basarili<T>(T deger) implements Sonuc<T> {
    }

    record Hatali<T>(List<String> hatalar) implements Sonuc<T> {

        public Hatali {
            if (hatalar == null || hatalar.isEmpty()) {
                throw new IllegalArgumentException("Hatalı sonucun en az bir hatası olmalı");
            }
            hatalar = List.copyOf(hatalar);
        }
    }

    static <T> Sonuc<T> basarili(T deger) {
        return new Basarili<>(deger);
    }

    static <T> Sonuc<T> hatali(String... hatalar) {
        return new Hatali<>(Arrays.asList(hatalar));
    }

    /** İstisna fırlatan eski tarz bir işlemi (ör. {@code Integer.parseInt}) sonuca çevirir. */
    static <T> Sonuc<T> dene(Supplier<T> islem, String hataMesaji) {
        try {
            return basarili(islem.get());
        } catch (RuntimeException e) {
            return hatali(hataMesaji);
        }
    }

    static <T> Sonuc<T> optionaldan(Optional<T> deger, String bossaHata) {
        return deger.<Sonuc<T>>map(Sonuc::basarili).orElseGet(() -> hatali(bossaHata));
    }

    /** Birbirinden bağımsız sonuçların bütün hataları, sırasıyla. Hepsi başarılıysa boş. */
    static List<String> tumHatalar(Sonuc<?>... sonuclar) {
        List<String> hepsi = new ArrayList<>();
        for (Sonuc<?> sonuc : sonuclar) {
            if (sonuc instanceof Hatali<?>(List<String> hatalar)) {
                hepsi.addAll(hatalar);
            }
        }
        return hepsi;
    }

    /** Başarılıysa değeri dönüştürür; hatalıysa hataları olduğu gibi taşır, fonksiyonu hiç çağırmaz. */
    default <R> Sonuc<R> map(Function<? super T, ? extends R> donustur) {
        return switch (this) {
            case Basarili<T>(T deger) -> basarili(donustur.apply(deger));
            case Hatali<T>(List<String> hatalar) -> new Hatali<>(hatalar);
        };
    }

    /**
     * Sıradaki adım da başarısız olabiliyorsa. İlk hatada zincir durur ("fail fast"); sonraki
     * adımlar çalışmaz.
     */
    default <R> Sonuc<R> flatMap(Function<? super T, Sonuc<R>> sonraki) {
        return switch (this) {
            case Basarili<T>(T deger) -> sonraki.apply(deger);
            case Hatali<T>(List<String> hatalar) -> new Hatali<>(hatalar);
        };
    }

    /** İki durumu tek bir değere indirger: ör. HTTP 201 ya da 400 cevabı. */
    default <R> R katla(Function<? super T, ? extends R> basariysa, Function<List<String>, ? extends R> hataysa) {
        return switch (this) {
            case Basarili<T>(T deger) -> basariysa.apply(deger);
            case Hatali<T>(List<String> hatalar) -> hataysa.apply(hatalar);
        };
    }

    default boolean basariliMi() {
        return this instanceof Basarili<T>;
    }

    /**
     * Değeri alır; hatalıysa {@link IllegalStateException}. Sadece başarılı olduğu
     * kontrol edildikten sonra kullanılmalı ({@code Optional.orElseThrow} gibi).
     */
    default T deger() {
        return katla(deger -> deger, hatalar -> {
            throw new IllegalStateException("Hatalı sonucun değeri yok: " + hatalar);
        });
    }
}
