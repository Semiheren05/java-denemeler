package com.semih.gunluk.gun08;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Noktalı virgülle ayrılmış ürün listesini okur:
 *
 * <pre>
 * kod;ad;fiyat;stok
 * U001;Kalem;12,50;100
 * </pre>
 *
 * <p>İki tür hata var ve farklı ele alınıyor:
 * <ul>
 *   <li><b>Dosya düzeyi</b> (dosya yok, başlık yanlış): devam etmenin anlamı yok,
 *       {@link DosyaOkunamadiException} fırlatılır.</li>
 *   <li><b>Satır düzeyi</b> (eksik sütun, bozuk fiyat): tek satır yüzünden bütün dosya
 *       çöpe gitmesin diye satır atlanır ve {@link HataliSatir} olarak raporlanır.</li>
 * </ul>
 */
public final class UrunCsvOkuyucu {

    static final String BASLIK = "kod;ad;fiyat;stok";
    private static final String AYIRICI = ";";
    private static final int SUTUN_SAYISI = 4;

    private UrunCsvOkuyucu() {
        // yardımcı sınıf, nesnesi oluşturulmaz
    }

    public static OkumaSonucu oku(Path dosya) throws DosyaOkunamadiException {
        // try-with-resources: okuyucu, hata olsa da olmasa da otomatik kapatılır
        try (BufferedReader okuyucu = Files.newBufferedReader(dosya, StandardCharsets.UTF_8)) {
            return oku(dosya, okuyucu);
        } catch (NoSuchFileException e) {
            throw new DosyaOkunamadiException(dosya, "Dosya bulunamadı", e);
        } catch (IOException e) {
            throw new DosyaOkunamadiException(dosya, "Dosya okunamadı", e);
        }
    }

    private static OkumaSonucu oku(Path dosya, BufferedReader okuyucu) throws IOException, DosyaOkunamadiException {
        String baslik = okuyucu.readLine();
        if (baslik == null || !baslik.strip().equalsIgnoreCase(BASLIK)) {
            throw new DosyaOkunamadiException(dosya, "Başlık satırı '" + BASLIK + "' olmalı");
        }

        List<Urun> urunler = new ArrayList<>();
        List<HataliSatir> hatalar = new ArrayList<>();
        int satirNo = 1;
        String satir;
        while ((satir = okuyucu.readLine()) != null) {
            satirNo++;
            if (satir.isBlank()) {
                continue;
            }
            try {
                urunler.add(satiriCoz(satir));
            } catch (IllegalArgumentException e) {
                // NumberFormatException da IllegalArgumentException'ın alt sınıfı
                hatalar.add(new HataliSatir(satirNo, satir, e.getMessage()));
            }
        }
        return new OkumaSonucu(urunler, hatalar);
    }

    static Urun satiriCoz(String satir) {
        String[] sutunlar = satir.split(AYIRICI, -1);
        if (sutunlar.length != SUTUN_SAYISI) {
            throw new IllegalArgumentException(SUTUN_SAYISI + " sütun bekleniyordu, " + sutunlar.length + " bulundu");
        }
        String kod = sutunlar[0].strip();
        String ad = sutunlar[1].strip();
        BigDecimal fiyat = sayi(sutunlar[2], "Fiyat");
        int stok = tamSayi(sutunlar[3], "Stok");
        return new Urun(kod, ad, fiyat, stok);
    }

    private static int tamSayi(String deger, String alan) {
        try {
            return sayi(deger, alan).intValueExact();
        } catch (ArithmeticException e) {
            // intValueExact küsuratlı ya da int'e sığmayan sayıda ArithmeticException fırlatır
            throw new IllegalArgumentException(alan + " tam sayı olmalı: '" + deger.strip() + "'", e);
        }
    }

    /** Türkçe ondalık virgülü de kabul eder: "12,50" → 12.50 */
    private static BigDecimal sayi(String deger, String alan) {
        try {
            return new BigDecimal(deger.strip().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(alan + " sayı değil: '" + deger.strip() + "'", e);
        }
    }
}
