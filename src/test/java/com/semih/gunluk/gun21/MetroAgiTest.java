package com.semih.gunluk.gun21;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Süreler örnek; ağ gerçek İstanbul duraklarından küçük bir parça. */
class MetroAgiTest {

    private MetroAgi ag;

    @BeforeEach
    void setUp() {
        ag = new MetroAgi()
                .hatEkle("M2", 2, "Yenikapı", "Vezneciler", "Haliç", "Şişhane", "Taksim", "Osmanbey")
                .hatEkle("Marmaray", 4, "Yenikapı", "Sirkeci", "Üsküdar")
                .hatEkle("T1", 4, "Sirkeci", "Karaköy", "Tophane", "Kabataş")
                .baglantiEkle("Kabataş", "Taksim", 3, "F1")
                .hatEkle("T3", 2, "Kadıköy", "Moda");   // ağın geri kalanına bağlı değil
    }

    @Test
    void enHizliRotaMarmarayVeM2() {
        Rota rota = ag.enHizli("Sirkeci", "Taksim").orElseThrow();

        assertEquals(List.of("Sirkeci", "Yenikapı", "Vezneciler", "Haliç", "Şişhane", "Taksim"), rota.duraklar());
        assertEquals(12, rota.dakika());   // 4 + 2 + 2 + 2 + 2
        assertEquals(1, rota.aktarma());    // Yenikapı'da Marmaray'dan M2'ye
    }

    @Test
    void enAzDurakliRotaDahaYavasOlabilir() {
        Rota rota = ag.enAzDurak("Sirkeci", "Taksim").orElseThrow();

        assertEquals(List.of("Sirkeci", "Karaköy", "Tophane", "Kabataş", "Taksim"), rota.duraklar());
        assertEquals(4, rota.durakSayisi());           // en hızlı rotada 5
        assertEquals(15, rota.dakika());               // ama 12 yerine 15 dakika
        assertEquals(1, rota.aktarma());
    }

    @Test
    void rotaIkiYondeAyni() {
        assertEquals(12, ag.enHizli("Taksim", "Sirkeci").orElseThrow().dakika());
    }

    @Test
    void aktarmaSayisiHatDegisimleriniSayar() {
        Rota rota = ag.enHizli("Üsküdar", "Kabataş").orElseThrow();

        // Üsküdar –Marmaray– Sirkeci –T1– Kabataş: 4 + 12 = 16; M2 + F1 üzerinden 4+4+8+3 = 19
        assertEquals(16, rota.dakika());
        assertEquals(1, rota.aktarma());
        assertEquals(0, ag.enHizli("Yenikapı", "Osmanbey").orElseThrow().aktarma());
    }

    @Test
    void ayniDuraktaRotaSifirDakika() {
        Rota rota = ag.enHizli("Taksim", "Taksim").orElseThrow();

        assertEquals(List.of("Taksim"), rota.duraklar());
        assertEquals(0, rota.dakika());
    }

    @Test
    void bagliOlmayanParcayaRotaYok() {
        assertEquals(Optional.empty(), ag.enHizli("Taksim", "Moda"));
        assertEquals(Optional.empty(), ag.enAzDurak("Moda", "Sirkeci"));
    }

    @Test
    void bilinmeyenDurakVeGecersizSureReddedilir() {
        assertThrows(IllegalArgumentException.class, () -> ag.enHizli("Taksim", "Atlantis"));
        assertThrows(IllegalArgumentException.class, () -> ag.baglantiEkle("A", "B", 0, "X"));
        assertThrows(IllegalArgumentException.class, () -> ag.baglantiEkle("A", "B", -3, "X"));
    }

    @Test
    void kuyruktaKalanEskiKayitlarSonucuBozmaz() {
        // A'dan C'ye önce 10 dakikalık yol bulunur, sonra B üzerinden 2 dakikalık;
        // kuyrukta kalan 10'luk eski kayıt atlanmalı
        MetroAgi kucuk = new MetroAgi()
                .baglantiEkle("A", "C", 10, "X")
                .baglantiEkle("A", "B", 1, "Y")
                .baglantiEkle("B", "C", 1, "Y")
                .baglantiEkle("C", "D", 1, "Y");

        assertEquals(new Rota(List.of("A", "B", "C", "D"), 3, 0), kucuk.enHizli("A", "D").orElseThrow());
    }

    @Test
    void dijkstraRastgeleAglardaFloydWarshallIleAyniSureyiBulur() {
        Random rastgele = new Random(21);
        for (int deneme = 0; deneme < 100; deneme++) {
            int n = 2 + rastgele.nextInt(9);
            int[][] uzaklik = new int[n][n];
            for (int[] satir : uzaklik) {
                Arrays.fill(satir, Integer.MAX_VALUE / 2);   // "yol yok"; toplanınca taşmasın diye yarısı
            }
            MetroAgi rastgeleAg = new MetroAgi();
            for (int i = 0; i < n; i++) {
                uzaklik[i][i] = 0;
                // Bir halka, böylece her durak her durağa ulaşabiliyor; üstüne rastgele kısayollar
                ekle(rastgeleAg, uzaklik, i, (i + 1) % n, 1 + rastgele.nextInt(9));
                ekle(rastgeleAg, uzaklik, rastgele.nextInt(n), rastgele.nextInt(n), 1 + rastgele.nextInt(9));
            }
            // Floyd-Warshall: O(n³), bütün ikililer için en kısa yolu kaba ama kesin biçimde bulur
            for (int k = 0; k < n; k++) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        uzaklik[i][j] = Math.min(uzaklik[i][j], uzaklik[i][k] + uzaklik[k][j]);
                    }
                }
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    assertEquals(uzaklik[i][j], rastgeleAg.enHizli("D" + i, "D" + j).orElseThrow().dakika());
                }
            }
        }
    }

    private static void ekle(MetroAgi ag, int[][] uzaklik, int a, int b, int dakika) {
        if (a == b) {
            return;
        }
        ag.baglantiEkle("D" + a, "D" + b, dakika, "H");
        uzaklik[a][b] = Math.min(uzaklik[a][b], dakika);
        uzaklik[b][a] = uzaklik[a][b];
    }
}
