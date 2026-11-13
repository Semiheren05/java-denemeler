package com.semih.gunluk.gun33;

/** Kredi başvurusu. Tutarlar tam TL; örnek sade kalsın diye {@code int}. */
public record Basvuru(String ad, int yas, int aylikGelir, int krediNotu, int aylikBorc, int calismaYili, int istenenTutar) {
}
