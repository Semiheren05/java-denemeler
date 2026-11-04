package com.semih.gunluk.gun19;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Alan boş olamaz: {@code null} değil, metinse boşluktan ibaret değil.
 *
 * <p>{@code RUNTIME}: anotasyon derlenmiş sınıfta kalıyor ve çalışırken reflection ile
 * okunabiliyor. Varsayılan {@code CLASS} olsaydı {@code getAnnotation} hep {@code null} dönerdi.
 * {@code FIELD}: sadece alanlara (ve record bileşenlerine) yazılabiliyor.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Zorunlu {
}
