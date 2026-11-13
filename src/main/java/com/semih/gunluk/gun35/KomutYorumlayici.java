package com.semih.gunluk.gun35;

import java.math.BigDecimal;
import java.util.stream.Collectors;

/**
 * Konsol menüsünün çekirdeği: bir satır alır, bir cevap döner. {@code Scanner} döngüsü
 * ({@code main}) bunun etrafında üç satır; asıl iş burada olduğu için test edilebiliyor.
 */
public class KomutYorumlayici {

    private final Kutuphane kutuphane;

    public KomutYorumlayici(Kutuphane kutuphane) {
        this.kutuphane = kutuphane;
    }

    public String calistir(String satir) {
        String[] p = satir.trim().split("\\s+");
        try {
            return switch (p[0]) {
                case "ver" -> {
                    if (p.length != 3) {
                        yield "Kullanım: ver <isbn> <üye>";
                    }
                    yield "Verildi, son teslim " + kutuphane.oduncVer(p[1], p[2]).sonTeslim();
                }
                case "iade" -> {
                    if (p.length != 2) {
                        yield "Kullanım: iade <isbn>";
                    }
                    BigDecimal ceza = kutuphane.iadeEt(p[1]);
                    yield ceza.signum() == 0 ? "İade alındı" : "İade alındı, ceza " + ceza + " TL";
                }
                case "geciken" -> kutuphane.gecikenler().stream()
                        .map(o -> o.isbn() + " " + o.uyeNo())
                        .collect(Collectors.joining(", ", "Gecikenler: [", "]"));
                default -> "Bilinmeyen komut: " + p[0] + " (ver, iade, geciken)";
            };
        } catch (KutuphaneHatasi e) {
            return "Hata: " + e.getMessage();
        }
    }
}
