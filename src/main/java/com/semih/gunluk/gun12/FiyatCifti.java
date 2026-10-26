package com.semih.gunluk.gun12;

/** İki ürünün fiyatı (kuruş) ve dizideki yerleri. */
public record FiyatCifti(int birinciIndeks, int ikinciIndeks, int birinciFiyat, int ikinciFiyat) {

    public int toplam() {
        return birinciFiyat + ikinciFiyat;
    }
}
