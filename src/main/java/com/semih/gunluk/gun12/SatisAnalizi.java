package com.semih.gunluk.gun12;

import java.util.Optional;

/**
 * Bir e-ticaret sitesinin satış verileri üzerinde üç klasik algoritma. Fiyatlar ve
 * satışlar kuruş cinsinden {@code int}; her metot için kaba kuvvet çözümü O(n²) olurdu,
 * buradakiler O(log n) ya da O(n).
 *
 * <ul>
 *   <li><b>İkili arama</b>: sıralı dizide her adımda aralığın yarısını eliyor.</li>
 *   <li><b>İki işaretçi</b>: sıralı dizinin iki ucundan içeri doğru yürüyor.</li>
 *   <li><b>Kayan pencere</b>: bir sonraki pencerenin toplamını baştan hesaplamak yerine
 *       giren elemanı ekleyip çıkanı çıkarıyor.</li>
 * </ul>
 */
public final class SatisAnalizi {

    private SatisAnalizi() {
    }

    /**
     * Bütçeyi aşmayan en pahalı ürünün indeksi. O(log n).
     *
     * @param siraliFiyatlar küçükten büyüğe sıralı
     * @return yoksa (en ucuz ürün bile bütçeyi aşıyorsa) -1
     */
    public static int butceyeUyanEnPahali(int[] siraliFiyatlar, int butce) {
        int sol = 0;
        int sag = siraliFiyatlar.length - 1;
        int bulunan = -1;
        while (sol <= sag) {
            // (sol + sag) / 2 çok büyük dizilerde int taşmasına yol açabilir
            int orta = sol + (sag - sol) / 2;
            if (siraliFiyatlar[orta] <= butce) {
                bulunan = orta;     // uyuyor; daha pahalısı sağda olabilir
                sol = orta + 1;
            } else {
                sag = orta - 1;
            }
        }
        return bulunan;
    }

    /**
     * Toplamı tam olarak {@code hedef} eden iki farklı ürün ("iki ürün al, hediye çekini tam
     * kullan"). O(n): her adımda işaretçilerden biri içeri kayıyor, ikisi en fazla n adım yürüyor.
     *
     * @param siraliFiyatlar küçükten büyüğe sıralı
     */
    public static Optional<FiyatCifti> hedefToplamliCift(int[] siraliFiyatlar, int hedef) {
        int sol = 0;
        int sag = siraliFiyatlar.length - 1;
        while (sol < sag) {
            int toplam = siraliFiyatlar[sol] + siraliFiyatlar[sag];
            if (toplam == hedef) {
                return Optional.of(new FiyatCifti(sol, sag, siraliFiyatlar[sol], siraliFiyatlar[sag]));
            }
            if (toplam < hedef) {
                sol++;      // toplamı büyütmenin tek yolu soldakini büyütmek
            } else {
                sag--;      // küçültmenin tek yolu sağdakini küçültmek
            }
        }
        return Optional.empty();
    }

    /** Art arda {@code gun} günün en yüksek toplam satışı. O(n), sabit pencere. */
    public static long enIyiDonemSatisi(int[] gunlukSatislar, int gun) {
        if (gun < 1 || gun > gunlukSatislar.length) {
            throw new IllegalArgumentException("Gün sayısı 1 ile " + gunlukSatislar.length + " arasında olmalı: " + gun);
        }
        long pencere = 0;
        for (int i = 0; i < gun; i++) {
            pencere += gunlukSatislar[i];
        }
        long enIyi = pencere;
        for (int i = gun; i < gunlukSatislar.length; i++) {
            pencere += gunlukSatislar[i] - gunlukSatislar[i - gun];   // biri girdi, biri çıktı
            enIyi = Math.max(enIyi, pencere);
        }
        return enIyi;
    }

    /**
     * Toplam satışın {@code hedef}e ulaştığı en kısa art arda gün sayısı. O(n), değişken
     * pencere: sağ uç ilerleyip toplamı büyütüyor, hedef tutunca sol uç ilerleyip pencereyi
     * daraltıyor. Satışlar negatif olamayacağı için daraltmak toplamı hiç büyütmüyor.
     *
     * @return hedefe hiç ulaşılmıyorsa 0
     */
    public static int hedefeEnKisaSure(int[] gunlukSatislar, long hedef) {
        if (hedef <= 0) {
            throw new IllegalArgumentException("Hedef pozitif olmalı: " + hedef);
        }
        int enKisa = Integer.MAX_VALUE;
        long toplam = 0;
        int sol = 0;
        for (int sag = 0; sag < gunlukSatislar.length; sag++) {
            if (gunlukSatislar[sag] < 0) {
                throw new IllegalArgumentException("Satış negatif olamaz: " + gunlukSatislar[sag]);
            }
            toplam += gunlukSatislar[sag];
            while (toplam >= hedef && sol <= sag) {
                enKisa = Math.min(enKisa, sag - sol + 1);
                toplam -= gunlukSatislar[sol++];
            }
        }
        return enKisa == Integer.MAX_VALUE ? 0 : enKisa;
    }
}
