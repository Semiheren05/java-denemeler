package com.semih.gunluk.gun14;

/**
 * Dış SMS sağlayıcısı. Gerçekte bir HTTP çağrısı: thread cevap gelene kadar bekler (bloklar).
 * Hata olursa istisna fırlatır.
 */
@FunctionalInterface
public interface SmsServisi {

    void gonder(Bildirim bildirim) throws Exception;
}
