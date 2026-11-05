package com.semih.gunluk.gun24;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Arama kutusu için otomatik tamamlama: "ist" yazılınca "istanbul", "istasyon"...
 *
 * <p>Kelimeler bir <b>Trie</b>'da (önek ağacı) tutuluyor: her düğüm bir harf, kökten bir düğüme
 * giden yol bir önek. Aynı önekle başlayan kelimeler o öneki paylaşıyor. Bir önekle başlayan
 * kelimeleri bulmak, kaç kelime olduğundan bağımsız olarak önek uzunluğu kadar adım: O(m).
 * Düz bir listede her kelimeye {@code startsWith} sormak O(n · m) olurdu.
 *
 * <p>Öneriler en çok arananlar önce geliyor. En iyi k tanesi bütün adayları sıralamadan,
 * k boyutlu bir <b>yığın</b> ({@link PriorityQueue}) ile seçiliyor: O(a · log k).
 */
public class OtomatikTamamlama {

    private static final Locale TURKCE = Locale.forLanguageTag("tr-TR");
    // En kötü öneri yığının tepesinde dursun diye: az aranan, eşitlikte alfabede sonra gelen
    private static final Comparator<Oneri> KOTUDEN_IYIYE = Comparator.comparingLong(Oneri::siklik)
            .thenComparing(Oneri::kelime, Comparator.reverseOrder());

    private final Dugum kok = new Dugum();

    /** Kelimeyi ekler; zaten varsa aranma sayısını bir artırır. */
    public void ara(String kelime) {
        String temiz = temizle(kelime);
        if (temiz.isEmpty()) {
            throw new IllegalArgumentException("Kelime boş olamaz");
        }
        boolean yeni = !iceriyorMu(temiz);
        Dugum dugum = kok;
        if (yeni) {
            kok.altKelimeSayisi++;
        }
        for (char harf : temiz.toCharArray()) {
            dugum = dugum.cocuklar.computeIfAbsent(harf, h -> new Dugum());
            if (yeni) {
                dugum.altKelimeSayisi++;   // bu önekle başlayan kelime sayısı, her düğümde hazır
            }
        }
        dugum.siklik++;
    }

    /** @return en çok arananlar önce, eşitlikte alfabetik; en fazla {@code adet} tane */
    public List<Oneri> oneriler(String onek, int adet) {
        if (adet < 1) {
            throw new IllegalArgumentException("Adet en az 1 olmalı: " + adet);
        }
        String temiz = temizle(onek);
        Dugum baslangic = bul(temiz);
        if (baslangic == null) {
            return List.of();
        }
        PriorityQueue<Oneri> enIyiler = new PriorityQueue<>(KOTUDEN_IYIYE);
        topla(baslangic, new StringBuilder(temiz), enIyiler, adet);
        List<Oneri> sonuc = new ArrayList<>(enIyiler);
        sonuc.sort(KOTUDEN_IYIYE.reversed());
        return sonuc;
    }

    public boolean iceriyorMu(String kelime) {
        Dugum dugum = bul(temizle(kelime));
        // "ist" ağaçta bir yol ama kelime değil: kelime sonu sıklığı 0'dan büyük olan düğüm
        return dugum != null && dugum.siklik > 0;
    }

    /** Bu önekle başlayan kaç farklı kelime var: O(m), alt ağacı gezmeden. */
    public int onekSayisi(String onek) {
        Dugum dugum = bul(temizle(onek));
        return dugum == null ? 0 : dugum.altKelimeSayisi;
    }

    /**
     * Kelimeyi siler. Artık hiçbir kelimenin parçası olmayan düğümler de ağaçtan atılıyor;
     * yoksa silinen kelimelerin harfleri boş dallar olarak kalırdı.
     *
     * @return kelime varsa ve silindiyse {@code true}
     */
    public boolean sil(String kelime) {
        String temiz = temizle(kelime);
        if (!iceriyorMu(temiz)) {
            return false;
        }
        kok.altKelimeSayisi--;
        Dugum dugum = kok;
        for (char harf : temiz.toCharArray()) {
            Dugum cocuk = dugum.cocuklar.get(harf);
            if (--cocuk.altKelimeSayisi == 0) {
                // Bu daldan geçen başka kelime yok: dalın geri kalanıyla birlikte kopar
                dugum.cocuklar.remove(harf);
                return true;
            }
            dugum = cocuk;
        }
        dugum.siklik = 0;
        return true;
    }

    /** Ağaçtaki düğüm sayısı, kök hariç (testler için). */
    int dugumSayisi() {
        return say(kok) - 1;
    }

    private Dugum bul(String onek) {
        Dugum dugum = kok;
        for (char harf : onek.toCharArray()) {
            dugum = dugum.cocuklar.get(harf);
            if (dugum == null) {
                return null;
            }
        }
        return dugum;
    }

    /** Derinlik öncelikli gezinti; yığında en fazla {@code adet} öneri tutuluyor. */
    private static void topla(Dugum dugum, StringBuilder yol, PriorityQueue<Oneri> enIyiler, int adet) {
        if (dugum.siklik > 0) {
            enIyiler.add(new Oneri(yol.toString(), dugum.siklik));
            if (enIyiler.size() > adet) {
                enIyiler.poll();   // tepedeki en kötüsü atılıyor
            }
        }
        for (Map.Entry<Character, Dugum> cocuk : dugum.cocuklar.entrySet()) {
            yol.append(cocuk.getKey());
            topla(cocuk.getValue(), yol, enIyiler, adet);
            yol.deleteCharAt(yol.length() - 1);
        }
    }

    private static int say(Dugum dugum) {
        int toplam = 1;
        for (Dugum cocuk : dugum.cocuklar.values()) {
            toplam += say(cocuk);
        }
        return toplam;
    }

    /** "İSTANBUL", "Istanbul" yerine tek biçim. Türkçe kurallarla: I → ı, İ → i. */
    private static String temizle(String metin) {
        if (metin == null) {
            throw new IllegalArgumentException("Metin boş olamaz");
        }
        return metin.strip().toLowerCase(TURKCE);
    }

    private static final class Dugum {
        final Map<Character, Dugum> cocuklar = new HashMap<>();
        long siklik;              // 0: burada biten bir kelime yok
        int altKelimeSayisi;      // bu düğümden geçen (bu önekle başlayan) kelime sayısı
    }
}
