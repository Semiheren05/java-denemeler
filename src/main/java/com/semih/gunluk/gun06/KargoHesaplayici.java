package com.semih.gunluk.gun06;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Firma adına göre kayıtlı stratejileri tutar ve gönderiler için teklif çıkarır.
 *
 * <p>Hesaplayıcının içinde hiç {@code if (firma.equals("hizli"))} yok: her firma kendi
 * stratejisini getiriyor. Yeni firma eklemek bu sınıfa dokunmadan {@link #firmaEkle}
 * ile yapılıyor (açık/kapalı prensibi).
 */
public class KargoHesaplayici {

    private final Map<String, KargoUcretStratejisi> firmalar = new LinkedHashMap<>();

    public void firmaEkle(String firma, KargoUcretStratejisi strateji) {
        if (firma == null || firma.isBlank()) {
            throw new IllegalArgumentException("Firma adı boş olamaz");
        }
        Objects.requireNonNull(strateji, "Strateji boş olamaz");
        if (firmalar.putIfAbsent(firma.trim(), strateji) != null) {
            throw new IllegalArgumentException("Bu firma zaten kayıtlı: " + firma);
        }
    }

    public Teklif teklif(String firma, Gonderi gonderi) {
        KargoUcretStratejisi strateji = firmalar.get(firma);
        if (strateji == null) {
            throw new IllegalArgumentException("Bilinmeyen firma: " + firma);
        }
        return new Teklif(firma, strateji.hesapla(gonderi));
    }

    /** Tüm firmaların teklifleri, ucuzdan pahalıya; eşit fiyatta firma adına göre. */
    public List<Teklif> tumTeklifler(Gonderi gonderi) {
        Objects.requireNonNull(gonderi, "Gönderi boş olamaz");
        return firmalar.keySet().stream()
                .map(firma -> teklif(firma, gonderi))
                .sorted(Comparator.comparing(Teklif::ucret).thenComparing(Teklif::firma))
                .toList();
    }

    public Optional<Teklif> enUcuz(Gonderi gonderi) {
        return tumTeklifler(gonderi).stream().findFirst();
    }
}
