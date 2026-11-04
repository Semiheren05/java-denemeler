package com.semih.gunluk.gun20;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Birden fazla constructor varsa hangisinin kullanılacağını gösterir ({@code @Autowired} gibi). */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface Enjekte {
}
