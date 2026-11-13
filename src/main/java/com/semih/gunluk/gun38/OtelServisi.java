package com.semih.gunluk.gun38;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface OtelServisi {

    CompletableFuture<Otel> ara(String sehir);
}
