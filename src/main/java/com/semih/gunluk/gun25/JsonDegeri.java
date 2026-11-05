package com.semih.gunluk.gun25;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JSON'daki altı değer türü. {@code sealed}: başka tür olamaz, {@code switch} hepsini ele
 * alınca {@code default} gerekmiyor.
 */
public sealed interface JsonDegeri
        permits JsonDegeri.Nesne, JsonDegeri.Dizi, JsonDegeri.Metin, JsonDegeri.Sayi,
                JsonDegeri.Mantiksal, JsonDegeri.Bos {

    /** Alanlar yazıldığı sırayla tutuluyor ({@link LinkedHashMap}). */
    record Nesne(Map<String, JsonDegeri> alanlar) implements JsonDegeri {

        public Nesne {
            alanlar = Collections.unmodifiableMap(new LinkedHashMap<>(alanlar));
        }

        public Optional<JsonDegeri> alan(String ad) {
            return Optional.ofNullable(alanlar.get(ad));
        }
    }

    record Dizi(List<JsonDegeri> elemanlar) implements JsonDegeri {

        public Dizi {
            elemanlar = List.copyOf(elemanlar);
        }
    }

    record Metin(String deger) implements JsonDegeri {
    }

    /** {@code double} değil {@link BigDecimal}: "0.1" okunup yazılınca "0.1000000000000000055..." olmasın. */
    record Sayi(BigDecimal deger) implements JsonDegeri {
    }

    record Mantiksal(boolean deger) implements JsonDegeri {
    }

    /** JSON {@code null}'ı. Tek bir örneği yeter; enum bunu garanti ediyor. */
    enum Bos implements JsonDegeri {
        NULL
    }
}
