package com.semih.gunluk.gun05;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Generic metotlar: tip parametresi sınıfta değil, metodun kendisinde tanımlanır.
 *
 * <p>Wildcard'larda kullanılan kural PECS'tir ("Producer Extends, Consumer Super"):
 * yalnızca okunan koleksiyon {@code ? extends T}, yalnızca yazılan koleksiyon
 * {@code ? super T} olur.
 */
public final class GenelAraclar {

    private GenelAraclar() {
        // yardımcı sınıf, nesnesi oluşturulmaz
    }

    public static <T extends Comparable<? super T>> Optional<T> enBuyuk(Collection<? extends T> elemanlar) {
        T enBuyuk = null;
        for (T eleman : elemanlar) {
            if (enBuyuk == null || eleman.compareTo(enBuyuk) > 0) {
                enBuyuk = eleman;
            }
        }
        return Optional.ofNullable(enBuyuk);
    }

    /** Kaynak sadece okunuyor (extends), hedefe sadece yazılıyor (super). */
    public static <T> void kopyala(Collection<? extends T> kaynak, Collection<? super T> hedef) {
        hedef.addAll(kaynak);
    }

    public static <T, R> List<R> donustur(Collection<? extends T> elemanlar,
                                          Function<? super T, ? extends R> donusum) {
        List<R> sonuc = new ArrayList<>();
        for (T eleman : elemanlar) {
            sonuc.add(donusum.apply(eleman));
        }
        return List.copyOf(sonuc);
    }

    /** Koşulu sağlayanlar birinci, sağlamayanlar ikinci listede. */
    public static <T> Cift<List<T>, List<T>> ayir(Collection<? extends T> elemanlar,
                                                  Predicate<? super T> kosul) {
        List<T> saglayanlar = new ArrayList<>();
        List<T> saglamayanlar = new ArrayList<>();
        for (T eleman : elemanlar) {
            (kosul.test(eleman) ? saglayanlar : saglamayanlar).add(eleman);
        }
        return new Cift<>(List.copyOf(saglayanlar), List.copyOf(saglamayanlar));
    }
}
