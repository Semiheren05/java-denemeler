package com.semih.gunluk.gun33;

import java.util.List;

public record Karar(String basvuran, boolean onay, List<String> sebepler) {

    public Karar {
        sebepler = List.copyOf(sebepler);
    }
}
