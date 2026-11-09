package com.semih.gunluk.gun30;

import com.semih.gunluk.gun30.yeni.Fiyatlandirma;
import com.semih.gunluk.gun30.yeni.Kategori;
import com.semih.gunluk.gun30.yeni.Kupon;
import com.semih.gunluk.gun30.yeni.MusteriTipi;
import com.semih.gunluk.gun30.yeni.Siparis;
import com.semih.gunluk.gun30.yeni.SiparisHesaplayici;
import com.semih.gunluk.gun30.yeni.SiparisKalemi;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RefactoringTest {

    private static final Map<String, Kupon> KUPONLAR =
            Map.of("YENI50", Kupon.sabitTutar(new BigDecimal("50"), new BigDecimal("500")));

    private final com.semih.gunluk.gun30.eski.SiparisHesaplayici eski = new com.semih.gunluk.gun30.eski.SiparisHesaplayici();
    private final SiparisHesaplayici yeni = new SiparisHesaplayici(KUPONLAR);

    private static SiparisKalemi kalem(String fiyat, int adet, Kategori kategori) {
        return new SiparisKalemi("urun", new BigDecimal(fiyat), adet, kategori);
    }

    private static List<String[]> eskiBicim(List<SiparisKalemi> kalemler) {
        List<String[]> sonuc = new ArrayList<>();
        for (SiparisKalemi k : kalemler) {
            sonuc.add(new String[]{k.urun(), k.birimFiyat().toPlainString(), String.valueOf(k.adet()), k.kategori().name()});
        }
        return sonuc;
    }

    /** Karakterizasyon testi: refactoring'den önce yazılır, eski kodun ne yaptığını "olduğu gibi" kilitler. */
    @Test
    void rastgeleBinSipariste_eskiVeYeniAyniSonucuVerir() {
        Random rastgele = new Random(42);   // sabit tohum: test her seferinde aynı siparişleri üretir
        String[] sehirler = {"Istanbul", "Ankara", "Izmir", "Trabzon", "Van"};
        for (int i = 0; i < 1000; i++) {
            List<SiparisKalemi> kalemler = new ArrayList<>();
            int kalemSayisi = 1 + rastgele.nextInt(4);
            for (int j = 0; j < kalemSayisi; j++) {
                String fiyat = (rastgele.nextInt(40000) + 1) / 100 + "." + String.format("%02d", rastgele.nextInt(100));
                kalemler.add(kalem(fiyat, 1 + rastgele.nextInt(3), Kategori.values()[rastgele.nextInt(3)]));
            }
            MusteriTipi tip = MusteriTipi.values()[rastgele.nextInt(3)];
            String sehir = sehirler[rastgele.nextInt(sehirler.length)];
            String kupon = rastgele.nextBoolean() ? "YENI50" : null;

            double beklenen = eski.hesapla(tip.name(), eskiBicim(kalemler), sehir, kupon);
            BigDecimal gercek = yeni.hesapla(new Siparis(tip, kalemler, sehir, Optional.ofNullable(kupon))).odenecek();

            assertEquals(beklenen, gercek.doubleValue(), 0.011, "sipariş #" + i);
        }
    }

    @Test
    void dokumHerParcayiAyriGosterir() {
        Siparis siparis = new Siparis(MusteriTipi.ALTIN,
                List.of(kalem("500", 1, Kategori.ELEKTRONIK)), "Van", Optional.of("YENI50"));

        Fiyatlandirma f = yeni.hesapla(siparis);

        assertEquals(new BigDecimal("600.00"), f.kdvliToplam());
        assertEquals(new BigDecimal("60.00"), f.musteriIndirimi());
        assertEquals(new BigDecimal("50.00"), f.kuponIndirimi());
        assertEquals(new BigDecimal("49.99"), f.kargo());        // 490 < 750, büyükşehir değil
        assertEquals(new BigDecimal("539.99"), f.odenecek());
        assertEquals(539.99, eski.hesapla("ALTIN", eskiBicim(siparis.kalemler()), "Van", "YENI50"));
    }

    @Test
    void kategoriyeGoreKdv() {
        Siparis siparis = new Siparis(MusteriTipi.STANDART, List.of(
                kalem("100", 1, Kategori.ELEKTRONIK),
                kalem("100", 1, Kategori.GIDA),
                kalem("100", 1, Kategori.DIGER)), "Istanbul", Optional.empty());

        assertEquals(new BigDecimal("331.00"), yeni.hesapla(siparis).kdvliToplam());   // 120 + 101 + 110
    }

    @Test
    void kupon_altSinirinAltindaUygulanmaz() {
        Siparis siparis = new Siparis(MusteriTipi.STANDART,
                List.of(kalem("400", 1, Kategori.DIGER)), "Ankara", Optional.of("YENI50"));   // 440 < 500

        assertEquals(new BigDecimal("0.00"), yeni.hesapla(siparis).kuponIndirimi());
    }

    @Test
    void bilinmeyenKupon_sessizceYokSayilir() {
        Siparis siparis = new Siparis(MusteriTipi.STANDART,
                List.of(kalem("1000", 1, Kategori.DIGER)), "Ankara", Optional.of("YOKBOYLE"));

        assertEquals(new BigDecimal("1100.00"), yeni.hesapla(siparis).odenecek());
    }

    @Test
    void kargo_sinirdaUcretsiz() {
        Siparis tamSinir = new Siparis(MusteriTipi.STANDART,
                List.of(kalem("750", 1, Kategori.GIDA)), "Van", Optional.empty());   // 757.50
        Siparis altinda = new Siparis(MusteriTipi.STANDART,
                List.of(kalem("600", 1, Kategori.GIDA)), "Izmir", Optional.empty()); // 606

        assertEquals(0, yeni.hesapla(tamSinir).kargo().signum());
        assertEquals(new BigDecimal("29.99"), yeni.hesapla(altinda).kargo());
    }

    /** Açık/kapalı ilkesi: yeni kupon türü için hesaplayıcıya tek satır dokunmadık, sadece haritaya ekledik. */
    @Test
    void yeniKuponTuru_hesaplayiciyiDegistirmedenEklenir() {
        Kupon yuzdeOn = tutar -> tutar.multiply(new BigDecimal("0.10"));
        SiparisHesaplayici hesaplayici = new SiparisHesaplayici(Map.of("YUZDE10", yuzdeOn));
        Siparis siparis = new Siparis(MusteriTipi.STANDART,
                List.of(kalem("1000", 1, Kategori.DIGER)), "Istanbul", Optional.of("YUZDE10"));

        Fiyatlandirma f = hesaplayici.hesapla(siparis);

        assertEquals(new BigDecimal("110.00"), f.kuponIndirimi());
        assertEquals(new BigDecimal("990.00"), f.odenecek());
    }

    /** double ile para: 0.1 + 0.2 tam olarak 0.3 değil. BigDecimal (String'den kurulursa) tam. */
    @Test
    void doubleParaIcinGuvenliDegil() {
        assertTrue(0.1 + 0.2 != 0.3);
        assertEquals(new BigDecimal("0.3"), new BigDecimal("0.1").add(new BigDecimal("0.2")));
        assertEquals(new BigDecimal("0.1000000000000000055511151231257827021181583404541015625"), new BigDecimal(0.1));
    }

    @Test
    void ilkelTakintisi_eskiKodYazimHatasiniSessizceYutar() {
        List<String[]> urun = List.<String[]>of(new String[]{"tv", "1000", "1", "ELEKTRONİK"});   // Türkçe İ

        // Kategori tutmadı, sessizce "diğer" sayıldı: %20 yerine %10 KDV
        assertEquals(1100.0, eski.hesapla("STANDART", urun, "Istanbul", null));
        // Yeni kodda aynı hata derlenmez bile; metinden çevirirken de hemen patlar
        assertThrows(IllegalArgumentException.class, () -> Kategori.valueOf("ELEKTRONİK"));
    }

    @Test
    void gecersizKalemReddedilir() {
        assertThrows(IllegalArgumentException.class, () -> kalem("-5", 1, Kategori.GIDA));
        assertThrows(IllegalArgumentException.class, () -> kalem("5", 0, Kategori.GIDA));
    }
}
