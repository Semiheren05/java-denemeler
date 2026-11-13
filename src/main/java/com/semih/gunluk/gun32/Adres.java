package com.semih.gunluk.gun32;

/** {@code postaKodu} bazı eski kayıtlarda boş; {@code sehir} hiç {@code null} olmasın diye kurucuda kontrol yok, bilerek: veri kirli. */
public record Adres(String sehir, String postaKodu) {
}
