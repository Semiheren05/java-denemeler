package com.semih.gunluk.gun14;

/** Gönderilecek tek bir SMS: kime ve ne. */
public record Bildirim(String telefon, String mesaj) {

    public Bildirim {
        if (telefon == null || telefon.isBlank()) {
            throw new IllegalArgumentException("Telefon boş olamaz");
        }
        if (mesaj == null || mesaj.isBlank()) {
            throw new IllegalArgumentException("Mesaj boş olamaz");
        }
    }
}
