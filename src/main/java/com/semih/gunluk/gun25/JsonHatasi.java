package com.semih.gunluk.gun25;

/** Ayrıştırma hatası; nerede olduğu satır ve sütunla birlikte. */
public class JsonHatasi extends RuntimeException {

    private final int satir;
    private final int sutun;

    public JsonHatasi(String mesaj, int satir, int sutun) {
        super(satir + ". satır, " + sutun + ". sütun: " + mesaj);
        this.satir = satir;
        this.sutun = sutun;
    }

    public int satir() {
        return satir;
    }

    public int sutun() {
        return sutun;
    }
}
