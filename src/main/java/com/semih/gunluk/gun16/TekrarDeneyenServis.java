package com.semih.gunluk.gun16;

/**
 * <b>Decorator</b>: içteki servis hata verirse birkaç kez daha dener. Hepsi başarısız
 * olursa son hatayı fırlatır; önceki hatalar "suppressed" olarak ona eklenir, kaybolmaz.
 */
public class TekrarDeneyenServis implements HavaDurumuServisi {

    private final HavaDurumuServisi ic;
    private final int denemeSayisi;

    public TekrarDeneyenServis(HavaDurumuServisi ic, int denemeSayisi) {
        if (denemeSayisi < 1) {
            throw new IllegalArgumentException("Deneme sayısı en az 1 olmalı: " + denemeSayisi);
        }
        this.ic = ic;
        this.denemeSayisi = denemeSayisi;
    }

    @Override
    public double sicaklik(String sehir) {
        RuntimeException sonHata = null;
        for (int deneme = 1; deneme <= denemeSayisi; deneme++) {
            try {
                return ic.sicaklik(sehir);
            } catch (IllegalArgumentException e) {
                // Bilinmeyen şehir: tekrar denemek sonucu değiştirmez
                throw e;
            } catch (RuntimeException e) {
                if (sonHata != null) {
                    e.addSuppressed(sonHata);
                }
                sonHata = e;
            }
        }
        throw sonHata;
    }
}
