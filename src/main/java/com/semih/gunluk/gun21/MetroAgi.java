package com.semih.gunluk.gun21;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;

/**
 * Bir raylı sistem ağı: duraklar düğüm, aralarındaki yollar kenar (ağırlıklı, yönsüz bir
 * <b>graf</b>). Komşuluk listesiyle tutuluyor: her durağın gidebildiği yerlerin listesi.
 *
 * <ul>
 *   <li>{@link #enHizli}: <b>Dijkstra</b> algoritması, en kısa <em>süre</em>.</li>
 *   <li>{@link #enAzDurak}: <b>genişlik öncelikli arama (BFS)</b>, en az <em>durak</em>.</li>
 * </ul>
 * İkisi her zaman aynı rotayı vermiyor: az duraklı bir yol, yavaş bir hat yüzünden daha uzun sürebilir.
 */
public class MetroAgi {

    private final Map<String, List<Baglanti>> komsular = new HashMap<>();

    /** İki yönlü bağlantı ekler. */
    public MetroAgi baglantiEkle(String a, String b, int dakika, String hat) {
        if (dakika <= 0) {
            // Dijkstra negatif ya da sıfır ağırlıkla doğru çalışmaz; iki durak arası hep zaman alır
            throw new IllegalArgumentException("Süre pozitif olmalı: " + a + " – " + b + " = " + dakika);
        }
        komsular.computeIfAbsent(a, d -> new ArrayList<>()).add(new Baglanti(b, dakika, hat));
        komsular.computeIfAbsent(b, d -> new ArrayList<>()).add(new Baglanti(a, dakika, hat));
        return this;
    }

    /** Durakları sırayla, aralarında eşit süre olan bir hat ekler. */
    public MetroAgi hatEkle(String hat, int durakArasiDakika, String... duraklar) {
        for (int i = 1; i < duraklar.length; i++) {
            baglantiEkle(duraklar[i - 1], duraklar[i], durakArasiDakika, hat);
        }
        return this;
    }

    /**
     * Dijkstra. Kuyruktan hep o ana kadar en yakın durak çıkıyor; ağırlıklar pozitif olduğu için
     * o durağa daha kısa bir yol bulunamaz, mesafesi kesinleşmiş oluyor. O((D + B) log D).
     *
     * @return ulaşılamıyorsa boş
     */
    public Optional<Rota> enHizli(String baslangic, String varis) {
        durakVarMi(baslangic);
        durakVarMi(varis);
        Map<String, Integer> mesafe = new HashMap<>();
        Map<String, Adim> geldigiYer = new HashMap<>();
        PriorityQueue<Aday> kuyruk = new PriorityQueue<>(Comparator.comparingInt(Aday::mesafe));
        mesafe.put(baslangic, 0);
        kuyruk.add(new Aday(baslangic, 0));

        while (!kuyruk.isEmpty()) {
            Aday aday = kuyruk.poll();
            if (aday.mesafe() > mesafe.get(aday.durak())) {
                // Bu durağa sonradan daha kısa bir yol bulunmuş; kuyrukta kalan eski kayıt, atla
                continue;
            }
            if (aday.durak().equals(varis)) {
                break;
            }
            for (Baglanti b : komsular.get(aday.durak())) {
                int yeni = aday.mesafe() + b.dakika();
                if (yeni < mesafe.getOrDefault(b.hedef(), Integer.MAX_VALUE)) {
                    mesafe.put(b.hedef(), yeni);
                    geldigiYer.put(b.hedef(), new Adim(aday.durak(), b.hat(), b.dakika()));
                    kuyruk.add(new Aday(b.hedef(), yeni));
                }
            }
        }
        return mesafe.containsKey(varis) ? Optional.of(rotaKur(baslangic, varis, geldigiYer)) : Optional.empty();
    }

    /**
     * BFS: önce 1 durak ötedekiler, sonra 2 durak ötedekiler... İlk ulaşıldığı an en az durakla
     * ulaşılmış oluyor. Süreye bakmıyor. O(D + B), sıradan bir {@code ArrayDeque} kuyruğuyla.
     */
    public Optional<Rota> enAzDurak(String baslangic, String varis) {
        durakVarMi(baslangic);
        durakVarMi(varis);
        Map<String, Adim> geldigiYer = new HashMap<>();
        Deque<String> kuyruk = new ArrayDeque<>();
        kuyruk.add(baslangic);
        geldigiYer.put(baslangic, null);

        while (!kuyruk.isEmpty()) {
            String durak = kuyruk.poll();
            if (durak.equals(varis)) {
                return Optional.of(rotaKur(baslangic, varis, geldigiYer));
            }
            for (Baglanti b : komsular.get(durak)) {
                if (!geldigiYer.containsKey(b.hedef())) {
                    geldigiYer.put(b.hedef(), new Adim(durak, b.hat(), b.dakika()));
                    kuyruk.add(b.hedef());
                }
            }
        }
        return Optional.empty();
    }

    public int durakSayisi() {
        return komsular.size();
    }

    /** Varıştan geriye, "nereden geldim" bilgisini izleyerek yolu çıkarır. */
    private static Rota rotaKur(String baslangic, String varis, Map<String, Adim> geldigiYer) {
        List<String> duraklar = new ArrayList<>();
        List<String> hatlar = new ArrayList<>();
        int dakika = 0;
        for (String durak = varis; !durak.equals(baslangic); durak = geldigiYer.get(durak).onceki()) {
            Adim adim = geldigiYer.get(durak);
            duraklar.add(durak);
            hatlar.add(adim.hat());
            dakika += adim.dakika();
        }
        duraklar.add(baslangic);
        Collections.reverse(duraklar);
        int aktarma = 0;
        for (int i = 1; i < hatlar.size(); i++) {
            if (!hatlar.get(i).equals(hatlar.get(i - 1))) {
                aktarma++;
            }
        }
        return new Rota(List.copyOf(duraklar), dakika, aktarma);
    }

    private void durakVarMi(String durak) {
        if (!komsular.containsKey(durak)) {
            throw new IllegalArgumentException("Bilinmeyen durak: " + durak);
        }
    }

    private record Aday(String durak, int mesafe) {
    }

    private record Adim(String onceki, String hat, int dakika) {
    }
}
