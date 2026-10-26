package com.semih.gunluk.gun11;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Geri al (Ctrl+Z) ve ileri al (Ctrl+Y) destekleyen basit bir metin editörü.
 *
 * <p>İki yığın kullanıyor: yapılan düzenlemeler {@code geriAlinacaklar}'a, geri alınanlar
 * {@code ileriAlinacaklar}'a gidiyor. Yığın olarak eski {@code Stack} sınıfı değil
 * {@link ArrayDeque} var: {@code Stack}, her metodu {@code synchronized} olan eski
 * {@code Vector}'den türüyor; tek thread'li bir editörde boşuna yavaş.
 *
 * <p>Geçmiş sınırsız büyümesin diye en fazla {@code gecmisSiniri} düzenleme tutuluyor;
 * sınır aşılınca en eski düzenleme yığının dibinden atılıyor. {@code Deque} hem üstten
 * ({@code push}/{@code pop}) hem alttan ({@code removeLast}) O(1) işlem yapabildiği için
 * bu kolay.
 */
public class MetinEditoru {

    private final StringBuilder metin = new StringBuilder();
    private final Deque<Duzenleme> geriAlinacaklar = new ArrayDeque<>();
    private final Deque<Duzenleme> ileriAlinacaklar = new ArrayDeque<>();
    private final int gecmisSiniri;

    public MetinEditoru(int gecmisSiniri) {
        if (gecmisSiniri < 1) {
            throw new IllegalArgumentException("Geçmiş sınırı en az 1 olmalı: " + gecmisSiniri);
        }
        this.gecmisSiniri = gecmisSiniri;
    }

    /** Metnin sonuna yazar. */
    public void yaz(String yazi) {
        ekle(metin.length(), yazi);
    }

    public void ekle(int konum, String yazi) {
        if (yazi == null || yazi.isEmpty()) {
            throw new IllegalArgumentException("Eklenecek yazı boş olamaz");
        }
        konumuKontrolEt(konum, 0);
        yap(new Duzenleme.Ekleme(konum, yazi));
    }

    public void sil(int konum, int uzunluk) {
        if (uzunluk < 1) {
            throw new IllegalArgumentException("Silinecek uzunluk en az 1 olmalı: " + uzunluk);
        }
        konumuKontrolEt(konum, uzunluk);
        yap(new Duzenleme.Silme(konum, metin.substring(konum, konum + uzunluk)));
    }

    /** @return geri alınacak bir şey varsa {@code true} */
    public boolean geriAl() {
        if (geriAlinacaklar.isEmpty()) {
            return false;
        }
        Duzenleme son = geriAlinacaklar.pop();
        son.tersi().uygula(metin);
        ileriAlinacaklar.push(son);
        return true;
    }

    /** @return ileri alınacak bir şey varsa {@code true} */
    public boolean ileriAl() {
        if (ileriAlinacaklar.isEmpty()) {
            return false;
        }
        Duzenleme duzenleme = ileriAlinacaklar.pop();
        duzenleme.uygula(metin);
        geriAlinacaklar.push(duzenleme);
        return true;
    }

    public String metin() {
        return metin.toString();
    }

    public int geriAlinabilecekSayisi() {
        return geriAlinacaklar.size();
    }

    public int ileriAlinabilecekSayisi() {
        return ileriAlinacaklar.size();
    }

    private void yap(Duzenleme duzenleme) {
        duzenleme.uygula(metin);
        geriAlinacaklar.push(duzenleme);
        if (geriAlinacaklar.size() > gecmisSiniri) {
            geriAlinacaklar.removeLast();
        }
        // Yeni bir düzenleme, geri alınmış olanların "geleceğini" geçersiz kılar
        ileriAlinacaklar.clear();
    }

    private void konumuKontrolEt(int konum, int uzunluk) {
        if (konum < 0 || konum + uzunluk > metin.length()) {
            throw new IndexOutOfBoundsException(
                    "Geçersiz konum: " + konum + " (uzunluk " + uzunluk + ", metin " + metin.length() + " karakter)");
        }
    }
}
