package com.semih.gunluk.gun19;

/** Bir alanın uymadığı kural. */
public record Ihlal(String alan, String mesaj) {

    @Override
    public String toString() {
        return alan + ": " + mesaj;
    }
}
