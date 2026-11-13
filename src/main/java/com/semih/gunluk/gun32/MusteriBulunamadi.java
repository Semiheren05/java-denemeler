package com.semih.gunluk.gun32;

public class MusteriBulunamadi extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MusteriBulunamadi(String id) {
        super("Müşteri bulunamadı: " + id);
    }
}
