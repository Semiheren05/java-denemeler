package com.semih.gunluk.gun13;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FiyatKarsilastiriciTest {

    @Test
    void enUcuzTeklifSecilir() {
        havuzla(Duration.ofSeconds(2), karsilastirici -> {
            Optional<Teklif> teklif = karsilastirici.enUcuz("Klavye", List.of(
                    sabit("Ali Elektronik", "799.90"),
                    sabit("Deniz Bilişim", "749.90"),
                    sabit("Ayşe Ticaret", "820.00")));

            assertEquals(Optional.of(new Teklif("Deniz Bilişim", new BigDecimal("749.90"))), teklif);
        });
    }

    @Test
    void hataVerenTedarikciAtlanir() {
        havuzla(Duration.ofSeconds(2), karsilastirici -> {
            Tedarikci bozuk = new Tedarikci("Bozuk A.Ş.", urun -> {
                throw new IllegalStateException("Servis kapalı");
            });

            Optional<Teklif> teklif = karsilastirici.enUcuz("Klavye", List.of(bozuk, sabit("Ali Elektronik", "799.90")));

            assertEquals("Ali Elektronik", teklif.orElseThrow().tedarikci());
        });
    }

    @Test
    void gecikenTedarikciBeklenmez() {
        havuzla(Duration.ofMillis(200), karsilastirici -> {
            Tedarikci yavas = new Tedarikci("Yavaş Ltd.", urun -> {
                uyu(Duration.ofSeconds(3));
                return new BigDecimal("1.00");   // en ucuzu ama çok geç
            });

            long basla = System.nanoTime();
            Optional<Teklif> teklif = karsilastirici.enUcuz("Klavye", List.of(yavas, sabit("Ali Elektronik", "799.90")));
            Duration gecen = Duration.ofNanos(System.nanoTime() - basla);

            assertEquals("Ali Elektronik", teklif.orElseThrow().tedarikci());
            assertTrue(gecen.compareTo(Duration.ofSeconds(2)) < 0, "3 saniye beklenmemeliydi: " + gecen);
        });
    }

    @Test
    void tedarikcilerGercektenAyniAndaSorulur() {
        // Üç tedarikçi de, üçü birden aynı noktaya gelene kadar bekliyor. Sorular sırayla
        // gitseydi ilk tedarikçi diğer ikisini hiç göremez, 1 saniye sonra hata verirdi.
        CyclicBarrier hepsiBurada = new CyclicBarrier(3);
        FiyatKaynagi bekleyen = urun -> {
            try {
                hepsiBurada.await(1, TimeUnit.SECONDS);
            } catch (Exception e) {
                throw new IllegalStateException("Sorular paralel gitmedi", e);
            }
            return new BigDecimal("100.00");
        };

        havuzla(Duration.ofSeconds(3), karsilastirici -> {
            Optional<Teklif> teklif = karsilastirici.enUcuz("Klavye", List.of(
                    new Tedarikci("A", bekleyen), new Tedarikci("B", bekleyen), new Tedarikci("C", bekleyen)));

            assertTrue(teklif.isPresent());
        });
    }

    @Test
    void kimseCevapVermezseTeklifYok() {
        havuzla(Duration.ofSeconds(2), karsilastirici -> {
            Tedarikci bozuk = new Tedarikci("Bozuk", urun -> {
                throw new IllegalStateException("Servis kapalı");
            });

            assertTrue(karsilastirici.enUcuz("Klavye", List.of(bozuk)).isEmpty());
            assertTrue(karsilastirici.enUcuz("Klavye", List.of()).isEmpty());
        });
    }

    @Test
    void gecersizFiyatTeklifSayilmaz() {
        assertThrows(IllegalArgumentException.class, () -> new Teklif("X", BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> new Teklif("X", null));
    }

    /** Her test kendi havuzunu açar ve sonunda kapatır; yavaş görevler kesilir. */
    private static void havuzla(Duration zamanAsimi, java.util.function.Consumer<FiyatKarsilastirici> test) {
        ExecutorService havuz = Executors.newFixedThreadPool(4);
        try {
            test.accept(new FiyatKarsilastirici(havuz, zamanAsimi));
        } finally {
            havuz.shutdownNow();
        }
    }

    private static Tedarikci sabit(String ad, String fiyat) {
        return new Tedarikci(ad, urun -> new BigDecimal(fiyat));
    }

    private static void uyu(Duration sure) {
        try {
            Thread.sleep(sure);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }
}
