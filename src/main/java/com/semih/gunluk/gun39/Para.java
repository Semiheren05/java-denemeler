package com.semih.gunluk.gun39;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

/**
 * Para bir değer nesnesi: tutar ve birim birlikte. Tutar her zaman birimin kuruş hanesi kadar
 * ölçekte ({@code TRY} 2, {@code JPY} 0) tutuluyor; böylece {@code 10.5} ile {@code 10.50} aynı nesne
 * ve {@code equals} doğru çalışıyor. Bölme ve paylaştırma kuruş kaybetmiyor.
 */
public record Para(BigDecimal tutar, Currency birim) implements Comparable<Para> {

    public static final Currency TL = Currency.getInstance("TRY");

    public Para {
        int hane = birim.getDefaultFractionDigits();
        try {
            tutar = tutar.setScale(hane, RoundingMode.UNNECESSARY);   // yuvarlama gerekiyorsa hata: sessizce kuruş kaybolmasın
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException(tutar + " " + birim + " için en fazla " + hane + " ondalık olabilir", e);
        }
    }

    public static Para tl(String tutar) {
        return new Para(new BigDecimal(tutar), TL);
    }

    public static Para of(String tutar, String birim) {
        return new Para(new BigDecimal(tutar), Currency.getInstance(birim));
    }

    public Para topla(Para diger) {
        ayniBirim(diger);
        return new Para(tutar.add(diger.tutar), birim);
    }

    public Para cikar(Para diger) {
        ayniBirim(diger);
        return new Para(tutar.subtract(diger.tutar), birim);
    }

    /** Oranla çarpınca kuruş altı hane çıkar; nasıl yuvarlanacağını çağıran söyler. */
    public Para carp(BigDecimal carpan, RoundingMode mod) {
        return new Para(tutar.multiply(carpan).setScale(birim.getDefaultFractionDigits(), mod), birim);
    }

    /**
     * Eşit paylaştır: 100 TL / 3 = 33,34 + 33,33 + 33,33. Hesap kuruş (tam sayı) üzerinden; artan
     * kuruşlar baştakilere birer birer veriliyor. Toplam her zaman ilk tutara eşit.
     */
    public List<Para> dagit(int parca) {
        if (parca < 1) {
            throw new IllegalArgumentException("Parça sayısı en az 1 olmalı");
        }
        long kurus = kurus();
        long taban = Math.floorDiv(kurus, parca);
        long artan = Math.floorMod(kurus, parca);
        return IntStream.range(0, parca).mapToObj(i -> kurustan(taban + (i < artan ? 1 : 0))).toList();
    }

    /**
     * Oranla paylaştır (ör. 50:30:20), "en büyük kalan" yöntemiyle: herkes payının aşağı yuvarlanmışını
     * alıyor, artan kuruşlar kalanı en büyük olanlardan başlayarak dağıtılıyor (eşitlikte öndeki).
     */
    public List<Para> oranlaDagit(List<Integer> oranlar) {
        int toplamOran = oranlar.stream().mapToInt(Integer::intValue).sum();
        if (toplamOran <= 0 || oranlar.stream().anyMatch(o -> o < 0)) {
            throw new IllegalArgumentException("Oranlar negatif olamaz ve toplamı pozitif olmalı: " + oranlar);
        }
        long kurus = kurus();
        long[] paylar = new long[oranlar.size()];
        long[] kalanlar = new long[oranlar.size()];
        long dagitilan = 0;
        for (int i = 0; i < paylar.length; i++) {
            long pay = kurus * oranlar.get(i);
            paylar[i] = Math.floorDiv(pay, toplamOran);
            kalanlar[i] = Math.floorMod(pay, toplamOran);
            dagitilan += paylar[i];
        }
        List<Integer> sira = new ArrayList<>(IntStream.range(0, paylar.length).boxed().toList());
        sira.sort(Comparator.comparingLong((Integer i) -> kalanlar[i]).reversed().thenComparing(i -> i));
        for (int k = 0; k < kurus - dagitilan; k++) {
            paylar[sira.get(k)]++;
        }
        return IntStream.range(0, paylar.length).mapToObj(i -> kurustan(paylar[i])).toList();
    }

    /** KDV dahil fiyattan net ve KDV'yi ayırır; ikisinin toplamı tam olarak fiyat. */
    public List<Para> kdvAyir(int kdvYuzdesi) {
        BigDecimal bolen = BigDecimal.valueOf(100 + kdvYuzdesi).movePointLeft(2);
        Para net = new Para(tutar.divide(bolen, birim.getDefaultFractionDigits(), RoundingMode.HALF_UP), birim);
        return List.of(net, cikar(net));   // KDV'yi ayrıca hesaplamak yerine farktan: kuruş farkı oluşamaz
    }

    public String bicimle(Locale yerel) {
        NumberFormat bicim = NumberFormat.getCurrencyInstance(yerel);
        bicim.setCurrency(birim);
        return bicim.format(tutar);
    }

    @Override
    public int compareTo(Para diger) {
        ayniBirim(diger);
        return tutar.compareTo(diger.tutar);
    }

    private long kurus() {
        return tutar.movePointRight(birim.getDefaultFractionDigits()).longValueExact();
    }

    private Para kurustan(long kurus) {
        return new Para(BigDecimal.valueOf(kurus, birim.getDefaultFractionDigits()), birim);
    }

    private void ayniBirim(Para diger) {
        if (!birim.equals(diger.birim)) {
            throw new FarkliParaBirimi(birim.getCurrencyCode(), diger.birim.getCurrencyCode());
        }
    }
}
