package com.semih.gunluk.gun19;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Metnin uzunluğu {@code min} ile {@code max} arasında olmalı. Anotasyonların da parametresi olabiliyor. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Uzunluk {

    int min() default 0;

    int max() default Integer.MAX_VALUE;
}
