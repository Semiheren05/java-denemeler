package com.semih.gunluk.gun34;

/**
 * <b>Bilerek hatalı:</b> kilitleri parametre sırasıyla alıyor. Thread 1 A→B için A'yı, thread 2
 * B→A için B'yi alır; sonra ikisi de ötekinin kilidini sonsuza kadar bekler (deadlock).
 *
 * <p>{@code lockInterruptibly} sadece test için: kilitlenen thread'ler {@code interrupt} ile
 * kurtarılabilsin. {@code araAdim}, iki thread'in de ilk kilidi aldığından emin olmak için.
 */
public class HataliTransferServisi {

    private final Runnable araAdim;

    public HataliTransferServisi(Runnable araAdim) {
        this.araAdim = araAdim;
    }

    public void transfer(Hesap kaynak, Hesap hedef, long tutar) throws InterruptedException {
        TransferServisi.dogrula(kaynak, hedef, tutar);
        kaynak.kilit().lockInterruptibly();
        try {
            araAdim.run();
            hedef.kilit().lockInterruptibly();
            try {
                kaynak.dus(tutar);
                hedef.ekle(tutar);
            } finally {
                hedef.kilit().unlock();
            }
        } finally {
            kaynak.kilit().unlock();
        }
    }
}
