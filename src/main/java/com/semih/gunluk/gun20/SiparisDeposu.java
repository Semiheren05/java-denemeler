package com.semih.gunluk.gun20;

import java.math.BigDecimal;
import java.util.List;

/** Servis sadece bu arayüzü biliyor; hangi depoyla çalışacağına kapsayıcı karar veriyor. */
public interface SiparisDeposu {

    Siparis kaydet(String musteri, BigDecimal tutar);

    List<Siparis> hepsi();
}
