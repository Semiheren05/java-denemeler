package com.semih.gunluk.gun22;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * En son kullanılanı tutan, sınırlı boyutlu önbellek (LRU: Least Recently Used). Dolunca en
 * uzun süredir kullanılmayan kayıt atılıyor.
 *
 * <p>İki yapı birlikte çalışıyor:
 * <ul>
 *   <li>{@link HashMap}: anahtardan düğüme O(1) erişim.</li>
 *   <li>Çift yönlü bağlı liste: kullanım sırası. Baş en yeni, son en eski. Bir düğümü
 *       yerinden çıkarıp başa koymak, komşularının işaretçilerini değiştirmekten ibaret: O(1).</li>
 * </ul>
 * Sadece liste olsaydı bir anahtarı bulmak O(n), sadece map olsaydı en eskiyi bulmak O(n) olurdu.
 *
 * <p>Thread güvenli değil; tek thread'den kullanılmak için.
 */
public class LruOnbellek<K, V> {

    private final int kapasite;
    private final Map<K, Dugum<K, V>> dugumler = new HashMap<>();
    private final BiConsumer<K, V> atilinca;
    // Bekçi (sentinel) düğümler: liste hiç boş olmuyor, baş/son için özel durum yazmak gerekmiyor
    private final Dugum<K, V> bas = new Dugum<>(null, null);
    private final Dugum<K, V> son = new Dugum<>(null, null);
    private long isabet;
    private long kacan;

    public LruOnbellek(int kapasite) {
        this(kapasite, (anahtar, deger) -> { });
    }

    /** @param atilinca kayıt kapasite yüzünden atıldığında çağrılır (ör. dosyayı kapatmak için) */
    public LruOnbellek(int kapasite, BiConsumer<K, V> atilinca) {
        if (kapasite < 1) {
            throw new IllegalArgumentException("Kapasite en az 1 olmalı: " + kapasite);
        }
        this.kapasite = kapasite;
        this.atilinca = atilinca;
        bas.sonraki = son;
        son.onceki = bas;
    }

    public Optional<V> getir(K anahtar) {
        Dugum<K, V> dugum = dugumler.get(anahtar);
        if (dugum == null) {
            kacan++;
            return Optional.empty();
        }
        isabet++;
        basaTasi(dugum);
        return Optional.of(dugum.deger);
    }

    public void koy(K anahtar, V deger) {
        Objects.requireNonNull(anahtar, "Anahtar boş olamaz");
        Objects.requireNonNull(deger, "Değer boş olamaz");
        Dugum<K, V> mevcut = dugumler.get(anahtar);
        if (mevcut != null) {
            mevcut.deger = deger;
            basaTasi(mevcut);
            return;
        }
        if (dugumler.size() == kapasite) {
            Dugum<K, V> enEski = son.onceki;
            cikar(enEski);
            dugumler.remove(enEski.anahtar);
            atilinca.accept(enEski.anahtar, enEski.deger);
        }
        Dugum<K, V> yeni = new Dugum<>(anahtar, deger);
        basaEkle(yeni);
        dugumler.put(anahtar, yeni);
    }

    public int boyut() {
        return dugumler.size();
    }

    /** Anahtarlar en yeniden en eskiye. */
    public List<K> kullanimSirasi() {
        List<K> sira = new ArrayList<>();
        for (Dugum<K, V> d = bas.sonraki; d != son; d = d.sonraki) {
            sira.add(d.anahtar);
        }
        return sira;
    }

    /** İsabet oranı: isteklerin ne kadarı önbellekten karşılandı (0.0–1.0). */
    public double isabetOrani() {
        long toplam = isabet + kacan;
        return toplam == 0 ? 0 : (double) isabet / toplam;
    }

    private void basaTasi(Dugum<K, V> dugum) {
        cikar(dugum);
        basaEkle(dugum);
    }

    private void cikar(Dugum<K, V> dugum) {
        dugum.onceki.sonraki = dugum.sonraki;
        dugum.sonraki.onceki = dugum.onceki;
    }

    private void basaEkle(Dugum<K, V> dugum) {
        dugum.sonraki = bas.sonraki;
        dugum.onceki = bas;
        bas.sonraki.onceki = dugum;
        bas.sonraki = dugum;
    }

    private static final class Dugum<K, V> {
        final K anahtar;
        V deger;
        Dugum<K, V> onceki;
        Dugum<K, V> sonraki;

        Dugum(K anahtar, V deger) {
            this.anahtar = anahtar;
            this.deger = deger;
        }
    }
}
