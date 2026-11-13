package com.semih.gunluk.gun32;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Supplier;

/** Aynı soru iki yazımla: iç içe {@code null} kontrolleri ve {@code Optional} zinciri. */
public class KargoAdresi {

    public static final String BILINMIYOR = "BILINMIYOR";

    private final MusteriDeposu depo;

    public KargoAdresi(MusteriDeposu depo) {
        this.depo = depo;
    }

    /** Eski yazım: her adımda {@code null} kontrolü, okunan şey iş kuralı değil savunma. */
    public String sehirEski(String musteriId) {
        Musteri musteri = depo.bulYaDaNull(musteriId);
        if (musteri != null) {
            Adres adres = musteri.adresYaDaNull();
            if (adres != null) {
                String sehir = adres.sehir();
                if (sehir != null && !sehir.isBlank()) {
                    return sehir;
                }
            }
        }
        return BILINMIYOR;
    }

    /** Yeni yazım: {@code flatMap} Optional döndüren adımı, {@code map} düz değeri bağlıyor. */
    public String sehir(String musteriId) {
        return depo.bul(musteriId)
                .flatMap(Musteri::adres)
                .map(Adres::sehir)
                .filter(sehir -> !sehir.isBlank())
                .orElse(BILINMIYOR);
    }

    /** Bulunamaması bir hata ise boş dönmek yerine anlamlı bir istisna. */
    public Musteri zorunluBul(String musteriId) {
        return depo.bul(musteriId).orElseThrow(() -> new MusteriBulunamadi(musteriId));
    }

    /** {@code or}: ilk kaynak boşsa ikinciye <em>ancak o zaman</em> gidiliyor (tembel). */
    public Optional<Musteri> onbellektenYaDaDepodan(Optional<Musteri> onbellek, String musteriId) {
        return onbellek.or(() -> depo.bul(musteriId));
    }

    /** {@code Optional::stream} boşları atıp dolu olanları açıyor. */
    public List<String> bulunanAdlar(List<String> idler) {
        return idler.stream()
                .map(depo::bul)
                .flatMap(Optional::stream)
                .map(Musteri::ad)
                .toList();
    }

    /** İlkel değer için {@code OptionalInt}: kutulama ({@code Integer}) yok. */
    public static OptionalInt enYuksekPuan(List<Integer> puanlar) {
        return puanlar.stream().mapToInt(Integer::intValue).max();
    }

    /** {@code orElse} argümanı her durumda hesaplanır; {@code orElseGet} sadece boşsa. */
    public static Adres varsayilanla(Optional<Adres> adres, Supplier<Adres> varsayilan, boolean tembel) {
        return tembel ? adres.orElseGet(varsayilan) : adres.orElse(varsayilan.get());
    }
}
