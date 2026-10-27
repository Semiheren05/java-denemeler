package com.semih.gunluk.gun15;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Observer deseni: olayı üreten (depo), olayı dinleyenleri (e-posta, panel, sipariş sistemi)
 * tanımıyor. Sadece "şu oldu" diye yayınlıyor; ilgilenen abone oluyor.
 *
 * <p>Dinleyiciler olay türüne göre abone oluyor: {@code aboneOl(StokBitti.class, ...)} sadece
 * stok bitince çağrılıyor, {@code aboneOl(StokOlayi.class, ...)} hepsinde.
 *
 * @param <O> bu yayının taşıdığı olayların ortak tipi
 */
public class OlayYayini<O> {

    /**
     * {@link CopyOnWriteArrayList}: bir dinleyici olay sırasında yeni abonelik açar ya da kendi
     * aboneliğini kapatırsa, düz {@code ArrayList} üzerinde dönerken
     * {@code ConcurrentModificationException} alırdık. Bu liste her değişiklikte kopyalanıyor,
     * dönen döngü eski kopyayı görmeye devam ediyor. Okuması çok, yazması az listeler için uygun.
     */
    private final List<Kayit<? extends O>> kayitlar = new CopyOnWriteArrayList<>();

    /**
     * @param tur      dinlenecek olay tipi; alt tipler de gelir
     * @param dinleyici {@code ? super T}: {@code StokOlayi} kabul eden bir dinleyici,
     *                  {@code StokBitti} olaylarını da dinleyebilir
     */
    public <T extends O> Abonelik aboneOl(Class<T> tur, Consumer<? super T> dinleyici) {
        Kayit<T> kayit = new Kayit<>(tur, dinleyici);
        kayitlar.add(kayit);
        return () -> kayitlar.remove(kayit);
    }

    /**
     * Olayı ilgilenen bütün dinleyicilere, abone oldukları sırayla iletir. Bir dinleyicinin
     * hatası diğerlerini durdurmuyor; hatalar toplanıp geri veriliyor.
     *
     * @return dinleyicilerin fırlattığı hatalar (yoksa boş)
     */
    public List<RuntimeException> yayinla(O olay) {
        List<RuntimeException> hatalar = new ArrayList<>();
        for (Kayit<? extends O> kayit : kayitlar) {
            try {
                kayit.ilet(olay);
            } catch (RuntimeException e) {
                hatalar.add(e);
            }
        }
        return hatalar;
    }

    public int aboneSayisi() {
        return kayitlar.size();
    }

    private record Kayit<T>(Class<T> tur, Consumer<? super T> dinleyici) {

        void ilet(Object olay) {
            if (tur.isInstance(olay)) {
                // isInstance kontrol edildi, cast güvenli
                dinleyici.accept(tur.cast(olay));
            }
        }
    }
}
