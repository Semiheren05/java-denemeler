package com.semih.gunluk.gun38;

import java.util.concurrent.CompletableFuture;

/** Dış servisler bloklamadan cevap veriyor: sonuç hemen değil, "ileride" ({@code CompletableFuture}). */
@FunctionalInterface
public interface UcusServisi {

    CompletableFuture<Ucus> ara(String nereden, String nereye);
}
