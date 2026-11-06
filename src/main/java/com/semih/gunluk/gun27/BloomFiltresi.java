package com.semih.gunluk.gun27;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

/**
 * "Bu kullanıcı adı alınmış mı?" sorusuna veritabanına gitmeden, çok az bellekle cevap veren
 * olasılıklı bir küme. İki tür cevap veriyor:
 * <ul>
 *   <li>{@code false}: <b>kesinlikle yok</b>. Yanlış negatif hiç olmaz.</li>
 *   <li>{@code true}: <b>muhtemelen var</b>. Ara sıra (ayarlanan oranda) yanılır; o zaman
 *       veritabanına sormak gerekir.</li>
 * </ul>
 *
 * <p>Nasıl: m bitlik bir dizi ve k farklı hash. Eklerken kelimenin k hash'inin gösterdiği k bit 1
 * yapılıyor. Sorarken k bitin hepsi 1 mi diye bakılıyor; biri bile 0'sa o kelime hiç eklenmemiş.
 * Hepsi 1 ise ya eklenmiş ya da başka kelimeler o bitleri tesadüfen 1 yapmış (yanlış pozitif).
 * Kelimenin kendisi hiç saklanmıyor; silmek de bu yüzden mümkün değil.
 */
public final class BloomFiltresi {

    private static final double LN2 = Math.log(2);

    private final BitSet bitler;
    private final int bitSayisi;
    private final int hashSayisi;
    private int eklenen;

    private BloomFiltresi(int bitSayisi, int hashSayisi) {
        this.bitSayisi = bitSayisi;
        this.hashSayisi = hashSayisi;
        this.bitler = new BitSet(bitSayisi);
    }

    /**
     * Beklenen eleman sayısı ve istenen yanlış pozitif oranı için en uygun boyut:
     * <pre>
     * m = −n · ln(p) / (ln 2)²     (bit sayısı)
     * k = (m / n) · ln 2           (hash sayısı)
     * </pre>
     * Örnek: 1.000 eleman, %1 yanılma → 9.586 bit (1,2 KB) ve 7 hash.
     */
    public static BloomFiltresi icin(int beklenenEleman, double yanlisPozitifOrani) {
        if (beklenenEleman < 1) {
            throw new IllegalArgumentException("Beklenen eleman sayısı en az 1 olmalı: " + beklenenEleman);
        }
        if (!(yanlisPozitifOrani > 0 && yanlisPozitifOrani < 1)) {
            throw new IllegalArgumentException("Yanlış pozitif oranı 0 ile 1 arasında olmalı: " + yanlisPozitifOrani);
        }
        int m = (int) Math.ceil(-beklenenEleman * Math.log(yanlisPozitifOrani) / (LN2 * LN2));
        int k = Math.max(1, (int) Math.round((double) m / beklenenEleman * LN2));
        return new BloomFiltresi(m, k);
    }

    public void ekle(String kelime) {
        long hash = hash64(kelime);
        for (int i = 0; i < hashSayisi; i++) {
            bitler.set(konum(hash, i));
        }
        eklenen++;
    }

    /** @return {@code false} ise kesinlikle eklenmemiş; {@code true} ise muhtemelen eklenmiş */
    public boolean belkiVar(String kelime) {
        long hash = hash64(kelime);
        for (int i = 0; i < hashSayisi; i++) {
            if (!bitler.get(konum(hash, i))) {
                return false;
            }
        }
        return true;
    }

    /** Şu ana kadar eklenenlere göre beklenen yanılma oranı: (1 − e^(−k·n/m))^k. */
    public double tahminiYanlisPozitifOrani() {
        return Math.pow(1 - Math.exp(-(double) hashSayisi * eklenen / bitSayisi), hashSayisi);
    }

    /** 1 olan bitlerin oranı. Yarıyı çok geçmesi, filtrenin taşmaya başladığını gösterir. */
    public double dolulukOrani() {
        return (double) bitler.cardinality() / bitSayisi;
    }

    /**
     * İki filtrenin birleşimi: bitlerin VEYA'sı. İkisinin boyutu ve hash sayısı aynı olmalı;
     * yoksa aynı kelime iki filtrede farklı bitlere düşer.
     */
    public BloomFiltresi birlestir(BloomFiltresi diger) {
        if (bitSayisi != diger.bitSayisi || hashSayisi != diger.hashSayisi) {
            throw new IllegalArgumentException("Sadece aynı boyutta filtreler birleştirilebilir");
        }
        BloomFiltresi sonuc = new BloomFiltresi(bitSayisi, hashSayisi);
        sonuc.bitler.or(bitler);
        sonuc.bitler.or(diger.bitler);
        sonuc.eklenen = eklenen + diger.eklenen;   // ortak kelimeler iki kez sayılır; tahmin biraz kötümser
        return sonuc;
    }

    public int bitSayisi() {
        return bitSayisi;
    }

    public int hashSayisi() {
        return hashSayisi;
    }

    /**
     * k ayrı hash fonksiyonu yazmak yerine tek bir 64 bitlik hash'in iki yarısından k tane
     * türetiliyor: h1 + i · h2 (Kirsch–Mitzenmacher). Araştırmalar bunun k bağımsız hash kadar
     * iyi sonuç verdiğini gösteriyor.
     */
    private int konum(long hash, int i) {
        int h1 = (int) hash;
        int h2 = (int) (hash >>> 32);
        return Math.floorMod(h1 + i * h2, bitSayisi);
    }

    /**
     * FNV-1a (basit ve hızlı), ardından bitleri iyice karıştıran bir son adım (SplitMix64).
     * {@code String.hashCode()} 32 bit ve "Aa" ile "BB" gibi kolay çakışıyor; burada yetmez.
     */
    static long hash64(String kelime) {
        long h = 0xcbf29ce484222325L;
        for (byte b : kelime.getBytes(StandardCharsets.UTF_8)) {
            h ^= b & 0xff;
            h *= 0x100000001b3L;
        }
        h = (h ^ (h >>> 30)) * 0xbf58476d1ce4e5b9L;
        h = (h ^ (h >>> 27)) * 0x94d049bb133111ebL;
        return h ^ (h >>> 31);
    }
}
