package com.semih.gunluk.gun33;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KuralMotoruTest {

    private static final Basvuru IYI = new Basvuru("Ayşe", 30, 40_000, 1600, 5_000, 2, 100_000);

    @Test
    void uygunBasvuruOnaylanir() {
        Karar karar = KrediKurallari.standart().degerlendir(IYI);

        assertTrue(karar.onay());
        assertEquals(List.of(), karar.sebepler());
    }

    @Test
    void ilkHatadaDurmaz_butunSebepleriToplar() {
        Basvuru kotu = new Basvuru(" ", 17, 20_000, 900, 15_000, 0, 50_000);

        Karar karar = KrediKurallari.standart().degerlendir(kotu);

        assertFalse(karar.onay());
        assertEquals(List.of(
                "Ad boş olamaz",
                "18 yaşından küçükler başvuramaz",
                "Kredi notu düşük ve gelir/deneyim yetersiz",
                "Mevcut borç gelirin yarısını aşıyor"), karar.sebepler());
    }

    @ParameterizedTest
    @CsvSource({
            "1600, 10000, 0, true",    // notu iyi
            "900,  60000, 5, true",    // notu düşük ama yüksek gelir ve deneyim
            "900,  60000, 1, false",   // yüksek gelir ama deneyim yok
            "900,  30000, 5, false"})  // deneyim var ama gelir düşük
    void andVeOr_birlesikKosul(int not, int gelir, int yil, boolean beklenen) {
        assertEquals(beklenen, KrediKurallari.GUVENILIR.test(new Basvuru("x", 30, gelir, not, 0, yil, 1)));
    }

    @Test
    void negate_veNot() {
        Predicate<Basvuru> cocuk = KrediKurallari.RESIT.negate();
        Predicate<String> doluMu = Predicate.not(String::isBlank);

        assertTrue(cocuk.test(new Basvuru("x", 16, 0, 0, 0, 0, 0)));
        assertFalse(doluMu.test("   "));
        assertTrue(doluMu.test("Semih"));
    }

    @Test
    void yeniKural_motoruDegistirmedenEklenir_adlaCikarilir() {
        KuralMotoru motor = KrediKurallari.standart()
                .ekle(new Kural("ust-sinir", b -> b.istenenTutar() <= b.aylikGelir() * 10, "Tutar gelirin 10 katını aşamaz"));
        Basvuru buyukTutar = new Basvuru("Ayşe", 30, 40_000, 1600, 5_000, 2, 500_000);

        assertEquals(List.of("Tutar gelirin 10 katını aşamaz"), motor.degerlendir(buyukTutar).sebepler());
        assertTrue(motor.cikar("ust-sinir").degerlendir(buyukTutar).onay());
    }

    @Test
    void and_kisaDevreYapar_pahaliKontrolGereksizseCalismaz() {
        AtomicInteger buroSorgusu = new AtomicInteger();
        Predicate<Basvuru> krediBurosundaTemiz = b -> {
            buroSorgusu.incrementAndGet();   // dış servise gidiyormuş gibi
            return true;
        };
        Predicate<Basvuru> kosul = KrediKurallari.RESIT.and(krediBurosundaTemiz);   // ucuz olan önce

        kosul.test(new Basvuru("x", 16, 0, 0, 0, 0, 0));
        kosul.test(IYI);

        assertEquals(1, buroSorgusu.get());
    }

    @Test
    void andThen_ve_compose_sirasi() {
        Function<Integer, Integer> ikiKati = x -> x * 2;
        Function<Integer, Integer> onEkle = x -> x + 10;

        assertEquals(30, ikiKati.andThen(onEkle).apply(10));   // önce ×2, sonra +10
        assertEquals(40, ikiKati.compose(onEkle).apply(10));   // önce +10, sonra ×2
        assertEquals(160, KrediKurallari.HAM_PUAN.apply(IYI));
    }

    @Test
    void unaryOperatorZinciri_duzeltmelerSirayla() {
        UnaryOperator<Integer> zincir = KrediKurallari.duzeltmeler(List.of(
                p -> p + 20,                // düzenli maaş
                p -> p - 15,                // son 6 ayda gecikme
                p -> Math.min(p, 200)));    // tavan

        assertEquals(165, zincir.apply(160));
        assertEquals(200, zincir.apply(250));
        assertEquals(160, KrediKurallari.duzeltmeler(List.of()).apply(160));   // boş liste: identity
    }

    @ParameterizedTest
    @CsvSource({"190, 12, 38.0", "190, 36, 40.0", "160, 12, 42.0", "100, 24, 48.0"})
    void biFunction_faizTablosu(int puan, int vade, double beklenen) {
        assertEquals(beklenen, KrediKurallari.FAIZ.apply(puan, vade).doubleValue());
    }

    @Test
    void consumerAndThen_dinleyicilerSirayla() {
        List<String> kayit = new ArrayList<>();
        KuralMotoru motor = KrediKurallari.standart()
                .dinle(k -> kayit.add("denetim:" + k.basvuran()))
                .dinle(k -> kayit.add("bildirim:" + (k.onay() ? "onay" : "red")));

        motor.degerlendir(IYI);

        assertEquals(List.of("denetim:Ayşe", "bildirim:onay"), kayit);
    }
}
