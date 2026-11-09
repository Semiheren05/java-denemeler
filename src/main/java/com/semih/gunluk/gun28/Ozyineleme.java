package com.semih.gunluk.gun28;

/**
 * Her metot çağrısı thread'in stack'inde bir çerçeve açar (parametreler, yerel değişkenler,
 * dönüş adresi). Özyinelemede her seviye bir çerçeve daha; stack dolunca
 * {@link StackOverflowError}. Heap'te istediğin kadar yer olsa bile.
 */
public final class Ozyineleme {

    private Ozyineleme() {
    }

    /** 1 + 2 + ... + n, özyinelemeyle: n kadar çerçeve. */
    public static long toplamOzyinelemeli(int n) {
        return n == 0 ? 0 : n + toplamOzyinelemeli(n - 1);
    }

    /** Aynısı döngüyle: tek çerçeve, n ne olursa olsun. */
    public static long toplamDongulu(int n) {
        long toplam = 0;
        for (int i = 1; i <= n; i++) {
            toplam += i;
        }
        return toplam;
    }
}
