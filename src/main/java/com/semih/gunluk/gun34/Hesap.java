package com.semih.gunluk.gun34;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Her hesabın kendi kilidi var. Para kuruş cinsinden {@code long}: tam sayı olduğu için yuvarlama
 * derdi yok, toplamın korunduğunu kontrol etmek de kolay.
 */
public class Hesap {

    private final long id;
    private final ReentrantLock kilit = new ReentrantLock();
    private final Condition paraGeldi = kilit.newCondition();
    private long bakiye;

    public Hesap(long id, long bakiye) {
        this.id = id;
        this.bakiye = bakiye;
    }

    public long id() {
        return id;
    }

    ReentrantLock kilit() {
        return kilit;
    }

    public long bakiye() {
        kilit.lock();
        try {
            return bakiye;
        } finally {
            kilit.unlock();
        }
    }

    /** Kilit tutuluyorken çağrılır. */
    void dus(long tutar) {
        if (tutar > bakiye) {
            throw new YetersizBakiye(id, bakiye, tutar);
        }
        bakiye -= tutar;
    }

    /** Kilit tutuluyorken çağrılır; bekleyen çekimleri uyandırır. */
    void ekle(long tutar) {
        bakiye += tutar;
        paraGeldi.signalAll();
    }

    public void yatir(long tutar) {
        kilit.lock();
        try {
            ekle(tutar);
        } finally {
            kilit.unlock();
        }
    }

    /**
     * {@link Condition}: bakiye yetene kadar kilidi bırakıp bekler; para gelince uyanıp tekrar dener.
     * {@code while} şart: uyanmak koşulun sağlandığı anlamına gelmiyor (başkası önce çekmiş olabilir).
     *
     * @return süre dolmadan çekebildiyse {@code true}
     */
    public boolean bekleVeCek(long tutar, long sureMs) throws InterruptedException {
        long kalanNs = TimeUnit.MILLISECONDS.toNanos(sureMs);
        kilit.lock();
        try {
            while (bakiye < tutar) {
                if (kalanNs <= 0) {
                    return false;
                }
                kalanNs = paraGeldi.awaitNanos(kalanNs);
            }
            bakiye -= tutar;
            return true;
        } finally {
            kilit.unlock();
        }
    }
}
