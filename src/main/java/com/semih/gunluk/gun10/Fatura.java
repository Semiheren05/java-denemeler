package com.semih.gunluk.gun10;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Değiştirilemez (immutable) bir fatura. Oluşturulduktan sonra hiçbir alanı değişmez:
 * alanlar {@code final}, liste kopyalanarak saklanıyor ve dışarıya salt okunur veriliyor.
 *
 * <p>Çok alanlı, bazı alanları isteğe bağlı bir nesneyi uzun bir constructor yerine
 * {@link Builder} ile kuruyorum: {@code Fatura.builder().no(...).musteri(...).kalem(...).build()}.
 * Kurallar tek bir yerde, {@code build()} içinde kontrol ediliyor; yarım kalmış bir fatura
 * hiç oluşamıyor.
 */
public final class Fatura {

    private static final BigDecimal YUZ = BigDecimal.valueOf(100);

    private final String no;
    private final String musteri;
    private final LocalDate tarih;
    private final List<FaturaKalemi> kalemler;
    private final int indirimYuzdesi;

    private Fatura(Builder builder) {
        this.no = builder.no;
        this.musteri = builder.musteri;
        this.tarih = builder.tarih;
        // Kopya: build()'den sonra builder'a eklenen kalemler bu faturayı değiştiremez
        this.kalemler = List.copyOf(builder.kalemler);
        this.indirimYuzdesi = builder.indirimYuzdesi;
    }

    public static Builder builder() {
        return new Builder();
    }

    /** Bu faturanın bir kopyasından başlayan builder: değiştirmek yerine yenisini üretmek için. */
    public Builder toBuilder() {
        Builder builder = new Builder().no(no).musteri(musteri).tarih(tarih).indirim(indirimYuzdesi);
        builder.kalemler.addAll(kalemler);
        return builder;
    }

    /** İndirim ve KDV öncesi toplam. */
    public BigDecimal araToplam() {
        return kalemler.stream().map(FaturaKalemi::tutar).reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Her KDV oranı için indirimli matrah. İndirim önce uygulanıyor, KDV indirimli tutar
     * üzerinden hesaplanıyor; her oran ayrı yuvarlandığı için toplamlar kuruşu kuruşuna tutuyor.
     */
    public Map<KdvOrani, BigDecimal> matrahlar() {
        Map<KdvOrani, BigDecimal> matrah = new EnumMap<>(KdvOrani.class);
        for (FaturaKalemi kalem : kalemler) {
            matrah.merge(kalem.kdv(), kalem.tutar(), BigDecimal::add);
        }
        BigDecimal kalanOran = YUZ.subtract(BigDecimal.valueOf(indirimYuzdesi));
        matrah.replaceAll((oran, tutar) -> tutar.multiply(kalanOran).divide(YUZ, 2, RoundingMode.HALF_UP));
        return Collections.unmodifiableMap(matrah);
    }

    /** KDV dökümü: her oran için ödenecek vergi. */
    public Map<KdvOrani, BigDecimal> kdvDokumu() {
        Map<KdvOrani, BigDecimal> kdv = new EnumMap<>(KdvOrani.class);
        matrahlar().forEach((oran, matrah) ->
                kdv.put(oran, matrah.multiply(oran.oran()).setScale(2, RoundingMode.HALF_UP)));
        return Collections.unmodifiableMap(kdv);
    }

    public BigDecimal indirimTutari() {
        return araToplam().subtract(toplam(matrahlar()));
    }

    public BigDecimal kdvToplami() {
        return toplam(kdvDokumu());
    }

    public BigDecimal genelToplam() {
        return toplam(matrahlar()).add(kdvToplami());
    }

    public String no() {
        return no;
    }

    public String musteri() {
        return musteri;
    }

    public LocalDate tarih() {
        return tarih;
    }

    /** Salt okunur liste: {@code add} ya da {@code remove} denemesi hata verir. */
    public List<FaturaKalemi> kalemler() {
        return kalemler;
    }

    public int indirimYuzdesi() {
        return indirimYuzdesi;
    }

    private static BigDecimal toplam(Map<KdvOrani, BigDecimal> tutarlar) {
        return tutarlar.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Faturayı adım adım kurar. Her metot builder'ın kendisini döndürdüğü için çağrılar
     * zincirlenebiliyor. Builder değişebilir; ondan çıkan {@link Fatura} değişmez.
     */
    public static final class Builder {

        private String no;
        private String musteri;
        private LocalDate tarih = LocalDate.now();
        private final List<FaturaKalemi> kalemler = new ArrayList<>();
        private int indirimYuzdesi;

        private Builder() {
        }

        public Builder no(String no) {
            this.no = no;
            return this;
        }

        public Builder musteri(String musteri) {
            this.musteri = musteri;
            return this;
        }

        public Builder tarih(LocalDate tarih) {
            this.tarih = Objects.requireNonNull(tarih, "Tarih boş olamaz");
            return this;
        }

        /** @param birimFiyat metin olarak ("749.90"), double'ın yuvarlama hatalarına düşmemek için */
        public Builder kalem(String urun, String birimFiyat, int adet, KdvOrani kdv) {
            kalemler.add(new FaturaKalemi(urun, new BigDecimal(birimFiyat), adet, kdv));
            return this;
        }

        public Builder indirim(int yuzde) {
            if (yuzde < 0 || yuzde > 100) {
                throw new IllegalArgumentException("İndirim 0 ile 100 arasında olmalı: " + yuzde);
            }
            this.indirimYuzdesi = yuzde;
            return this;
        }

        /** @throws IllegalStateException zorunlu bir alan eksikse */
        public Fatura build() {
            if (no == null || no.isBlank()) {
                throw new IllegalStateException("Fatura numarası zorunlu");
            }
            if (musteri == null || musteri.isBlank()) {
                throw new IllegalStateException("Müşteri zorunlu");
            }
            if (kalemler.isEmpty()) {
                throw new IllegalStateException("Faturada en az bir kalem olmalı");
            }
            return new Fatura(this);
        }
    }
}
