package com.semih.gunluk.gun18;

/** Doğrulanmış ve düzenlenmiş üye bilgileri. Bir {@code Uye} varsa bilgileri geçerlidir. */
public record Uye(String ad, String tcKimlikNo, String iban, String telefon, int dogumYili) {
}
