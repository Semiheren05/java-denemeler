package com.semih.gunluk.gun36;

public class LogOkumaHatasi extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LogOkumaHatasi(String mesaj, Throwable sebep) {
        super(mesaj, sebep);
    }
}
