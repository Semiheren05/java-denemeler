package com.semih.gunluk.gun08;

import java.nio.file.Path;

/**
 * Dosyanın kendisi okunamadığında fırlatılır (yok, izin yok, başlık yanlış...).
 *
 * <p>Bilerek <b>checked</b> exception: dosya sistemi programın kontrolünde değil ve
 * çağıran kod bu durumu ele almak zorunda kalsın istiyorum. Asıl {@code IOException}
 * {@code cause} olarak saklanır, hiçbir bilgi kaybolmaz.
 */
public class DosyaOkunamadiException extends Exception {

    private static final long serialVersionUID = 1L;

    private final transient Path dosya;

    public DosyaOkunamadiException(Path dosya, String mesaj, Throwable neden) {
        super(mesaj + ": " + dosya, neden);
        this.dosya = dosya;
    }

    public DosyaOkunamadiException(Path dosya, String mesaj) {
        this(dosya, mesaj, null);
    }

    public Path getDosya() {
        return dosya;
    }
}
