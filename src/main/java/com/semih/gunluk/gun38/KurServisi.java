package com.semih.gunluk.gun38;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface KurServisi {

    CompletableFuture<BigDecimal> tlKarsiligi(String paraBirimi);
}
