package com.semih.gunluk.gun31;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TekilTest {

    private static final int THREAD_SAYISI = 8;

    @BeforeEach
    void sifirla() {
        HataliTekil.sifirla();
        CiftKontrolluTekil.sifirla();
    }

    /** 8 thread'i bir bariyerde bekletip aynı anda saldırtır; kaç farklı örnek gördüklerini döner. */
    private static Set<Object> ayniAndaIste(Supplier<Object> tekil) throws InterruptedException {
        Set<Object> gorulenler = ConcurrentHashMap.newKeySet();   // Object.equals = kimlik karşılaştırması
        CyclicBarrier bariyer = new CyclicBarrier(THREAD_SAYISI);
        List<Thread> threadler = new ArrayList<>();
        for (int i = 0; i < THREAD_SAYISI; i++) {
            Thread t = new Thread(() -> {
                try {
                    bariyer.await();
                    gorulenler.add(tekil.get());
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            });
            threadler.add(t);
            t.start();
        }
        for (Thread t : threadler) {
            t.join();
        }
        return gorulenler;
    }

    @Test
    void enumTekil_herYerdeAyniNesneVeDurum() {
        Ayarlar bir = Ayarlar.INSTANCE;
        Ayarlar iki = Ayarlar.valueOf("INSTANCE");

        bir.ayarla("tema", "acik");

        assertSame(bir, iki);
        assertEquals("acik", iki.getir("tema"));
        bir.ayarla("tema", "koyu");   // küresel durumu geri al: yoksa sonraki testlere sızar
    }

    @Test
    void enumTekil_yansimaIleKopyalanamaz() throws Exception {
        Constructor<?> kurucu = Ayarlar.class.getDeclaredConstructors()[0];
        kurucu.setAccessible(true);

        assertThrows(IllegalArgumentException.class, () -> kurucu.newInstance("KOPYA", 1));
    }

    @Test
    void enumTekil_serilestirmedenSonraDaAyniNesne() throws Exception {
        ByteArrayOutputStream bayt = new ByteArrayOutputStream();
        try (ObjectOutputStream cikis = new ObjectOutputStream(bayt)) {
            cikis.writeObject(Ayarlar.INSTANCE);
        }
        Object geriOkunan;
        try (ObjectInputStream giris = new ObjectInputStream(new ByteArrayInputStream(bayt.toByteArray()))) {
            geriOkunan = giris.readObject();
        }

        assertSame(Ayarlar.INSTANCE, geriOkunan);
    }

    @Test
    void sinifTekil_yansimaIleKirilabilir() throws Exception {
        Constructor<TembelTekil> kurucu = TembelTekil.class.getDeclaredConstructor();
        kurucu.setAccessible(true);   // private kurucu yansımaya karşı koruma değil

        TembelTekil kopya = kurucu.newInstance();

        assertNotSame(TembelTekil.ornek(), kopya);
    }

    @Test
    void holderDeyimi_tembelVeTekOrnek() {
        int once = TembelTekil.KURULUM_SAYISI.get();

        TembelTekil.surum();   // dış sınıfı kullandık ama Tutucu yüklenmedi

        assertEquals(once, TembelTekil.KURULUM_SAYISI.get());
        assertSame(TembelTekil.ornek(), TembelTekil.ornek());
    }

    @Test
    void holderDeyimi_threadlerArasindaTekOrnek() throws InterruptedException {
        assertEquals(1, ayniAndaIste(TembelTekil::ornek).size());
    }

    @Test
    void hataliTekil_ayniAndaIstenirseBirdenFazlaKurulur() throws InterruptedException {
        Set<Object> gorulenler = ayniAndaIste(HataliTekil::ornek);

        assertTrue(gorulenler.size() > 1, "farklı örnek sayısı: " + gorulenler.size());
        assertTrue(HataliTekil.KURULUM_SAYISI.get() > 1);
    }

    @Test
    void ciftKontrolluKilit_tamOlarakBirKezKurar() throws InterruptedException {
        Set<Object> gorulenler = ayniAndaIste(CiftKontrolluTekil::ornek);

        assertEquals(1, gorulenler.size());
        assertEquals(1, CiftKontrolluTekil.KURULUM_SAYISI.get());
    }

    @Test
    void globalTekil_durumTestlerArasindaTasinir() {
        long ilk = Long.parseLong(SiparisNumaralari.yeniNumaraGlobal().substring(3));
        long ikinci = Long.parseLong(SiparisNumaralari.yeniNumaraGlobal().substring(3));

        // "SP-1" bekleyemiyoruz: bu sayaca daha önce hangi testin dokunduğuna bağlı
        assertEquals(ilk + 1, ikinci);
    }

    @Test
    void bagimlilikEnjeksiyonu_testeOzelUreticiVerilir() {
        SiparisNumaralari gercek = new SiparisNumaralari(new SiraliNumaraUretici(1000));
        SiparisNumaralari sahte = new SiparisNumaralari(() -> 7);   // sahte üretici: tek satır lambda

        assertEquals("SP-1001", gercek.yeniNumara());
        assertEquals("SP-1002", gercek.yeniNumara());
        assertEquals("SP-7", sahte.yeniNumara());
    }
}
