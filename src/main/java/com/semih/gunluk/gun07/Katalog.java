package com.semih.gunluk.gun07;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Kitap kataloğu. Kitaplar bir {@link HashSet} içinde tutulur; aynı ISBN'li kitap
 * ikinci kez eklenemez çünkü {@link Kitap#equals} ve {@link Kitap#hashCode} doğru yazıldı.
 */
public class Katalog {

    private final Set<Kitap> kitaplar = new HashSet<>();

    /** @return kitap yeni eklendiyse true, aynı ISBN zaten varsa false */
    public boolean ekle(Kitap kitap) {
        if (kitap == null) {
            throw new IllegalArgumentException("Kitap boş olamaz");
        }
        return kitaplar.add(kitap);
    }

    public boolean cikar(String isbn) {
        return bul(isbn).map(kitaplar::remove).orElse(false);
    }

    public boolean iceriyor(Kitap kitap) {
        return kitaplar.contains(kitap);
    }

    /** Arama ISBN'in tireli ya da tiresiz yazılmasından etkilenmez. */
    public Optional<Kitap> bul(String isbn) {
        String aranan = isbn == null ? "" : isbn.replaceAll("[\\s-]", "");
        return kitaplar.stream()
                .filter(k -> k.getIsbn().equals(aranan))
                .findFirst();
    }

    public List<Kitap> sirala(Comparator<Kitap> siralama) {
        return kitaplar.stream()
                .sorted(siralama)
                .toList();
    }

    /** Yazarlar alfabetik, her yazarın kitapları yıla göre. */
    public Map<String, List<Kitap>> yazaraGore() {
        return kitaplar.stream()
                .sorted(Kitap.YILA_GORE)
                .collect(Collectors.groupingBy(Kitap::getYazar, TreeMap::new, Collectors.toList()));
    }

    public int boyut() {
        return kitaplar.size();
    }

    public void hepsiniEkle(Collection<Kitap> yeniler) {
        yeniler.forEach(this::ekle);
    }
}
