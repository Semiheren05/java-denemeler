package com.semih.gunluk.gun32;

import java.util.Optional;

/**
 * Alan {@code null} olabilir ama dışarıya {@code Optional} olarak açılıyor. Kural: {@code Optional}
 * dönüş tipi içindir; alan, parametre ya da koleksiyon elemanı olarak kullanılmaz
 * (serileştirilemiyor, her erişimde fazladan nesne, "ya Optional'ın kendisi null ise?").
 */
public final class Musteri {

    private final String id;
    private final String ad;
    private final Adres adres;   // null olabilir: adres henüz girilmemiş

    public Musteri(String id, String ad, Adres adres) {
        this.id = id;
        this.ad = ad;
        this.adres = adres;
    }

    public String id() {
        return id;
    }

    public String ad() {
        return ad;
    }

    public Optional<Adres> adres() {
        return Optional.ofNullable(adres);
    }

    /** Eski kodun kullandığı ham erişim; {@code null} dönebilir. */
    Adres adresYaDaNull() {
        return adres;
    }
}
