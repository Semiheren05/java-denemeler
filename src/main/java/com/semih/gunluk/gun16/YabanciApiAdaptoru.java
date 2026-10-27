package com.semih.gunluk.gun16;

import java.util.Locale;
import java.util.Map;

/**
 * <b>Adapter</b>: yabancı API'yi bizim {@link HavaDurumuServisi} arayüzümüze uyduruyor.
 * Şehir adını plaka koduna, "72.5F" metnini santigrada çeviriyor. Uygulamanın geri kalanı
 * yabancı API'yi hiç görmüyor; yarın başka bir sağlayıcıya geçersek sadece bu sınıf değişir.
 */
public class YabanciApiAdaptoru implements HavaDurumuServisi {

    private static final Map<String, Integer> PLAKALAR = Map.of(
            "istanbul", 34, "ankara", 6, "izmir", 35, "antalya", 7, "erzurum", 25);
    private static final Locale TURKCE = Locale.forLanguageTag("tr-TR");

    private final YabanciHavaApi api;

    public YabanciApiAdaptoru(YabanciHavaApi api) {
        this.api = api;
    }

    @Override
    public double sicaklik(String sehir) {
        Integer plaka = PLAKALAR.get(sehir.trim().toLowerCase(TURKCE));
        if (plaka == null) {
            throw new IllegalArgumentException("Bilinmeyen şehir: " + sehir);
        }
        String cevap = api.currentTemperature(plaka);
        if (cevap == null || !cevap.endsWith("F")) {
            throw new IllegalStateException("Beklenmeyen cevap: " + cevap);
        }
        double fahrenheit = Double.parseDouble(cevap.substring(0, cevap.length() - 1));
        double santigrat = (fahrenheit - 32) * 5 / 9;
        return Math.round(santigrat * 10) / 10.0;
    }
}
