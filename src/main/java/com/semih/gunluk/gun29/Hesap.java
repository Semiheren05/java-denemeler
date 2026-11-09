package com.semih.gunluk.gun29;

import java.math.BigDecimal;

/**
 * <b>Abstract class</b>: ortak durum (bakiye) ve ortak akış burada, değişen tek parça alt
 * sınıflarda. Bir interface bunu yapamazdı: alanı ve constructor'ı yok.
 *
 * <p>{@link #paraCek} bir <b>şablon metot</b> (template method): adımların sırası sabit ve
 * {@code final}, alt sınıf sırayı bozamıyor; sadece {@link #cekimUcreti} kancasını dolduruyor.
 * "Hollywood prensibi": alt sınıf üst sınıfı çağırmıyor, üst sınıf alt sınıfı çağırıyor.
 */
public abstract class Hesap {

    private final String no;
    private BigDecimal bakiye = BigDecimal.ZERO;

    protected Hesap(String no) {
        this.no = no;
    }

    public void paraYatir(BigDecimal tutar) {
        if (tutar.signum() <= 0) {
            throw new IllegalArgumentException("Tutar pozitif olmalı: " + tutar);
        }
        bakiye = bakiye.add(tutar);
    }

    /** @return hesaptan düşen toplam (tutar + ücret) */
    public final BigDecimal paraCek(BigDecimal tutar) {
        if (tutar.signum() <= 0) {
            throw new IllegalArgumentException("Tutar pozitif olmalı: " + tutar);
        }
        BigDecimal toplam = tutar.add(cekimUcreti(tutar));
        if (toplam.compareTo(bakiye) > 0) {
            throw new IllegalStateException(no + ": yetersiz bakiye (" + bakiye + " var, " + toplam + " gerekli)");
        }
        bakiye = bakiye.subtract(toplam);
        return toplam;
    }

    /**
     * Alt sınıfın doldurduğu tek adım. {@code protected}: alt sınıflar görür, dışarıdaki kod görmez.
     */
    protected abstract BigDecimal cekimUcreti(BigDecimal tutar);

    public BigDecimal bakiye() {
        return bakiye;
    }

    public String no() {
        return no;
    }
}
