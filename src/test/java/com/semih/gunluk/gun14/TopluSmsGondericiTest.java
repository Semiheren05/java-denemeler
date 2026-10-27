package com.semih.gunluk.gun14;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TopluSmsGondericiTest {

    /** Her gönderim 100 ms süren bir ağ çağrısı gibi bekliyor. */
    private static final SmsServisi YAVAS_SERVIS = bildirim -> Thread.sleep(100);

    @Test
    void onBinSmsVirtualThreadlerleSaniyelerIcindeGider() {
        TopluSmsGonderici gonderici = new TopluSmsGonderici(YAVAS_SERVIS, 10_000);

        GonderimRaporu rapor = gonderici.sanalThreadlerle(bildirimler(10_000));

        // Sırayla gönderilseydi 10.000 × 100 ms = 1000 saniye sürerdi
        assertEquals(10_000, rapor.basarili());
        assertTrue(rapor.sure().compareTo(Duration.ofSeconds(10)) < 0, "Çok uzun sürdü: " + rapor.sure());
    }

    @Test
    void islerGercektenVirtualThreadlerdeCalisir() {
        Set<Boolean> sanalMi = ConcurrentHashMap.newKeySet();
        TopluSmsGonderici gonderici = new TopluSmsGonderici(
                bildirim -> sanalMi.add(Thread.currentThread().isVirtual()), 100);

        gonderici.sanalThreadlerle(bildirimler(50));

        assertEquals(Set.of(true), sanalMi);
    }

    @Test
    void kucukPlatformHavuzuIsleriSirayaSokar() {
        TopluSmsGonderici gonderici = new TopluSmsGonderici(YAVAS_SERVIS, 1_000);

        // 10 thread, 200 iş: her thread 20 işi sırayla yapıyor, en az 20 × 100 ms = 2 saniye
        GonderimRaporu havuz = gonderici.platformHavuzuyla(bildirimler(200), 10);
        GonderimRaporu sanal = gonderici.sanalThreadlerle(bildirimler(200));

        assertTrue(havuz.sure().compareTo(Duration.ofMillis(1900)) >= 0, "Havuz: " + havuz.sure());
        assertTrue(sanal.sure().compareTo(havuz.sure()) < 0, "Sanal: " + sanal.sure() + ", havuz: " + havuz.sure());
    }

    @Test
    void semaforAyniAndakiGonderimSayisiniSinirlar() {
        AtomicInteger suAnda = new AtomicInteger();
        AtomicInteger enFazla = new AtomicInteger();
        SmsServisi olcen = bildirim -> {
            int simdiki = suAnda.incrementAndGet();
            enFazla.accumulateAndGet(simdiki, Math::max);
            Thread.sleep(20);
            suAnda.decrementAndGet();
        };

        GonderimRaporu rapor = new TopluSmsGonderici(olcen, 25).sanalThreadlerle(bildirimler(500));

        assertEquals(500, rapor.basarili());
        assertTrue(enFazla.get() <= 25, "Aynı anda " + enFazla.get() + " gönderim yapıldı");
        assertTrue(enFazla.get() > 1, "Hiç paralel çalışmadı");
    }

    @Test
    void hataVerenGonderimDigerleriniDurdurmaz() {
        SmsServisi bazenBozuk = bildirim -> {
            if (bildirim.telefon().endsWith("7")) {
                throw new IllegalStateException("Numara kapalı");
            }
        };

        GonderimRaporu rapor = new TopluSmsGonderici(bazenBozuk, 10).sanalThreadlerle(bildirimler(100));

        assertEquals(90, rapor.basarili());
        assertEquals(10, rapor.basarisiz());
        assertEquals(100, rapor.toplam());
    }

    @Test
    void hatadaIzinGeriVerilirSinirKuculmez() {
        // Sınır 1 ve her gönderim hata veriyor. İzin geri verilmeseydi ikinci gönderim sonsuza dek beklerdi.
        GonderimRaporu rapor = new TopluSmsGonderici(bildirim -> {
            throw new IllegalStateException("Servis kapalı");
        }, 1).sanalThreadlerle(bildirimler(20));

        assertEquals(20, rapor.basarisiz());
    }

    @Test
    void bosListeAnindaBiter() {
        GonderimRaporu rapor = new TopluSmsGonderici(YAVAS_SERVIS, 5).sanalThreadlerle(List.of());

        assertEquals(0, rapor.toplam());
        assertFalse(rapor.sure().isNegative());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void esZamanliSinirEnAzBir(int sinir) {
        assertThrows(IllegalArgumentException.class, () -> new TopluSmsGonderici(YAVAS_SERVIS, sinir));
    }

    @Test
    void bosTelefonYaDaMesajKabulEdilmez() {
        assertThrows(IllegalArgumentException.class, () -> new Bildirim(" ", "Merhaba"));
        assertThrows(IllegalArgumentException.class, () -> new Bildirim("05321234567", ""));
    }

    private static List<Bildirim> bildirimler(int adet) {
        List<Bildirim> liste = new ArrayList<>();
        for (int i = 0; i < adet; i++) {
            liste.add(new Bildirim("0532000" + String.format("%04d", i), "Siparişiniz kargoya verildi"));
        }
        return liste;
    }
}
