package com.semih.gunluk.gun19;

/**
 * Örnek bir form. Kurallar alanların yanında, bildirimsel (declarative) olarak duruyor;
 * doğrulama kodu ayrı bir yerde, {@link Dogrulayici}'da. Anotasyonlar record bileşenlerine
 * yazılınca, {@code @Target(FIELD)} sayesinde record'un private alanlarına da geçiyor.
 */
public record UyelikFormu(
        @Zorunlu @Uzunluk(min = 2, max = 50) String ad,
        @Zorunlu @Desen(value = "[^@\\s]+@[^@\\s]+\\.[a-z]{2,}", mesaj = "geçerli bir e-posta olmalı") String eposta,
        @Aralik(min = 18, max = 120) int yas,
        @Desen(value = "\\d{5}", mesaj = "5 haneli olmalı") String postaKodu) {
}
