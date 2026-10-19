package com.semih.gunluk.gun01;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Basit bir banka hesabı: para yatırma, çekme ve başka hesaba transfer.
 *
 * <p>Para için {@code double} yerine {@link BigDecimal} kullanılır; {@code double}
 * 0.1 + 0.2 gibi işlemlerde yuvarlama hatası verir. Tutarlar 2 ondalık basamağa
 * yuvarlanır (kuruş).
 */
public class BankaHesabi {

    private static final int KURUS_HANESI = 2;

    private final String hesapNo;
    private final String sahip;
    private BigDecimal bakiye = BigDecimal.ZERO.setScale(KURUS_HANESI);
    private final List<Islem> islemGecmisi = new ArrayList<>();

    public BankaHesabi(String hesapNo, String sahip) {
        if (hesapNo == null || hesapNo.isBlank()) {
            throw new IllegalArgumentException("Hesap numarası boş olamaz");
        }
        if (sahip == null || sahip.isBlank()) {
            throw new IllegalArgumentException("Hesap sahibi boş olamaz");
        }
        this.hesapNo = hesapNo.trim();
        this.sahip = sahip.trim();
    }

    public void paraYatir(BigDecimal tutar) {
        BigDecimal gecerliTutar = dogrula(tutar);
        bakiye = bakiye.add(gecerliTutar);
        islemGecmisi.add(new Islem(IslemTuru.YATIRMA, gecerliTutar, bakiye));
    }

    /**
     * @throws YetersizBakiyeException bakiye çekilmek istenen tutardan azsa;
     *                                 bu durumda bakiye değişmez
     */
    public void paraCek(BigDecimal tutar) {
        BigDecimal gecerliTutar = dogrula(tutar);
        if (bakiye.compareTo(gecerliTutar) < 0) {
            throw new YetersizBakiyeException(hesapNo, bakiye, gecerliTutar);
        }
        bakiye = bakiye.subtract(gecerliTutar);
        islemGecmisi.add(new Islem(IslemTuru.CEKME, gecerliTutar, bakiye));
    }

    /**
     * Önce bu hesaptan çeker, sonra hedefe yatırır. Bakiye yetmezse iki hesap da değişmez.
     */
    public void transferEt(BankaHesabi hedef, BigDecimal tutar) {
        Objects.requireNonNull(hedef, "Hedef hesap boş olamaz");
        if (hedef == this) {
            throw new IllegalArgumentException("Bir hesap kendisine transfer yapamaz");
        }
        paraCek(tutar);
        hedef.paraYatir(tutar);
    }

    private static BigDecimal dogrula(BigDecimal tutar) {
        if (tutar == null) {
            throw new IllegalArgumentException("Tutar boş olamaz");
        }
        BigDecimal yuvarlanmis = tutar.setScale(KURUS_HANESI, RoundingMode.HALF_EVEN);
        if (yuvarlanmis.signum() <= 0) {
            throw new IllegalArgumentException("Tutar sıfırdan büyük olmalı: " + tutar);
        }
        return yuvarlanmis;
    }

    public BigDecimal getBakiye() {
        return bakiye;
    }

    public String getHesapNo() {
        return hesapNo;
    }

    public String getSahip() {
        return sahip;
    }

    /** Değiştirilemez kopya döner; dışarıdan geçmişe kayıt eklenemez. */
    public List<Islem> getIslemGecmisi() {
        return List.copyOf(islemGecmisi);
    }
}
