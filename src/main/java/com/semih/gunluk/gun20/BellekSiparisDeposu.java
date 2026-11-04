package com.semih.gunluk.gun20;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BellekSiparisDeposu implements SiparisDeposu {

    private final List<Siparis> siparisler = new ArrayList<>();

    @Override
    public Siparis kaydet(String musteri, BigDecimal tutar) {
        Siparis siparis = new Siparis(siparisler.size() + 1L, musteri, tutar);
        siparisler.add(siparis);
        return siparis;
    }

    @Override
    public List<Siparis> hepsi() {
        return List.copyOf(siparisler);
    }
}
