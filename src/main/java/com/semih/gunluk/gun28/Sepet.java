package com.semih.gunluk.gun28;

import java.util.ArrayList;
import java.util.List;

/** Değiştirilebilir bir alışveriş sepeti: içi değişebilen bir nesne örneği. */
public class Sepet {

    private final List<String> urunler = new ArrayList<>();

    public void ekle(String urun) {
        urunler.add(urun);
    }

    public void temizle() {
        urunler.clear();
    }

    public List<String> urunler() {
        return List.copyOf(urunler);
    }
}
