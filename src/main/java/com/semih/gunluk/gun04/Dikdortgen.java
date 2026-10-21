package com.semih.gunluk.gun04;

public record Dikdortgen(double en, double boy) implements Sekil {

    public Dikdortgen {
        Sekil.pozitifOlmali("En", en);
        Sekil.pozitifOlmali("Boy", boy);
    }
}
