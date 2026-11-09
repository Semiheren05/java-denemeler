package com.semih.gunluk.gun28;

/**
 * Java'da parametreler <b>her zaman değerle</b> geçirilir. Referans tipinde kopyalanan değer
 * nesnenin kendisi değil, onun heap'teki adresidir:
 * <pre>
 *   stack (çağıran)          heap
 *   sepet ──────────────►  [Sepet: Klavye, Mouse]
 *   stack (metot)            ▲
 *   s     ─────────────────┘   aynı adresin kopyası
 * </pre>
 * Metot o adres üzerinden nesnenin <em>içini</em> değiştirebilir; çağıran da bunu görür.
 * Ama kendi kopyasına yeni bir adres atarsa, çağıranın değişkeni eski nesneyi göstermeye devam eder.
 */
public final class SepetIslemleri {

    private SepetIslemleri() {
    }

    /** Nesnenin içi değişiyor: çağıran bunu görür. */
    public static void hediyeEkle(Sepet s) {
        s.ekle("Hediye paketi");
    }

    /**
     * <b>Bilerek hatalı:</b> {@code s}'ye yeni bir nesne atamak sadece bu metodun kopyasını
     * değiştirir. Çağıranın sepeti boşalmaz.
     */
    public static void yanlisBosalt(Sepet s) {
        s = new Sepet();
        s.ekle("bu ürünü kimse görmeyecek");
    }

    /** Doğrusu: nesneye "kendini boşalt" demek. */
    public static void dogruBosalt(Sepet s) {
        s.temizle();
    }

    /** İlkel tipte değerin kendisi kopyalanır; {@code fiyat} burada değişse de çağıranınki değişmez. */
    public static int indirimliFiyat(int fiyat) {
        fiyat = fiyat * 90 / 100;
        return fiyat;
    }
}
