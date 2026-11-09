package com.semih.gunluk.gun28;

import java.util.List;

/**
 * Doğrusu: savunmacı kopya. {@code final} sadece alanın başka bir listeye yönlendirilmesini
 * engelliyor, listenin içinin değişmesini değil; o yüzden kopyalamak şart.
 */
public class Kampanya {

    private final List<String> urunler;

    public Kampanya(List<String> urunler) {
        this.urunler = List.copyOf(urunler);
    }

    public boolean kapsiyorMu(String urun) {
        return urunler.contains(urun);
    }
}
