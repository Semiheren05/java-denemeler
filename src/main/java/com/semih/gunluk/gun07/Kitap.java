package com.semih.gunluk.gun07;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

/**
 * Bir kitap. Kimliği ISBN'dir: başlığı ya da baskı yılı farklı yazılmış olsa bile
 * aynı ISBN'li iki nesne aynı kitaptır.
 *
 * <p>Bilerek {@code record} değil, sınıf olarak yazıldı: {@code record} tüm alanlara
 * göre {@code equals} üretir, oysa burada eşitlik yalnızca ISBN'e bağlı. {@code equals}
 * ile {@code hashCode} her zaman birlikte ve aynı alanlarla yazılır; biri eksik olursa
 * {@code HashSet} ve {@code HashMap} yanlış çalışır.
 */
public final class Kitap implements Comparable<Kitap> {

    /** Türkçe alfabe sırası: "Çalıkuşu", "Cevdet Bey"den sonra, "Dokuzuncu"dan önce gelir. */
    private static final Collator TURKCE = Collator.getInstance(Locale.forLanguageTag("tr-TR"));

    public static final Comparator<Kitap> BASLIGA_GORE =
            Comparator.comparing(Kitap::getBaslik, TURKCE).thenComparing(Comparator.naturalOrder());

    public static final Comparator<Kitap> YILA_GORE =
            Comparator.comparingInt(Kitap::getYil).thenComparing(BASLIGA_GORE);

    private final String isbn;
    private final String baslik;
    private final String yazar;
    private final int yil;

    public Kitap(String isbn, String baslik, String yazar, int yil) {
        this.isbn = normalizeIsbn(isbn);
        if (baslik == null || baslik.isBlank()) {
            throw new IllegalArgumentException("Başlık boş olamaz");
        }
        if (yazar == null || yazar.isBlank()) {
            throw new IllegalArgumentException("Yazar boş olamaz");
        }
        this.baslik = baslik.trim();
        this.yazar = yazar.trim();
        this.yil = yil;
    }

    /** Tire ve boşlukları atar; 13 rakam kalmalı. "978-975-07-0137-6" → "9789750701376". */
    private static String normalizeIsbn(String isbn) {
        if (isbn == null) {
            throw new IllegalArgumentException("ISBN boş olamaz");
        }
        String rakamlar = isbn.replaceAll("[\\s-]", "");
        if (!rakamlar.matches("\\d{13}")) {
            throw new IllegalArgumentException("ISBN 13 haneli olmalı: " + isbn);
        }
        return rakamlar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Kitap diger)) {
            return false;
        }
        return isbn.equals(diger.isbn);
    }

    @Override
    public int hashCode() {
        return isbn.hashCode();
    }

    /**
     * Doğal sıralama ISBN'e göre, yani {@code equals} ile tutarlı: {@code compareTo}
     * 0 dönüyorsa {@code equals} de true döner. Bu yüzden {@code TreeSet} ile
     * {@code HashSet} aynı kitapları "aynı" sayar.
     */
    @Override
    public int compareTo(Kitap diger) {
        return isbn.compareTo(diger.isbn);
    }

    @Override
    public String toString() {
        return baslik + " (" + yazar + ", " + yil + ")";
    }

    public String getIsbn() {
        return isbn;
    }

    public String getBaslik() {
        return baslik;
    }

    public String getYazar() {
        return yazar;
    }

    public int getYil() {
        return yil;
    }
}
