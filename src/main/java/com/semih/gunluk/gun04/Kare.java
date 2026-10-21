package com.semih.gunluk.gun04;

public record Kare(double kenar) implements Sekil {

    public Kare {
        Sekil.pozitifOlmali("Kenar", kenar);
    }
}
