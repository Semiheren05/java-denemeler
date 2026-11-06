package com.semih.gunluk.gun26;

import java.io.Serial;

public class GecersizGecisHatasi extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public GecersizGecisHatasi(String mesaj) {
        super(mesaj);
    }
}
