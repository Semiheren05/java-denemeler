package com.semih.gunluk.gun35;

public record Kitap(String isbn, String ad, String yazar) {

    public Kitap {
        for (String alan : new String[]{isbn, ad, yazar}) {
            if (alan == null || alan.isBlank() || alan.contains(";")) {
                throw new IllegalArgumentException("Geçersiz kitap alanı: " + alan);   // ';' dosyada ayırıcı
            }
        }
    }
}
