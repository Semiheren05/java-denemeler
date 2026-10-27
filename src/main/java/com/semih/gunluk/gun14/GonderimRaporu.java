package com.semih.gunluk.gun14;

import java.time.Duration;

/** Toplu gönderimin sonucu. */
public record GonderimRaporu(int basarili, int basarisiz, Duration sure) {

    public int toplam() {
        return basarili + basarisiz;
    }
}
