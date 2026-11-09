package com.semih.gunluk.gun31;

/**
 * İki sürüm yan yana: biri küresel singleton'a kendisi uzanıyor, öteki bağımlılığını
 * kurucudan alıyor (constructor injection).
 */
public class SiparisNumaralari {

    private final NumaraUretici uretici;

    public SiparisNumaralari(NumaraUretici uretici) {
        this.uretici = uretici;
    }

    public String yeniNumara() {
        return "SP-" + uretici.sonraki();
    }

    /** Gizli bağımlılık: imzaya bakan kimse bunun küresel bir sayaca dokunduğunu bilemez. */
    public static String yeniNumaraGlobal() {
        return "SP-" + GlobalSayac.ornek().sonraki();
    }
}
