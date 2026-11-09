package com.semih.gunluk.gun30.yeni;

import java.math.BigDecimal;

/** {@code String[]} ve "u[2] adetti" yorumu yerine adlı alanlar. */
public record SiparisKalemi(String urun, BigDecimal birimFiyat, int adet, Kategori kategori) {

    public SiparisKalemi {
        if (birimFiyat.signum() < 0 || adet < 1) {
            throw new IllegalArgumentException("Geçersiz kalem: " + urun);
        }
    }

    public BigDecimal kdvliTutar() {
        return birimFiyat.multiply(BigDecimal.valueOf(adet)).multiply(BigDecimal.ONE.add(kategori.kdvOrani()));
    }
}
