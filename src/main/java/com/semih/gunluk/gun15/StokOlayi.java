package com.semih.gunluk.gun15;

/** Depoda olan biten her şey. {@code sealed}: olay türleri bu üçüyle sınırlı. */
public sealed interface StokOlayi permits StokOlayi.StokEklendi, StokOlayi.StokAzaldi, StokOlayi.StokBitti {

    String urun();

    record StokEklendi(String urun, int adet, int yeniStok) implements StokOlayi {
    }

    /** Stok kritik seviyenin altına <em>indiği anda</em> bir kez yayınlanır. */
    record StokAzaldi(String urun, int kalan, int kritikSeviye) implements StokOlayi {
    }

    record StokBitti(String urun) implements StokOlayi {
    }
}
