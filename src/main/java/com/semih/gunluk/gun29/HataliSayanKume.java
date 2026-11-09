package com.semih.gunluk.gun29;

import java.io.Serial;
import java.util.Collection;
import java.util.HashSet;

/**
 * <b>Bilerek hatalı</b>: "kaç eleman eklenmeye çalışıldı?" sayacı, {@link HashSet}'ten türeyerek.
 * {@code addAll(3 eleman)} sonrası sayaç 6 gösteriyor: {@code HashSet.addAll} içeride her eleman
 * için {@code add}'i çağırıyor ve o da bizim ezdiğimiz {@code add}. Bu, üst sınıfın kendi
 * içinde nasıl çalıştığına (dokümante edilmemiş bir ayrıntıya) bağımlı olmak demek; JDK
 * sürümü değişse davranış değişebilir. Buna "kırılgan üst sınıf" sorunu deniyor.
 */
public class HataliSayanKume<E> extends HashSet<E> {

    @Serial
    private static final long serialVersionUID = 1L;

    private int denemeSayisi;

    @Override
    public boolean add(E e) {
        denemeSayisi++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        denemeSayisi += c.size();
        return super.addAll(c);
    }

    public int denemeSayisi() {
        return denemeSayisi;
    }
}
