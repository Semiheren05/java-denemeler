package com.semih.gunluk.gun32;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/** Bulamayınca {@code null} değil boş {@code Optional} dönüyor: "bulunamayabilir" imzada yazıyor. */
public class MusteriDeposu {

    private final Map<String, Musteri> musteriler = new HashMap<>();
    private final AtomicInteger sorguSayisi = new AtomicInteger();

    public MusteriDeposu ekle(Musteri musteri) {
        musteriler.put(musteri.id(), musteri);
        return this;
    }

    public Optional<Musteri> bul(String id) {
        sorguSayisi.incrementAndGet();
        return Optional.ofNullable(musteriler.get(id));
    }

    /** Eski stil: bulamazsa {@code null}. */
    Musteri bulYaDaNull(String id) {
        sorguSayisi.incrementAndGet();
        return musteriler.get(id);
    }

    /** <b>Bilerek hatalı:</b> {@code Optional} döndüren bir metot asla {@code null} döndürmemeli. */
    Optional<Musteri> hataliBul(String id) {
        return musteriler.containsKey(id) ? Optional.of(musteriler.get(id)) : null;
    }

    public int sorguSayisi() {
        return sorguSayisi.get();
    }
}
