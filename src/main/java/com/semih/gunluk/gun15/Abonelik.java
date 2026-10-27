package com.semih.gunluk.gun15;

/**
 * Bir dinleyicinin aboneliği. {@link AutoCloseable} olduğu için try-with-resources ile
 * kullanılabiliyor: blok bitince abonelik kendiliğinden iptal oluyor. ({@code close} burada
 * hiçbir checked exception fırlatmadığı için daraltıldı.)
 */
@FunctionalInterface
public interface Abonelik extends AutoCloseable {

    @Override
    void close();
}
