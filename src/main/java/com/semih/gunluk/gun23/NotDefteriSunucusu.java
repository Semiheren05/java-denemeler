package com.semih.gunluk.gun23;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Spring olmadan, JDK'nın içindeki {@link HttpServer} ile küçük bir REST API. Spring Boot'un
 * bizim yerimize yaptıklarını burada elle yapıyorum: yolu parçalamak, metoda göre dallanmak,
 * durum kodunu ve başlıkları yazmak, gövdeyi okuyup JSON üretmek.
 *
 * <pre>
 * GET    /notlar        200, bütün notlar
 * POST   /notlar        201 + Location, gövde: notun metni
 * GET    /notlar/{id}   200 ya da 404
 * DELETE /notlar/{id}   204 ya da 404
 * </pre>
 *
 * Her istek ayrı bir virtual thread'de işleniyor (Gün 14). Notlar {@link ConcurrentHashMap}'te,
 * numaralar {@link AtomicLong}'dan; aynı anda gelen istekler birbirini bozmuyor (Gün 13).
 */
public class NotDefteriSunucusu implements AutoCloseable {

    static final int EN_UZUN_NOT = 1_000;
    private static final String YOL = "/notlar";

    private final HttpServer sunucu;
    private final Map<Long, Not> notlar = new ConcurrentHashMap<>();
    private final AtomicLong sonNumara = new AtomicLong();

    /** @param port 0 verilirse işletim sistemi boş bir port seçer (testler için) */
    public NotDefteriSunucusu(int port) throws IOException {
        sunucu = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
        sunucu.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        sunucu.createContext(YOL, this::isle);
    }

    public NotDefteriSunucusu baslat() {
        sunucu.start();
        return this;
    }

    public int port() {
        return sunucu.getAddress().getPort();
    }

    @Override
    public void close() {
        sunucu.stop(0);
    }

    private void isle(HttpExchange istek) throws IOException {
        try {
            yonlendir(istek);
        } catch (RuntimeException e) {
            // Spring'deki son çare @ExceptionHandler(Exception.class) gibi: iç ayrıntı dışarı çıkmıyor
            cevap(istek, 500, Json.hata("Beklenmeyen bir hata oluştu"));
        } finally {
            istek.close();
        }
    }

    /** Yol ve metoda göre dallanma: Spring'de @GetMapping / @PostMapping'in yaptığı iş. */
    private void yonlendir(HttpExchange istek) throws IOException {
        String yol = istek.getRequestURI().getPath();
        String metot = istek.getRequestMethod();
        if (yol.equals(YOL) || yol.equals(YOL + "/")) {
            switch (metot) {
                case "GET" -> cevap(istek, 200, liste());
                case "POST" -> ekle(istek);
                default -> izinYok(istek, "GET, POST");
            }
        } else if (yol.startsWith(YOL + "/")) {
            Long id = numara(yol.substring(YOL.length() + 1));
            if (id == null) {
                cevap(istek, 400, Json.hata("Not numarası sayı olmalı"));
                return;
            }
            switch (metot) {
                case "GET" -> getir(istek, id);
                case "DELETE" -> sil(istek, id);
                default -> izinYok(istek, "GET, DELETE");
            }
        } else {
            // "/notlarim" gibi: bağlam önek eşleştiği için buraya düşüyor ama bizim yolumuz değil
            cevap(istek, 404, Json.hata("Böyle bir adres yok"));
        }
    }

    private void ekle(HttpExchange istek) throws IOException {
        String metin = new String(istek.getRequestBody().readAllBytes(), StandardCharsets.UTF_8).strip();
        if (metin.isEmpty()) {
            cevap(istek, 400, Json.hata("Not boş olamaz"));
            return;
        }
        if (metin.length() > EN_UZUN_NOT) {
            cevap(istek, 413, Json.hata("Not en fazla " + EN_UZUN_NOT + " karakter olabilir"));
            return;
        }
        Not not = new Not(sonNumara.incrementAndGet(), metin);
        notlar.put(not.id(), not);
        istek.getResponseHeaders().add("Location", YOL + "/" + not.id());
        cevap(istek, 201, not.json());
    }

    private void getir(HttpExchange istek, long id) throws IOException {
        Not not = notlar.get(id);
        if (not == null) {
            cevap(istek, 404, Json.hata(id + " numaralı not yok"));
        } else {
            cevap(istek, 200, not.json());
        }
    }

    private void sil(HttpExchange istek, long id) throws IOException {
        if (notlar.remove(id) == null) {
            cevap(istek, 404, Json.hata(id + " numaralı not yok"));
        } else {
            // 204 No Content: gövde yok, uzunluk -1
            istek.sendResponseHeaders(204, -1);
        }
    }

    private String liste() {
        return notlar.values().stream()
                .sorted(Comparator.comparingLong(Not::id))
                .map(Not::json)
                .collect(Collectors.joining(",", "[", "]"));
    }

    private static void izinYok(HttpExchange istek, String izinliler) throws IOException {
        // 405 cevabında hangi metotların geçerli olduğu Allow başlığıyla söylenmeli
        istek.getResponseHeaders().add("Allow", izinliler);
        cevap(istek, 405, Json.hata("Bu adres " + izinliler + " kabul ediyor"));
    }

    private static void cevap(HttpExchange istek, int durum, String json) throws IOException {
        byte[] govde = json.getBytes(StandardCharsets.UTF_8);
        istek.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        // Uzunluk karakter değil bayt: "ş" UTF-8'de 2 bayt
        istek.sendResponseHeaders(durum, govde.length);
        try (OutputStream cikis = istek.getResponseBody()) {
            cikis.write(govde);
        }
    }

    private static Long numara(String metin) {
        try {
            return Long.parseLong(metin);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
