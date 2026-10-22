package com.semih.gunluk.gun05;

/**
 * İki farklı tipte değeri bir arada taşıyan genel bir {@code record}.
 *
 * @param <A> birinci değerin tipi
 * @param <B> ikinci değerin tipi
 */
public record Cift<A, B>(A birinci, B ikinci) {

    /** Yerleri değişmiş yeni bir çift; tipler de yer değiştirir: {@code Cift<B, A>}. */
    public Cift<B, A> tersCevir() {
        return new Cift<>(ikinci, birinci);
    }
}
