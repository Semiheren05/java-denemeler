package com.semih.gunluk.gun35;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Kütüphaneyi tek bir metin dosyasına yazıp okur. Satır başındaki etiket türü söylüyor:
 * {@code K;isbn;ad;yazar} ve {@code O;isbn;uye;alis;sonTeslim;uzatildi;iade} (iade yoksa boş).
 */
public final class KutuphaneDosyasi {

    private KutuphaneDosyasi() {
    }

    public static void kaydet(Kutuphane kutuphane, Path dosya) {
        List<String> satirlar = new ArrayList<>();
        for (Kitap k : kutuphane.kitaplar()) {
            satirlar.add(String.join(";", "K", k.isbn(), k.ad(), k.yazar()));
        }
        for (Odunc o : kutuphane.oduncler()) {
            satirlar.add(String.join(";", "O", o.isbn(), o.uyeNo(), o.alis().toString(), o.sonTeslim().toString(),
                    String.valueOf(o.uzatildi()), o.iadeTarihi().map(LocalDate::toString).orElse("")));
        }
        try {
            Files.write(dosya, satirlar, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Kaydedilemedi: " + dosya, e);
        }
    }

    public static Kutuphane yukle(Path dosya, Clock saat) {
        Kutuphane kutuphane = new Kutuphane(saat);
        List<String> satirlar;
        try {
            satirlar = Files.readAllLines(dosya, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Okunamadı: " + dosya, e);
        }
        int no = 0;
        for (String satir : satirlar) {
            no++;
            String[] p = satir.split(";", -1);   // -1: sondaki boş alanı (iade yok) atma
            switch (p[0]) {
                case "K" -> kutuphane.ekle(new Kitap(p[1], p[2], p[3]));
                case "O" -> kutuphane.kayitEkle(new Odunc(p[1], p[2], LocalDate.parse(p[3]), LocalDate.parse(p[4]),
                        Boolean.parseBoolean(p[5]), p[6].isEmpty() ? null : LocalDate.parse(p[6])));
                default -> throw new KutuphaneHatasi(no + ". satır anlaşılamadı: " + satir);
            }
        }
        return kutuphane;
    }
}
