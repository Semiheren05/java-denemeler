package com.semih.gunluk.gun31;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Önerilen singleton: tek elemanlı {@code enum}. JVM, enum sabitini sınıf yüklenirken bir kez ve
 * thread-safe şekilde kurar; yansıma ({@code reflection}) ile ikinci örnek yaratılamaz,
 * serileştirme sonrası da aynı nesne döner. Ek kod gerekmez.
 */
public enum Ayarlar {
    INSTANCE;

    private final Map<String, String> degerler = new ConcurrentHashMap<>(Map.of("dil", "tr", "tema", "koyu"));

    public String getir(String anahtar) {
        return degerler.get(anahtar);
    }

    public void ayarla(String anahtar, String deger) {
        degerler.put(anahtar, deger);
    }
}
