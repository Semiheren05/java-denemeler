package com.semih.gunluk.gun05;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Elemanlarını her zaman küçükten büyüğe sıralı tutan, tipli bir liste.
 *
 * <p>{@code <T extends Comparable<? super T>>} sınırı, yalnızca kendi aralarında
 * karşılaştırılabilen tiplerin kabul edildiğini derleyiciye söyler. {@code ? super T}
 * kısmı sayesinde karşılaştırmayı üst sınıfından miras alan tipler de kullanılabilir.
 *
 * <p>Ekleme ikili arama ile doğru yere yapılır, böylece liste hiçbir zaman yeniden
 * sıralanmaz.
 */
public class SiraliListe<T extends Comparable<? super T>> implements Iterable<T> {

    private final List<T> elemanlar = new ArrayList<>();

    public void ekle(T eleman) {
        Objects.requireNonNull(eleman, "Eleman boş olamaz");
        int indeks = Collections.binarySearch(elemanlar, eleman);
        // Bulunamazsa binarySearch -(eklenecek yer) - 1 döner
        int yer = indeks >= 0 ? indeks : -indeks - 1;
        elemanlar.add(yer, eleman);
    }

    /** {@code ? extends T}: T'nin alt tiplerinden oluşan her koleksiyon verilebilir. */
    public void hepsiniEkle(Collection<? extends T> yeniler) {
        for (T eleman : yeniler) {
            ekle(eleman);
        }
    }

    public boolean iceriyor(T eleman) {
        return eleman != null && Collections.binarySearch(elemanlar, eleman) >= 0;
    }

    public Optional<T> enKucuk() {
        return elemanlar.isEmpty() ? Optional.empty() : Optional.of(elemanlar.getFirst());
    }

    public Optional<T> enBuyuk() {
        return elemanlar.isEmpty() ? Optional.empty() : Optional.of(elemanlar.getLast());
    }

    public T get(int indeks) {
        return elemanlar.get(indeks);
    }

    public int boyut() {
        return elemanlar.size();
    }

    /** Değiştirilemez kopya döner; dışarıdan eklenen eleman sıralamayı bozamaz. */
    public List<T> listele() {
        return List.copyOf(elemanlar);
    }

    @Override
    public Iterator<T> iterator() {
        return listele().iterator();
    }
}
