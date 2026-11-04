package com.semih.gunluk.gun19;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Sayı {@code min} ile {@code max} arasında olmalı (ikisi de dahil). */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Aralik {

    long min();

    long max();
}
