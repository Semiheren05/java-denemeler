package com.semih.gunluk.gun33;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Motor kuralların ne olduğunu bilmiyor; sadece sırayla deniyor. Yeni bir kural bir satır
 * {@link #ekle}, motora dokunmak yok. İlk hatada durmuyor: kullanıcıya bütün sebepler birden gösteriliyor.
 */
public class KuralMotoru {

    private final Map<String, Kural> kurallar = new LinkedHashMap<>();
    private Consumer<Karar> kararDinleyici = karar -> { };

    public KuralMotoru ekle(Kural kural) {
        kurallar.put(kural.ad(), kural);
        return this;
    }

    public KuralMotoru cikar(String ad) {
        kurallar.remove(ad);
        return this;
    }

    /** {@code Consumer.andThen}: birden fazla dinleyici, verildikleri sırayla. */
    public KuralMotoru dinle(Consumer<Karar> dinleyici) {
        kararDinleyici = kararDinleyici.andThen(dinleyici);
        return this;
    }

    public Karar degerlendir(Basvuru basvuru) {
        List<String> sebepler = new ArrayList<>();
        for (Kural kural : kurallar.values()) {
            if (!kural.kosul().test(basvuru)) {
                sebepler.add(kural.redSebebi());
            }
        }
        Karar karar = new Karar(basvuru.ad(), sebepler.isEmpty(), sebepler);
        kararDinleyici.accept(karar);
        return karar;
    }
}
