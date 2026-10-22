package com.semih.gunluk.gun09;

/** Yeni randevu var olan bir randevuyla aynı zamana denk geldiğinde fırlatılır. */
public class CakismaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final transient Randevu mevcut;

    public CakismaException(Randevu mevcut) {
        super("Bu saatte başka bir randevu var: " + mevcut.musteri() + ", "
                + mevcut.baslangic().toLocalTime() + "–" + mevcut.bitis().toLocalTime());
        this.mevcut = mevcut;
    }

    public Randevu getMevcut() {
        return mevcut;
    }
}
