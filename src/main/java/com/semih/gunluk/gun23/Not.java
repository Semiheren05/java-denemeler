package com.semih.gunluk.gun23;

public record Not(long id, String metin) {

    /** Elle JSON: kütüphane kullanmadan, ama kaçış karakterlerini atlamadan. */
    String json() {
        return "{\"id\":" + id + ",\"metin\":" + Json.metin(metin) + "}";
    }
}
