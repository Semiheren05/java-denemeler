package com.semih.gunluk.gun21;

import java.util.List;

/**
 * @param duraklar  başlangıç ve varış dahil, sırasıyla
 * @param dakika    toplam yolculuk süresi
 * @param aktarma   kaç kez hat değiştirildiği
 */
public record Rota(List<String> duraklar, int dakika, int aktarma) {

    public int durakSayisi() {
        return duraklar.size() - 1;
    }
}
