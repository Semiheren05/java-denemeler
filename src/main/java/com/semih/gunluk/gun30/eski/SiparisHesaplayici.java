package com.semih.gunluk.gun30.eski;

import java.util.List;

/**
 * <b>Bilerek kötü yazılmış</b> başlangıç kodu. Çalışıyor, ama okumak, test etmek ve değiştirmek
 * zor. Kokular (README'deki tabloyla aynı numaralar):
 * <ol>
 *   <li>Uzun metot: dört ayrı iş tek metotta.</li>
 *   <li>İlkel takıntısı: ürün {@code String[]}, müşteri tipi ve kategori {@code String}, para {@code double}.</li>
 *   <li>Sihirli sayılar: 1.20, 0.10, 750, 29.99...</li>
 *   <li>Derin iç içe {@code if}'ler.</li>
 *   <li>Anlamsız adlar: {@code t}, {@code u}, {@code f}, {@code a}.</li>
 *   <li>Açık/kapalıya aykırı: yeni bir kupon ya da kategori için bu metodu açıp değiştirmek gerekiyor.</li>
 *   <li>Sonucun parçaları kayıp: sadece tek bir sayı dönüyor, kargo ya da indirim ayrıca görülemiyor.</li>
 * </ol>
 * Bu sınıf değiştirilmiyor; {@code yeni} paketindeki sürümün davranışı bununla karşılaştırılarak test ediliyor.
 */
public class SiparisHesaplayici {

    public double hesapla(String musteriTipi, List<String[]> urunler, String sehir, String kupon) {
        double t = 0;
        for (String[] u : urunler) {
            // u[0] = ad, u[1] = fiyat, u[2] = adet, u[3] = kategori
            double f = Double.parseDouble(u[1]);
            int a = Integer.parseInt(u[2]);
            if (u[3].equals("ELEKTRONIK")) {
                t += f * a * 1.20;
            } else if (u[3].equals("GIDA")) {
                t += f * a * 1.01;
            } else {
                t += f * a * 1.10;
            }
        }
        if (musteriTipi.equals("ALTIN")) {
            t = t - t * 0.10;
        } else if (musteriTipi.equals("GUMUS")) {
            t = t - t * 0.05;
        }
        if (kupon != null) {
            if (kupon.equals("YENI50")) {
                if (t >= 500) {
                    t = t - 50;
                }
            }
        }
        if (t < 750) {
            if (sehir.equals("Istanbul") || sehir.equals("Ankara") || sehir.equals("Izmir")) {
                t = t + 29.99;
            } else {
                t = t + 49.99;
            }
        }
        return Math.round(t * 100) / 100.0;
    }
}
