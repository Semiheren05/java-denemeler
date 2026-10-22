package com.semih.gunluk.gun08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UrunCsvOkuyucuTest {

    // JUnit her test için boş bir geçici klasör açar ve sonra siler
    @TempDir
    Path klasor;

    @Test
    void gecerliDosyaOkunur() throws Exception {
        Path dosya = yaz("""
                kod;ad;fiyat;stok
                U001;Kalem;12,50;100
                U002;Defter;45.90;20
                """);

        OkumaSonucu sonuc = UrunCsvOkuyucu.oku(dosya);

        assertEquals(List.of(
                new Urun("U001", "Kalem", new BigDecimal("12.50"), 100),
                new Urun("U002", "Defter", new BigDecimal("45.90"), 20)
        ), sonuc.urunler());
        assertTrue(sonuc.hatasizMi());
    }

    @Test
    @DisplayName("Hatalı satırlar atlanır, satır numarasıyla raporlanır")
    void hataliSatirlarAtlanir() throws Exception {
        Path dosya = yaz("""
                kod;ad;fiyat;stok
                U001;Kalem;12,50;100
                U002;Defter;bedava;20
                U003;Silgi;5
                U004;Cetvel;8;10
                """);

        OkumaSonucu sonuc = UrunCsvOkuyucu.oku(dosya);

        assertEquals(List.of("U001", "U004"), sonuc.urunler().stream().map(Urun::kod).toList());
        assertEquals(List.of(3, 4), sonuc.hatalar().stream().map(HataliSatir::satirNo).toList());
        assertEquals("Fiyat sayı değil: 'bedava'", sonuc.hatalar().get(0).neden());
        assertFalse(sonuc.hatasizMi());
    }

    @Test
    void bosSatirlarYokSayilir() throws Exception {
        Path dosya = yaz("kod;ad;fiyat;stok\n\nU001;Kalem;1;1\n   \n");

        OkumaSonucu sonuc = UrunCsvOkuyucu.oku(dosya);

        assertEquals(1, sonuc.urunler().size());
        assertTrue(sonuc.hatasizMi());
    }

    @Test
    void sadeceBaslikOlanDosyaBosSonucVerir() throws Exception {
        OkumaSonucu sonuc = UrunCsvOkuyucu.oku(yaz("kod;ad;fiyat;stok\n"));

        assertEquals(List.of(), sonuc.urunler());
        assertEquals(List.of(), sonuc.hatalar());
    }

    @Test
    @DisplayName("Dosya yoksa checked exception, asıl neden içinde saklı")
    void olmayanDosyaHataVerir() {
        Path yok = klasor.resolve("yok.csv");

        DosyaOkunamadiException hata = assertThrows(DosyaOkunamadiException.class, () -> UrunCsvOkuyucu.oku(yok));

        assertEquals(yok, hata.getDosya());
        assertInstanceOf(NoSuchFileException.class, hata.getCause());
    }

    @Test
    void yanlisBaslikDosyayiReddeder() throws Exception {
        Path dosya = yaz("isim,fiyat\nKalem,10\n");

        assertThrows(DosyaOkunamadiException.class, () -> UrunCsvOkuyucu.oku(dosya));
    }

    @Test
    void bosDosyaReddedilir() throws Exception {
        Path dosya = yaz("");

        assertThrows(DosyaOkunamadiException.class, () -> UrunCsvOkuyucu.oku(dosya));
    }

    @Test
    void turkceKarakterlerBozulmaz() throws Exception {
        Path dosya = yaz("kod;ad;fiyat;stok\nU009;Çağrı Işıklı Şemsiye;150;3\n");

        assertEquals("Çağrı Işıklı Şemsiye", UrunCsvOkuyucu.oku(dosya).urunler().getFirst().ad());
    }

    @ParameterizedTest
    // Varsayılan tırnak ' olduğu için mesajlardaki 'kesme işaretleri' bozulmasın diye " seçildi
    @CsvSource(delimiter = '|', quoteCharacter = '"', textBlock = """
            U1;Kalem;0;5         | Fiyat sıfırdan büyük olmalı: 0
            U1;Kalem;10;-1       | Stok negatif olamaz: -1
            U1;Kalem;10;2,5      | Stok tam sayı olmalı: '2,5'
            ;Kalem;10;1          | Ürün kodu boş olamaz
            U1;Kalem;10;1;fazla  | 4 sütun bekleniyordu, 5 bulundu
            """)
    void gecersizSatirNedeniyleReddedilir(String satir, String beklenenNeden) {
        IllegalArgumentException hata = assertThrows(IllegalArgumentException.class,
                () -> UrunCsvOkuyucu.satiriCoz(satir));

        assertEquals(beklenenNeden, hata.getMessage());
    }

    private Path yaz(String icerik) throws IOException {
        return Files.writeString(klasor.resolve("urunler.csv"), icerik, StandardCharsets.UTF_8);
    }
}
