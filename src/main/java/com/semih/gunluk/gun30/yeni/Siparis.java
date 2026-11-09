package com.semih.gunluk.gun30.yeni;

import java.util.List;
import java.util.Optional;

/** Dört ayrı parametre yerine tek bir parametre nesnesi. Kupon gerçekten isteğe bağlı: {@code Optional}. */
public record Siparis(MusteriTipi musteriTipi, List<SiparisKalemi> kalemler, String sehir, Optional<String> kuponKodu) {

    public Siparis {
        kalemler = List.copyOf(kalemler);
    }
}
