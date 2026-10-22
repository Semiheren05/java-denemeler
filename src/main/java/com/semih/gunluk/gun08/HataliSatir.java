package com.semih.gunluk.gun08;

/**
 * Okunamayan bir satır ve nedeni.
 *
 * @param satirNo dosyadaki satır numarası, 1'den başlar (başlık satırı 1)
 */
public record HataliSatir(int satirNo, String icerik, String neden) {
}
