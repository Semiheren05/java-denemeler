package com.semih.gunluk.gun22;

import java.io.Serial;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Aynı davranış, Java'nın hazır parçalarıyla. {@link LinkedHashMap}'in içinde zaten bir bağlı
 * liste var. {@code accessOrder = true} verilince sırayı ekleme sırasına göre değil son
 * erişime göre tutuyor; {@link #removeEldestEntry} {@code true} dönünce de en eskiyi atıyor.
 *
 * <p>Gerçek projede bu (ya da Caffeine gibi bir kütüphane) kullanılır; {@link LruOnbellek}
 * içeride ne olduğunu görmek için.
 */
public class KisaLruOnbellek<K, V> extends LinkedHashMap<K, V> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int kapasite;

    public KisaLruOnbellek(int kapasite) {
        super(16, 0.75f, true);
        this.kapasite = kapasite;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> enEski) {
        return size() > kapasite;
    }
}
