package com.semih.gunluk.gun38;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Birbirinden bağımsız çağrılar aynı anda başlatılıyor ve sonuçları birleştiriliyor; birbirine
 * bağlı olanlar zincirleniyor. Hiçbir yerde {@code get()} ile beklenmiyor: metotlar
 * {@code CompletableFuture} döndürüyor, beklemek çağıranın kararı.
 */
public class SeyahatPlanlayici {

    private final List<UcusServisi> havayollari;
    private final OtelServisi oteller;
    private final KurServisi kurlar;

    public SeyahatPlanlayici(List<UcusServisi> havayollari, OtelServisi oteller, KurServisi kurlar) {
        this.havayollari = List.copyOf(havayollari);
        this.oteller = oteller;
        this.kurlar = kurlar;
    }

    /** Otel ve TL karşılığı birlikte taşınıyor. */
    private record OtelFiyati(Otel otel, BigDecimal toplamTl) {
    }

    /**
     * Uçuş ve otel <b>paralel</b> ({@code thenCombine}). Kur çağrısı otelin para birimine bağlı,
     * yani otelden <b>sonra</b> ({@code thenCompose}: future döndüren bir adımı zincire düz bağlıyor;
     * {@code thenApply} olsaydı sonuç {@code CompletableFuture<CompletableFuture<...>>} olurdu).
     */
    public CompletableFuture<Paket> paket(String nereden, String nereye, int gece) {
        CompletableFuture<Ucus> ucus = enUcuzUcus(nereden, nereye);
        CompletableFuture<OtelFiyati> otel = oteller.ara(nereye)
                .thenCompose(o -> kurlar.tlKarsiligi(o.paraBirimi())
                        .thenApply(kur -> new OtelFiyati(o, o.geceFiyati().multiply(kur).multiply(BigDecimal.valueOf(gece)))));
        return ucus.thenCombine(otel, (u, of) ->
                new Paket(u, of.otel(), gece, u.fiyatTl().add(of.toplamTl()).setScale(2, RoundingMode.HALF_UP)));
    }

    /**
     * Bütün havayollarına aynı anda sor ({@code allOf}), cevap verenlerin en ucuzunu seç. Hata veren
     * havayolu {@code handle} ile {@code null}'a çevriliyor: biri çöktü diye arama çökmüyor.
     */
    public CompletableFuture<Ucus> enUcuzUcus(String nereden, String nereye) {
        List<CompletableFuture<Ucus>> cevaplar = havayollari.stream()
                .map(h -> h.ara(nereden, nereye)
                        .orTimeout(2, TimeUnit.SECONDS)
                        .handle((ucus, hata) -> hata == null ? ucus : null))
                .toList();
        return CompletableFuture.allOf(cevaplar.toArray(CompletableFuture[]::new))
                .thenApply(bos -> cevaplar.stream()
                        .map(CompletableFuture::join)   // allOf bitti: join artık beklemiyor
                        .filter(Objects::nonNull)
                        .min(Comparator.comparing(Ucus::fiyatTl))
                        .orElseThrow(() -> new SeyahatBulunamadi(nereden + "-" + nereye + " için uçuş bulunamadı")));
    }

    /** Aynı veriyi veren yansılardan hangisi önce cevap verirse ({@code anyOf}). */
    public static CompletableFuture<BigDecimal> ilkCevap(List<CompletableFuture<BigDecimal>> yansilar) {
        return CompletableFuture.anyOf(yansilar.toArray(CompletableFuture[]::new))
                .thenApply(BigDecimal.class::cast);
    }

    /** Kur servisi geç kalırsa son bilinen kurla devam ({@code completeOnTimeout}). */
    public CompletableFuture<BigDecimal> kurYaDaSonBilinen(BigDecimal sonBilinen, long sureMs) {
        return kurlar.tlKarsiligi("EUR").completeOnTimeout(sonBilinen, sureMs, TimeUnit.MILLISECONDS);
    }

    /** Hata durumunda boş sonuçla devam ({@code exceptionally}). */
    public CompletableFuture<Optional<Otel>> oteliDene(String sehir) {
        return oteller.ara(sehir)
                .thenApply(Optional::of)
                .exceptionally(hata -> Optional.empty());
    }
}
