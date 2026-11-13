package com.semih.gunluk.gun36;

import java.time.Instant;

/** {@code yol} normalleştirilmiş hâli: {@code /api/posts/12} → {@code /api/posts/{id}}. */
public record LogKaydi(Instant zaman, String seviye, String metot, String yol, int durum, long sureMs) {

    public String ucNokta() {
        return metot + " " + yol;
    }

    public boolean sunucuHatasi() {
        return durum >= 500;
    }
}
