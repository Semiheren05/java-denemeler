package com.semih.gunluk.gun20;

/** Nesne kurulamadı: kayıt eksik, döngü var ya da constructor seçilemedi. */
public class KapsayiciHatasi extends RuntimeException {

    public KapsayiciHatasi(String mesaj) {
        super(mesaj);
    }

    public KapsayiciHatasi(String mesaj, Throwable neden) {
        super(mesaj, neden);
    }
}
