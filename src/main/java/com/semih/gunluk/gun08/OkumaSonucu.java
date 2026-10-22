package com.semih.gunluk.gun08;

import java.util.List;

/** Dosyadan okunan geçerli ürünler ve atlanan hatalı satırlar. */
public record OkumaSonucu(List<Urun> urunler, List<HataliSatir> hatalar) {

    public OkumaSonucu {
        urunler = List.copyOf(urunler);
        hatalar = List.copyOf(hatalar);
    }

    public boolean hatasizMi() {
        return hatalar.isEmpty();
    }
}
