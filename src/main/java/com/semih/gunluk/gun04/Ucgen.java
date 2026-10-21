package com.semih.gunluk.gun04;

/**
 * Üç kenarı verilen üçgen. Üçgen eşitsizliğini sağlamayan kenarlar
 * (ör. 1, 2, 10) gerçek bir üçgen oluşturmadığı için reddedilir.
 */
public record Ucgen(double a, double b, double c) implements Sekil {

    public Ucgen {
        Sekil.pozitifOlmali("a kenarı", a);
        Sekil.pozitifOlmali("b kenarı", b);
        Sekil.pozitifOlmali("c kenarı", c);
        if (a + b <= c || a + c <= b || b + c <= a) {
            throw new IllegalArgumentException(
                    "Bu kenarlarla üçgen olmaz (her iki kenarın toplamı üçüncüden büyük olmalı): "
                            + a + ", " + b + ", " + c);
        }
    }

    public boolean eskenarMi() {
        return a == b && b == c;
    }

    public boolean ikizkenarMi() {
        return a == b || b == c || a == c;
    }
}
