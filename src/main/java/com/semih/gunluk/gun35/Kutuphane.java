package com.semih.gunluk.gun35;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Küçük bir kütüphane: kitap ve ödünç kayıtları bellekte, kurallar burada, zaman {@link Clock}'tan.
 * Kurallar: üye başına en fazla 3 kitap, 14 gün süre, bir kez 7 gün uzatma, gecikmiş kitabı olan
 * yeni kitap alamaz, ceza günde 5 TL ve en fazla 100 TL.
 */
public class Kutuphane {

    static final int UYE_SINIRI = 3;
    static final int ODUNC_GUNU = 14;
    static final int UZATMA_GUNU = 7;
    static final BigDecimal GUNLUK_CEZA = new BigDecimal("5");
    static final BigDecimal CEZA_TAVANI = new BigDecimal("100");

    private final Clock saat;
    private final Map<String, Kitap> kitaplar = new LinkedHashMap<>();
    private final List<Odunc> oduncler = new ArrayList<>();

    public Kutuphane(Clock saat) {
        this.saat = saat;
    }

    public Kutuphane ekle(Kitap kitap) {
        kitaplar.put(kitap.isbn(), kitap);
        return this;
    }

    public Odunc oduncVer(String isbn, String uyeNo) {
        Kitap kitap = kitap(isbn);
        if (acikOdunc(isbn).isPresent()) {
            throw new KutuphaneHatasi("'" + kitap.ad() + "' şu an başka bir üyede");
        }
        List<Odunc> uyedekiler = oduncler.stream().filter(o -> o.acik() && o.uyeNo().equals(uyeNo)).toList();
        if (uyedekiler.size() >= UYE_SINIRI) {
            throw new KutuphaneHatasi(uyeNo + " en fazla " + UYE_SINIRI + " kitap alabilir");
        }
        if (uyedekiler.stream().anyMatch(o -> o.gecikmeGunu(bugun()) > 0)) {
            throw new KutuphaneHatasi(uyeNo + " önce geciken kitabını iade etmeli");
        }
        Odunc odunc = new Odunc(isbn, uyeNo, bugun(), bugun().plusDays(ODUNC_GUNU), false, null);
        oduncler.add(odunc);
        return odunc;
    }

    /** @return gecikme cezası (gecikme yoksa 0) */
    public BigDecimal iadeEt(String isbn) {
        Odunc odunc = acikOdunc(isbn).orElseThrow(() -> new KutuphaneHatasi(isbn + " ödünçte değil"));
        Odunc iadeli = odunc.iadeEdildi(bugun());
        oduncler.set(oduncler.indexOf(odunc), iadeli);
        BigDecimal ceza = GUNLUK_CEZA.multiply(BigDecimal.valueOf(iadeli.gecikmeGunu(bugun())));
        return ceza.min(CEZA_TAVANI);
    }

    public Odunc uzat(String isbn) {
        Odunc odunc = acikOdunc(isbn).orElseThrow(() -> new KutuphaneHatasi(isbn + " ödünçte değil"));
        if (odunc.uzatildi()) {
            throw new KutuphaneHatasi("Süre sadece bir kez uzatılabilir");
        }
        if (odunc.gecikmeGunu(bugun()) > 0) {
            throw new KutuphaneHatasi("Gecikmiş ödünç uzatılamaz");
        }
        Odunc uzatilmis = odunc.uzatildi(UZATMA_GUNU);
        oduncler.set(oduncler.indexOf(odunc), uzatilmis);
        return uzatilmis;
    }

    public boolean musaitMi(String isbn) {
        kitap(isbn);
        return acikOdunc(isbn).isEmpty();
    }

    /** Geciken açık ödünçler, en çok geciken önce. */
    public List<Odunc> gecikenler() {
        LocalDate bugun = bugun();
        return oduncler.stream()
                .filter(o -> o.acik() && o.gecikmeGunu(bugun) > 0)
                .sorted(Comparator.comparingLong((Odunc o) -> o.gecikmeGunu(bugun)).reversed())
                .toList();
    }

    /** Yazar → ödünç sayısı, çoktan aza; eşitlikte yazar adına göre. */
    public List<Map.Entry<String, Long>> enCokOkunanYazarlar(int adet) {
        return oduncler.stream()
                .collect(Collectors.groupingBy(o -> kitaplar.get(o.isbn()).yazar(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(adet)
                .toList();
    }

    public Collection<Kitap> kitaplar() {
        return List.copyOf(kitaplar.values());
    }

    public List<Odunc> oduncler() {
        return List.copyOf(oduncler);
    }

    /** Dosyadan yüklerken kayıtları kurallara takılmadan geri koymak için. */
    void kayitEkle(Odunc odunc) {
        oduncler.add(odunc);
    }

    private Kitap kitap(String isbn) {
        Kitap kitap = kitaplar.get(isbn);
        if (kitap == null) {
            throw new KutuphaneHatasi("Kitap yok: " + isbn);
        }
        return kitap;
    }

    private Optional<Odunc> acikOdunc(String isbn) {
        return oduncler.stream().filter(o -> o.acik() && o.isbn().equals(isbn)).findFirst();
    }

    private LocalDate bugun() {
        return LocalDate.now(saat);
    }
}
