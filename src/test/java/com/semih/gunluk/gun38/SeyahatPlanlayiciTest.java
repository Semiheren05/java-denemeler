package com.semih.gunluk.gun38;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeyahatPlanlayiciTest {

    /** {@code ms} sonra değer veren sahte servis; bekleme için thread tutmuyor ({@code delayedExecutor}). */
    private static <T> CompletableFuture<T> gecikmeli(T deger, long ms) {
        return CompletableFuture.supplyAsync(() -> deger, CompletableFuture.delayedExecutor(ms, TimeUnit.MILLISECONDS));
    }

    private static <T> CompletableFuture<T> hatali(String mesaj, long ms) {
        return CompletableFuture.supplyAsync(() -> {
            throw new IllegalStateException(mesaj);
        }, CompletableFuture.delayedExecutor(ms, TimeUnit.MILLISECONDS));
    }

    private static UcusServisi havayolu(String firma, String fiyat, long ms) {
        return (nereden, nereye) -> gecikmeli(new Ucus(firma, new BigDecimal(fiyat)), ms);
    }

    private static final OtelServisi OTEL = sehir -> gecikmeli(new Otel(sehir + " Palas", new BigDecimal("80"), "EUR"), 400);
    private static final KurServisi KUR = birim -> gecikmeli(new BigDecimal("37.50"), 400);

    @Test
    void paket_bagimsizCagrilarParalel_bagimliOlanSirali() {
        SeyahatPlanlayici planlayici = new SeyahatPlanlayici(List.of(havayolu("THY", "2500", 400)), OTEL, KUR);

        long bas = System.nanoTime();
        Paket paket = planlayici.paket("IST", "ROM", 3).join();
        long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - bas);

        assertEquals(new BigDecimal("11500.00"), paket.toplamTl());   // 2500 + 80 € × 37,50 × 3 gece
        assertEquals("ROM Palas", paket.otel().ad());
        // uçuş ve otel aynı anda (400 ms), kur otelden sonra (+400 ms); sıralı olsaydı 1200 ms
        assertTrue(ms >= 780 && ms < 1150, "süre: " + ms);
    }

    @Test
    void enUcuzUcus_hepsineAyniAndaSorar() {
        SeyahatPlanlayici planlayici = new SeyahatPlanlayici(List.of(
                havayolu("THY", "2500", 400),
                havayolu("Pegasus", "1900", 600),
                havayolu("AJet", "2100", 200)), OTEL, KUR);

        long bas = System.nanoTime();
        Ucus ucus = planlayici.enUcuzUcus("IST", "ROM").join();
        long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - bas);

        assertEquals("Pegasus", ucus.firma());
        assertTrue(ms < 1000, "süre: " + ms);   // en yavaşı kadar (600), toplamları kadar (1200) değil
    }

    @Test
    void birHavayoluCokerse_digerleriyleDevam() {
        SeyahatPlanlayici planlayici = new SeyahatPlanlayici(List.of(
                (a, b) -> hatali("503 Service Unavailable", 50),
                havayolu("AJet", "2100", 100)), OTEL, KUR);

        assertEquals("AJet", planlayici.enUcuzUcus("IST", "ROM").join().firma());
    }

    @Test
    void hepsiCokerse_anlamliHata() {
        SeyahatPlanlayici planlayici = new SeyahatPlanlayici(List.of(
                (a, b) -> hatali("503", 50), (a, b) -> hatali("timeout", 80)), OTEL, KUR);

        CompletionException hata = assertThrows(CompletionException.class,
                () -> planlayici.enUcuzUcus("IST", "ROM").join());

        assertInstanceOf(SeyahatBulunamadi.class, hata.getCause());
        assertEquals("IST-ROM için uçuş bulunamadı", hata.getCause().getMessage());
    }

    @Test
    void anyOf_ilkCevapVereniAlir() {
        BigDecimal kur = SeyahatPlanlayici.ilkCevap(List.of(
                gecikmeli(new BigDecimal("37.40"), 400),
                gecikmeli(new BigDecimal("37.50"), 50),
                gecikmeli(new BigDecimal("37.60"), 300))).join();

        assertEquals(new BigDecimal("37.50"), kur);
    }

    @Test
    void completeOnTimeout_yedekDeger_orTimeout_hata() {
        SeyahatPlanlayici yavasKur = new SeyahatPlanlayici(List.of(), OTEL, birim -> gecikmeli(new BigDecimal("37.50"), 2_000));

        assertEquals(new BigDecimal("36.90"), yavasKur.kurYaDaSonBilinen(new BigDecimal("36.90"), 100).join());

        CompletableFuture<BigDecimal> sureli = gecikmeli(BigDecimal.ONE, 2_000).orTimeout(100, TimeUnit.MILLISECONDS);
        assertInstanceOf(TimeoutException.class, assertThrows(CompletionException.class, sureli::join).getCause());
    }

    @Test
    void join_ve_get_farkliIstisnaSarar() {
        CompletableFuture<Ucus> hataliCagri = hatali("bağlantı koptu", 10);

        CompletionException joinHatasi = assertThrows(CompletionException.class, hataliCagri::join);        // unchecked
        ExecutionException getHatasi = assertThrows(ExecutionException.class, hataliCagri::get);          // checked

        assertInstanceOf(IllegalStateException.class, joinHatasi.getCause());
        assertEquals("bağlantı koptu", getHatasi.getCause().getMessage());
    }

    @Test
    void exceptionally_zinciriKurtarir_aradakiAdimlarAtlanir() {
        AtomicInteger calisanAdim = new AtomicInteger();
        CompletableFuture<String> zincir = CompletableFuture.<Integer>failedFuture(new IllegalStateException("x"))
                .thenApply(x -> {
                    calisanAdim.incrementAndGet();   // hata varken çalışmaz
                    return x * 2;
                })
                .thenApply(x -> "sonuç " + x)
                .exceptionally(hata -> "yedek");

        assertEquals("yedek", zincir.join());
        assertEquals(0, calisanAdim.get());
        SeyahatPlanlayici planlayici = new SeyahatPlanlayici(List.of(), sehir -> hatali("yok", 10), KUR);
        assertEquals(Optional.empty(), planlayici.oteliDene("ROM").join());
    }

    @Test
    void thenApplyAsync_verilenHavuzdaCalisir() throws Exception {
        try (ExecutorService havuz = Executors.newFixedThreadPool(2, r -> new Thread(r, "seyahat-havuzu"))) {
            String thread = CompletableFuture.supplyAsync(() -> 1, havuz)
                    .thenApplyAsync(x -> Thread.currentThread().getName(), havuz)
                    .get(2, TimeUnit.SECONDS);

            assertEquals("seyahat-havuzu", thread);
        }
    }

    @Test
    void thenCompose_kurOtelinParaBirimiyleIstenir() {
        List<String> istenenBirimler = new java.util.concurrent.CopyOnWriteArrayList<>();
        KurServisi kaydeden = birim -> {
            istenenBirimler.add(birim);
            return gecikmeli(new BigDecimal("1.20"), 10);
        };
        OtelServisi sterlin = sehir -> gecikmeli(new Otel("Londra Inn", new BigDecimal("100"), "GBP"), 10);
        SeyahatPlanlayici planlayici = new SeyahatPlanlayici(List.of(havayolu("THY", "50", 10)), sterlin, kaydeden);

        Paket paket = planlayici.paket("IST", "LHR", 2).join();

        assertEquals(List.of("GBP"), istenenBirimler);
        assertEquals(new BigDecimal("290.00"), paket.toplamTl());   // 50 + 100 × 1,20 × 2
    }
}
