package com.semih.gunluk.gun16;

import java.time.Clock;
import java.time.Duration;

/**
 * <b>Factory</b>: servisin nasıl kurulacağını tek bir yere topluyor. Uygulama
 * {@code new OnbellekliServis(new TekrarDeneyenServis(new YabanciApiAdaptoru(api), 3), ...)}
 * zincirini bilmek zorunda kalmıyor; sadece {@code HavaDurumuServisleri.uretim(api, saat)} diyor.
 */
public final class HavaDurumuServisleri {

    private HavaDurumuServisleri() {
    }

    /**
     * Sıra önemli: önbellek en dışta. Önbellekte varsa hiç ağa çıkılmıyor; yoksa tekrar deneyen
     * katmana iniliyor, o da adaptör üzerinden yabancı API'yi çağırıyor.
     */
    public static HavaDurumuServisi uretim(YabanciHavaApi api, Clock saat) {
        HavaDurumuServisi servis = new YabanciApiAdaptoru(api);
        servis = new TekrarDeneyenServis(servis, 3);
        servis = new OnbellekliServis(servis, Duration.ofMinutes(10), saat);
        return servis;
    }
}
