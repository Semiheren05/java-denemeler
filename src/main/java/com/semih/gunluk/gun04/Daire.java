package com.semih.gunluk.gun04;

public record Daire(double yaricap) implements Sekil {

    public Daire {
        Sekil.pozitifOlmali("Yarıçap", yaricap);
    }
}
