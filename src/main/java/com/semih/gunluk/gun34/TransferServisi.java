package com.semih.gunluk.gun34;

import java.util.concurrent.TimeUnit;

/**
 * Para transferi: iki hesabın kilidi birden gerekiyor. Kilitler her zaman <b>aynı sırayla</b>
 * (küçük id önce) alınıyor; bu yüzden A→B ve B→A aynı anda gelse de deadlock olmuyor.
 */
public class TransferServisi {

    public void transfer(Hesap kaynak, Hesap hedef, long tutar) {
        dogrula(kaynak, hedef, tutar);
        Hesap ilk = kaynak.id() < hedef.id() ? kaynak : hedef;
        Hesap ikinci = ilk == kaynak ? hedef : kaynak;
        ilk.kilit().lock();
        try {
            ikinci.kilit().lock();
            try {
                kaynak.dus(tutar);   // önce düş: yetmezse hiçbir şey değişmeden istisna
                hedef.ekle(tutar);
            } finally {
                ikinci.kilit().unlock();
            }
        } finally {
            ilk.kilit().unlock();
        }
    }

    /**
     * {@code tryLock} ile süreli deneme: kilit alınamazsa beklemek yerine vazgeçiyor.
     * Deadlock'u önlemenin ikinci yolu; çağıran sonra tekrar deneyebilir.
     *
     * @return transfer yapıldıysa {@code true}, kilit süre içinde alınamadıysa {@code false}
     */
    public boolean dene(Hesap kaynak, Hesap hedef, long tutar, long sureMs) throws InterruptedException {
        dogrula(kaynak, hedef, tutar);
        if (!kaynak.kilit().tryLock(sureMs, TimeUnit.MILLISECONDS)) {
            return false;
        }
        try {
            if (!hedef.kilit().tryLock(sureMs, TimeUnit.MILLISECONDS)) {
                return false;
            }
            try {
                kaynak.dus(tutar);
                hedef.ekle(tutar);
                return true;
            } finally {
                hedef.kilit().unlock();
            }
        } finally {
            kaynak.kilit().unlock();
        }
    }

    static void dogrula(Hesap kaynak, Hesap hedef, long tutar) {
        if (kaynak == hedef) {
            throw new IllegalArgumentException("Aynı hesaba transfer yapılamaz");
        }
        if (tutar <= 0) {
            throw new IllegalArgumentException("Tutar pozitif olmalı: " + tutar);
        }
    }
}
