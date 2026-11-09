package com.semih.gunluk.gun28;

import java.util.List;

/**
 * <b>Bilerek hatalı:</b> dışarıdan gelen listeyi kopyalamadan saklıyor. Çağıranın elindeki
 * değişken ile bu alan <em>aynı</em> heap nesnesini gösteriyor (aliasing); çağıran listeyi
 * sonradan değiştirirse kampanya da habersizce değişiyor.
 */
public class HataliKampanya {

    private final List<String> urunler;

    public HataliKampanya(List<String> urunler) {
        this.urunler = urunler;
    }

    public boolean kapsiyorMu(String urun) {
        return urunler.contains(urun);
    }
}
