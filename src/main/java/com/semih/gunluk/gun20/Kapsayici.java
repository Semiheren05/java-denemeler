package com.semih.gunluk.gun20;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SequencedSet;
import java.util.stream.Collectors;

/**
 * Spring'in yaptığının çok küçük bir benzeri: bir <b>bağımlılık enjeksiyonu (DI) kapsayıcısı</b>.
 * Sınıflar ihtiyaç duydukları nesneleri {@code new} ile kendileri oluşturmuyor, constructor'da
 * istiyor. Kapsayıcı constructor'ın parametre tiplerine reflection ile bakıp önce onları
 * kuruyor, sonra sınıfı. Her tip bir kez kuruluyor (singleton).
 *
 * <pre>
 * Kapsayici kapsayici = new Kapsayici();
 * kapsayici.bagla(SiparisDeposu.class, BellekSiparisDeposu.class);
 * SiparisServisi servis = kapsayici.getir(SiparisServisi.class);
 * </pre>
 */
public class Kapsayici {

    private final Map<Class<?>, Class<?>> baglamalar = new HashMap<>();
    private final Map<Class<?>, Object> tekiller = new HashMap<>();

    /** Arayüz istendiğinde hangi sınıfın kurulacağı. */
    public <T> Kapsayici bagla(Class<T> arayuz, Class<? extends T> uygulama) {
        baglamalar.put(arayuz, uygulama);
        return this;
    }

    /** Elle kurulmuş bir nesneyi kaydeder (ayar değerleri, dış kütüphane nesneleri için). */
    public <T> Kapsayici kaydet(Class<T> tip, T nesne) {
        tekiller.put(tip, nesne);
        return this;
    }

    public <T> T getir(Class<T> tip) {
        return kur(tip, new LinkedHashSet<>());
    }

    public int kurulanSayisi() {
        return tekiller.size();
    }

    /**
     * @param yol şu an kurulmakta olan tipler, kurulma sırasıyla. Bir tip yolda zaten varsa
     *            döngü var demek: A, B'yi istiyor, B de A'yı. Sonsuz özyineleme yerine
     *            "A → B → A" diye anlaşılır bir hata.
     */
    private <T> T kur(Class<T> tip, SequencedSet<Class<?>> yol) {
        Object hazir = tekiller.get(tip);
        if (hazir != null) {
            return tip.cast(hazir);
        }
        if (!yol.add(tip)) {
            throw new KapsayiciHatasi("Döngüsel bağımlılık: " + yolMetni(yol, tip));
        }
        Class<?> uygulama = baglamalar.getOrDefault(tip, tip);
        if (uygulama.isInterface() || Modifier.isAbstract(uygulama.getModifiers())) {
            throw new KapsayiciHatasi(tip.getSimpleName() + " için bağlama yok: bagla("
                    + tip.getSimpleName() + ".class, <uygulama>.class) eksik");
        }
        Constructor<?> constructor = constructorSec(uygulama);
        Object[] argumanlar = Arrays.stream(constructor.getParameterTypes())
                .map(parametre -> kur(parametre, yol))
                .toArray();
        T nesne = tip.cast(yeniNesne(constructor, argumanlar));
        yol.removeLast();
        tekiller.put(tip, nesne);
        return nesne;
    }

    /** Tek public constructor varsa o; birden fazlaysa {@link Enjekte} işaretli olan. */
    private static Constructor<?> constructorSec(Class<?> sinif) {
        Constructor<?>[] adaylar = sinif.getConstructors();
        if (adaylar.length == 1) {
            return adaylar[0];
        }
        List<Constructor<?>> isaretli = Arrays.stream(adaylar)
                .filter(c -> c.isAnnotationPresent(Enjekte.class))
                .toList();
        if (isaretli.size() != 1) {
            throw new KapsayiciHatasi(sinif.getSimpleName() + ": " + adaylar.length
                    + " public constructor var, hangisinin kullanılacağı @Enjekte ile tek birine işaretlenmeli");
        }
        return isaretli.get(0);
    }

    private static Object yeniNesne(Constructor<?> constructor, Object[] argumanlar) {
        try {
            return constructor.newInstance(argumanlar);
        } catch (InvocationTargetException e) {
            // Constructor'ın kendi fırlattığı hata InvocationTargetException'ın içinde geliyor
            throw new KapsayiciHatasi(constructor.getDeclaringClass().getSimpleName() + " kurulamadı: "
                    + e.getCause().getMessage(), e.getCause());
        } catch (ReflectiveOperationException e) {
            throw new KapsayiciHatasi(constructor.getDeclaringClass().getSimpleName() + " kurulamadı", e);
        }
    }

    private static String yolMetni(SequencedSet<Class<?>> yol, Class<?> tekrar) {
        List<Class<?>> sira = new ArrayList<>(yol);
        List<Class<?>> dongu = sira.subList(sira.indexOf(tekrar), sira.size());
        return dongu.stream().map(Class::getSimpleName).collect(Collectors.joining(" → ")) + " → " + tekrar.getSimpleName();
    }
}
