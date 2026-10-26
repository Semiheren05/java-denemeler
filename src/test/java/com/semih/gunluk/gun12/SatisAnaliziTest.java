package com.semih.gunluk.gun12;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SatisAnaliziTest {

    private static final int[] FIYATLAR = {1999, 4500, 4500, 12990, 34900};
    private static final int[] SATISLAR = {120, 340, 90, 410, 560, 30, 700};

    @ParameterizedTest
    @CsvSource({
            "5000,  2",   // iki tane 4500 var; en sağdaki
            "12990, 3",   // tam bütçe de uyar
            "1000,  -1",  // en ucuzu bile pahalı
            "99999, 4"
    })
    void butceyeUyanEnPahaliUrun(int butce, int beklenen) {
        assertEquals(beklenen, SatisAnalizi.butceyeUyanEnPahali(FIYATLAR, butce));
    }

    @Test
    void bosListedeIkiliAramaBulamaz() {
        assertEquals(-1, SatisAnalizi.butceyeUyanEnPahali(new int[0], 100));
    }

    @Test
    void hediyeCekiniTamKullananIkiUrun() {
        int[] fiyatlar = {1500, 2500, 4000, 6000, 8500};

        assertEquals(Optional.of(new FiyatCifti(0, 4, 1500, 8500)), SatisAnalizi.hedefToplamliCift(fiyatlar, 10000));
        assertEquals(Optional.of(new FiyatCifti(1, 2, 2500, 4000)), SatisAnalizi.hedefToplamliCift(fiyatlar, 6500));
    }

    @Test
    void ayniUrunIkiKezSayilmaz() {
        // 1500 + 1500 = 3000 ama listede tek bir 1500 var
        assertTrue(SatisAnalizi.hedefToplamliCift(new int[] {1500, 2500, 4000}, 3000).isEmpty());
    }

    @Test
    void enIyiUcGunlukDonem() {
        // 550, 840, 1060, 1000, 1290 → en iyisi son üç gün
        assertEquals(1290, SatisAnalizi.enIyiDonemSatisi(SATISLAR, 3));
        assertEquals(2250, SatisAnalizi.enIyiDonemSatisi(SATISLAR, SATISLAR.length));
    }

    @Test
    void pencereDiziyeSigmali() {
        assertThrows(IllegalArgumentException.class, () -> SatisAnalizi.enIyiDonemSatisi(SATISLAR, 0));
        assertThrows(IllegalArgumentException.class, () -> SatisAnalizi.enIyiDonemSatisi(SATISLAR, 8));
    }

    @ParameterizedTest
    @CsvSource({
            "1000, 3",   // 410 + 560 + 30; iki günde 1000'e ulaşılamıyor
            "700,  1",   // son gün tek başına
            "5000, 0"    // hiç ulaşılamıyor
    })
    void hedefeUlasilanEnKisaSure(long hedef, int beklenen) {
        assertEquals(beklenen, SatisAnalizi.hedefeEnKisaSure(SATISLAR, hedef));
    }

    @Test
    void negatifSatisYaDaHedefKabulEdilmez() {
        assertThrows(IllegalArgumentException.class, () -> SatisAnalizi.hedefeEnKisaSure(new int[] {5, -1}, 3));
        assertThrows(IllegalArgumentException.class, () -> SatisAnalizi.hedefeEnKisaSure(SATISLAR, 0));
    }

    // Aşağıdaki testler hızlı çözümleri yavaş ama açıkça doğru kaba kuvvet çözümleriyle
    // yüzlerce rastgele dizi üzerinde karşılaştırıyor. Sabit tohum: her çalıştırmada aynı diziler.

    @Test
    void ikiliAramaDogrusalAramaylaAyniSonucuVerir() {
        Random rastgele = new Random(42);
        for (int deneme = 0; deneme < 500; deneme++) {
            int[] fiyatlar = rastgeleDizi(rastgele, rastgele.nextInt(20), 1000);
            Arrays.sort(fiyatlar);
            int butce = rastgele.nextInt(1100);

            int beklenen = -1;
            for (int i = 0; i < fiyatlar.length; i++) {
                if (fiyatlar[i] <= butce) {
                    beklenen = i;
                }
            }
            assertEquals(beklenen, SatisAnalizi.butceyeUyanEnPahali(fiyatlar, butce));
        }
    }

    @Test
    void ikiIsaretciCiftVarsaMutlakaBulur() {
        Random rastgele = new Random(7);
        for (int deneme = 0; deneme < 500; deneme++) {
            int[] fiyatlar = rastgeleDizi(rastgele, 2 + rastgele.nextInt(15), 100);
            Arrays.sort(fiyatlar);
            int hedef = rastgele.nextInt(200);

            boolean varMi = false;
            for (int i = 0; i < fiyatlar.length; i++) {
                for (int j = i + 1; j < fiyatlar.length; j++) {
                    varMi |= fiyatlar[i] + fiyatlar[j] == hedef;
                }
            }
            Optional<FiyatCifti> bulunan = SatisAnalizi.hedefToplamliCift(fiyatlar, hedef);
            assertEquals(varMi, bulunan.isPresent());
            bulunan.ifPresent(cift -> {
                assertEquals(hedef, cift.toplam());
                assertTrue(cift.birinciIndeks() < cift.ikinciIndeks());
            });
        }
    }

    @Test
    void kayanPencerelerKabaKuvvetleAyniSonucuVerir() {
        Random rastgele = new Random(2026);
        for (int deneme = 0; deneme < 300; deneme++) {
            int[] satislar = rastgeleDizi(rastgele, 1 + rastgele.nextInt(25), 500);
            int gun = 1 + rastgele.nextInt(satislar.length);
            long hedef = 1 + rastgele.nextInt(3000);

            long enIyi = Long.MIN_VALUE;
            int enKisa = 0;
            for (int bas = 0; bas < satislar.length; bas++) {
                long toplam = 0;
                for (int son = bas; son < satislar.length; son++) {
                    toplam += satislar[son];
                    int uzunluk = son - bas + 1;
                    if (uzunluk == gun) {
                        enIyi = Math.max(enIyi, toplam);
                    }
                    if (toplam >= hedef && (enKisa == 0 || uzunluk < enKisa)) {
                        enKisa = uzunluk;
                    }
                }
            }
            assertEquals(enIyi, SatisAnalizi.enIyiDonemSatisi(satislar, gun));
            assertEquals(enKisa, SatisAnalizi.hedefeEnKisaSure(satislar, hedef));
        }
    }

    private static int[] rastgeleDizi(Random rastgele, int uzunluk, int enBuyuk) {
        int[] dizi = new int[uzunluk];
        for (int i = 0; i < uzunluk; i++) {
            dizi[i] = rastgele.nextInt(enBuyuk);
        }
        return dizi;
    }
}
