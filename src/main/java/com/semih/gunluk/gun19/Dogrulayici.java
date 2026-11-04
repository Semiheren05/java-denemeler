package com.semih.gunluk.gun19;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Spring'in {@code @Valid}'inin küçük bir benzeri: nesnenin alanlarındaki anotasyonları
 * <b>reflection</b> ile okuyup kuralları uyguluyor. Hangi sınıfın hangi alanı olduğunu
 * derlerken bilmiyor; çalışırken {@code getDeclaredFields()} ile soruyor.
 *
 * <p>Reflection yavaş sayılır, bu yüzden her sınıfın alan listesi bir kez çıkarılıp
 * saklanıyor; aynı sınıftan ikinci nesnede tekrar taranmıyor.
 */
public class Dogrulayici {

    private final Map<Class<?>, List<Field>> alanlar = new ConcurrentHashMap<>();
    private final Map<String, Pattern> desenler = new ConcurrentHashMap<>();

    public List<Ihlal> dogrula(Object nesne) {
        if (nesne == null) {
            throw new IllegalArgumentException("Doğrulanacak nesne boş olamaz");
        }
        List<Ihlal> ihlaller = new ArrayList<>();
        for (Field alan : alanlar.computeIfAbsent(nesne.getClass(), Dogrulayici::kuralliAlanlar)) {
            Object deger = oku(alan, nesne);
            String ad = alan.getName();

            if (alan.isAnnotationPresent(Zorunlu.class) && bosMu(deger)) {
                ihlaller.add(new Ihlal(ad, "boş olamaz"));
                continue;   // boş bir değerin uzunluğuna ya da biçimine bakmanın anlamı yok
            }
            if (deger == null) {
                continue;   // zorunlu değilse boş bırakılabilir
            }
            Uzunluk uzunluk = alan.getAnnotation(Uzunluk.class);
            if (uzunluk != null && deger instanceof CharSequence metin
                    && (metin.length() < uzunluk.min() || metin.length() > uzunluk.max())) {
                ihlaller.add(new Ihlal(ad, uzunluk.min() + " ile " + uzunluk.max() + " karakter arasında olmalı"));
            }
            Aralik aralik = alan.getAnnotation(Aralik.class);
            if (aralik != null && deger instanceof Number sayi
                    && (sayi.longValue() < aralik.min() || sayi.longValue() > aralik.max())) {
                ihlaller.add(new Ihlal(ad, aralik.min() + " ile " + aralik.max() + " arasında olmalı"));
            }
            Desen desen = alan.getAnnotation(Desen.class);
            if (desen != null && deger instanceof CharSequence metin
                    && !desenler.computeIfAbsent(desen.value(), Pattern::compile).matcher(metin).matches()) {
                ihlaller.add(new Ihlal(ad, desen.mesaj()));
            }
        }
        return ihlaller;
    }

    /** Kaç sınıfın alanları taranıp saklandı (testler için). */
    int taranmisSinifSayisi() {
        return alanlar.size();
    }

    /**
     * Bu dört anotasyondan en az birini taşıyan, static olmayan alanlar. {@code getDeclaredFields()}
     * sıra garantisi vermiyor; record'larda bileşen sırası garanti, o yüzden ona göre diziliyor.
     */
    private static List<Field> kuralliAlanlar(Class<?> sinif) {
        List<Field> sonuc = new ArrayList<>();
        for (Field alan : siraliAlanlar(sinif)) {
            boolean kuralli = alan.isAnnotationPresent(Zorunlu.class) || alan.isAnnotationPresent(Uzunluk.class)
                    || alan.isAnnotationPresent(Aralik.class) || alan.isAnnotationPresent(Desen.class);
            if (kuralli && !Modifier.isStatic(alan.getModifiers())) {
                // private alanı okuyabilmek için. Modül sistemi dışarıdan kapalı paketlerde buna izin vermez.
                alan.setAccessible(true);
                sonuc.add(alan);
            }
        }
        return List.copyOf(sonuc);
    }

    private static List<Field> siraliAlanlar(Class<?> sinif) {
        if (!sinif.isRecord()) {
            return List.of(sinif.getDeclaredFields());
        }
        List<Field> sirali = new ArrayList<>();
        for (RecordComponent bilesen : sinif.getRecordComponents()) {
            try {
                sirali.add(sinif.getDeclaredField(bilesen.getName()));
            } catch (NoSuchFieldException e) {
                throw new IllegalStateException(e);   // her record bileşeninin bir alanı vardır
            }
        }
        return sirali;
    }

    private static Object oku(Field alan, Object nesne) {
        try {
            return alan.get(nesne);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(alan.getName() + " okunamadı", e);
        }
    }

    private static boolean bosMu(Object deger) {
        return deger == null || deger instanceof CharSequence metin && metin.toString().isBlank();
    }
}
