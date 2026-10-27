package com.semih.gunluk.gun17;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Örnek numaralar algoritmaya uyan, kimseye ait olmayan test numaraları. */
class DogrulayicilarTest {

    // --- T.C. kimlik no ---

    @ParameterizedTest
    @ValueSource(strings = {"10000000146", "12345678950", "11111111110"})
    void kontrolHaneleriTutanNumaralarGecerli(String no) {
        assertTrue(TcKimlikNo.gecerliMi(no));
    }

    @ParameterizedTest
    @CsvSource({
            "10000000147, son hane yanlış",
            "10000000136, 10. hane yanlış",
            "01234567890, sıfırla başlıyor",
            "1000000014,  10 hane",
            "100000001460, 12 hane",
            "1000000014a, harf var"
    })
    void kurallaraUymayanNumaralarGecersiz(String no, String neden) {
        assertFalse(TcKimlikNo.gecerliMi(no), neden);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void bosTcGecersiz(String no) {
        assertFalse(TcKimlikNo.gecerliMi(no));
    }

    // --- IBAN ---

    @ParameterizedTest
    @ValueSource(strings = {
            "TR330006100519786457841326",
            "TR33 0006 1005 1978 6457 8413 26",
            "tr330006100519786457841326",
            "GB82WEST12345698765432"
    })
    void gecerliIbanlar(String iban) {
        assertTrue(Iban.gecerliMi(iban));
    }

    @ParameterizedTest
    @CsvSource({
            "TR340006100519786457841326, kontrol haneleri yanlış",
            "TR330006100519786457841327, son hane değişmiş",
            "TR33000610051978645784132,  TR için 26 karakter olmalı",
            "TR33-0006-1005-1978-6457-84, izin verilmeyen karakter",
            "3300TR06100519786457841326, ülke kodu başta değil"
    })
    void gecersizIbanlar(String iban, String neden) {
        assertFalse(Iban.gecerliMi(iban), neden);
    }

    @Test
    void ibanDortluGruplaraBolunur() {
        assertEquals("TR33 0006 1005 1978 6457 8413 26", Iban.bicimle("tr330006100519786457841326"));
        assertThrows(IllegalArgumentException.class, () -> Iban.bicimle("TR00"));
    }

    @Test
    void mod97AdimAdimHesaplanir() {
        // 3214282912345698765432161182 mod 97 = 1 (Wikipedia'daki GB örneğinin yeniden dizilmiş hâli)
        assertEquals(1, Iban.mod97("WEST12345698765432GB82"));
        assertEquals(0, Iban.mod97("97"));
        assertEquals(96, Iban.mod97("193"));
    }

    // --- Telefon ---

    @ParameterizedTest
    @ValueSource(strings = {
            "05321234567",
            "0532 123 45 67",
            "+90 (532) 123-4567",
            "0090 532 123 45 67",
            "5321234567",
            "+90.532.123.45.67"
    })
    void farkliYazimlarAyniNumarayaDoner(String girdi) {
        assertEquals(Optional.of("+905321234567"), Telefon.normalle(girdi));
    }

    @ParameterizedTest
    @CsvSource({
            "0212 123 45 67, sabit hat",
            "0532 123 45 6,   eksik hane",
            "0532 123 45 678, fazla hane",
            "+1 532 123 4567, başka ülke",
            "0532 ABC 45 67,  harf var"
    })
    void cepNumarasiOlmayanlarReddedilir(String girdi, String neden) {
        assertEquals(Optional.empty(), Telefon.normalle(girdi), neden);
    }

    @Test
    void adlandirilmisGruplarlaOkunurBicim() {
        assertEquals(Optional.of("0532 123 45 67"), Telefon.okunur("+905321234567"));
        assertEquals(Optional.empty(), Telefon.okunur(null));
    }
}
