package com.semih.gunluk.gun29;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Doğrusu: <b>bileşim</b>. Bir kümeyi içinde tutuyor ve işi ona iletiyor (forwarding). İçteki
 * kümenin nasıl çalıştığı umurunda değil; {@code addAll} onun içinde ne çağırırsa çağırsın bizim
 * {@code ekle}'mize dokunmuyor. İçteki küme de değiştirilebilir: {@code HashSet}, {@code TreeSet}...
 */
public class SayanKume<E> {

    private final Set<E> kume;
    private int denemeSayisi;

    public SayanKume() {
        this(new HashSet<>());
    }

    public SayanKume(Set<E> kume) {
        this.kume = kume;
    }

    public boolean ekle(E e) {
        denemeSayisi++;
        return kume.add(e);
    }

    public boolean hepsiniEkle(Collection<? extends E> c) {
        denemeSayisi += c.size();
        return kume.addAll(c);
    }

    public boolean iceriyorMu(E e) {
        return kume.contains(e);
    }

    public int boyut() {
        return kume.size();
    }

    public int denemeSayisi() {
        return denemeSayisi;
    }
}
