package com.semih.gunluk.gun13;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Bir ürünün fiyatını birkaç tedarikçiye <em>aynı anda</em> sorar ve en ucuz teklifi seçer.
 *
 * <p>Sırayla sorsaydım toplam süre, cevap sürelerinin toplamı olurdu. {@link CompletableFuture}
 * ile her soru ayrı bir thread'de başlıyor; toplam süre en yavaş cevap kadar. Yavaş ya da
 * hata veren bir tedarikçi sonucu bozmuyor: zaman aşımına uğrayan ya da istisna fırlatan
 * teklifler atlanıyor.
 *
 * <p>Thread'leri kendisi açmıyor, dışarıdan bir {@link ExecutorService} alıyor. Kaç thread
 * olacağına ve havuzun ne zaman kapanacağına çağıran karar veriyor.
 */
public class FiyatKarsilastirici {

    private final ExecutorService havuz;
    private final Duration zamanAsimi;

    public FiyatKarsilastirici(ExecutorService havuz, Duration zamanAsimi) {
        this.havuz = havuz;
        this.zamanAsimi = zamanAsimi;
    }

    /** @return cevap veren tedarikçi yoksa boş */
    public Optional<Teklif> enUcuz(String urun, List<Tedarikci> tedarikciler) {
        List<CompletableFuture<Optional<Teklif>>> sorgular = tedarikciler.stream()
                .map(tedarikci -> sor(tedarikci, urun))
                .toList();

        // Hepsi başladıktan sonra bekleniyor; join() burada sırayla çağrılsa da sorgular paralel çalıştı
        return sorgular.stream()
                .map(CompletableFuture::join)
                .flatMap(Optional::stream)
                .min(Comparator.comparing(Teklif::fiyat));
    }

    private CompletableFuture<Optional<Teklif>> sor(Tedarikci tedarikci, String urun) {
        return CompletableFuture
                .supplyAsync(() -> Optional.of(new Teklif(tedarikci.ad(), tedarikci.kaynak().fiyat(urun))), havuz)
                // Zamanında gelmeyen cevap "teklif yok" sayılır
                .completeOnTimeout(Optional.empty(), zamanAsimi.toMillis(), TimeUnit.MILLISECONDS)
                // Hata veren tedarikçi de
                .exceptionally(hata -> Optional.empty());
    }
}
