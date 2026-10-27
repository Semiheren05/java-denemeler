package com.semih.gunluk.gun15;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ürün stoklarını tutan depo. Stok değişince olay yayınlıyor; kimin dinlediğini bilmiyor.
 * E-posta göndermek, paneli güncellemek ya da tedarikçiye sipariş açmak deponun işi değil.
 */
public class Depo {

    private final Map<String, Integer> stoklar = new HashMap<>();
    private final OlayYayini<StokOlayi> olaylar;
    private final int kritikSeviye;

    public Depo(OlayYayini<StokOlayi> olaylar, int kritikSeviye) {
        if (kritikSeviye < 1) {
            throw new IllegalArgumentException("Kritik seviye en az 1 olmalı: " + kritikSeviye);
        }
        this.olaylar = olaylar;
        this.kritikSeviye = kritikSeviye;
    }

    public void ekle(String urun, int adet) {
        if (adet <= 0) {
            throw new IllegalArgumentException("Adet pozitif olmalı: " + adet);
        }
        int yeni = stoklar.merge(urun, adet, Integer::sum);
        olaylar.yayinla(new StokOlayi.StokEklendi(urun, adet, yeni));
    }

    /**
     * Stoktan düşer. Satış önce kaydediliyor, olaylar sonra yayınlanıyor: bir dinleyici hata
     * verse bile satış geçerli.
     *
     * @return dinleyicilerin hataları; satışın kendisi başarılı
     * @throws IllegalStateException stok yetmiyorsa (hiçbir şey değişmez)
     */
    public List<RuntimeException> sat(String urun, int adet) {
        if (adet <= 0) {
            throw new IllegalArgumentException("Adet pozitif olmalı: " + adet);
        }
        int onceki = stok(urun);
        if (adet > onceki) {
            throw new IllegalStateException(urun + " için yeterli stok yok: " + onceki + " var, " + adet + " istendi");
        }
        int kalan = onceki - adet;
        stoklar.put(urun, kalan);

        if (kalan == 0) {
            return olaylar.yayinla(new StokOlayi.StokBitti(urun));
        }
        // Sadece eşiğin altına inildiği an: 4'ten 3'e inerken değil, 6'dan 4'e inerken
        if (onceki >= kritikSeviye && kalan < kritikSeviye) {
            return olaylar.yayinla(new StokOlayi.StokAzaldi(urun, kalan, kritikSeviye));
        }
        return List.of();
    }

    public int stok(String urun) {
        return stoklar.getOrDefault(urun, 0);
    }
}
