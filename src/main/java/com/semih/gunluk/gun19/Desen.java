package com.semih.gunluk.gun19;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Metin düzenli ifadeye bütünüyle uymalı. {@code value} adlı parametre {@code @Desen("...")} diye kısaca yazılabiliyor. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Desen {

    String value();

    String mesaj() default "biçimi geçersiz";
}
