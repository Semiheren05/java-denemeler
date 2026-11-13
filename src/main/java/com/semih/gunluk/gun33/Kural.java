package com.semih.gunluk.gun33;

import java.util.function.Predicate;

/** Kural bir veri: adı, koşulu ({@link Predicate}) ve tutmazsa gösterilecek sebep. */
public record Kural(String ad, Predicate<Basvuru> kosul, String redSebebi) {
}
