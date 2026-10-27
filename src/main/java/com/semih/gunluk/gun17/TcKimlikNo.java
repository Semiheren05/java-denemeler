package com.semih.gunluk.gun17;

/**
 * T.C. kimlik numarası doğrulaması. Biçim kontrolü (11 rakam) yetmiyor; son iki hane, ilk
 * dokuzdan hesaplanan kontrol haneleri. Yanlış yazılmış bir numaranın tesadüfen geçme ihtimali
 * çok düşük.
 *
 * <p>Kurallar:
 * <ol>
 *   <li>11 haneli, sadece rakam, ilk hane 0 değil.</li>
 *   <li>10. hane = ((1, 3, 5, 7, 9. hanelerin toplamı) × 7 − (2, 4, 6, 8. hanelerin toplamı)) mod 10</li>
 *   <li>11. hane = (ilk 10 hanenin toplamı) mod 10</li>
 * </ol>
 * Bu sadece numaranın <em>geçerli biçimde</em> olduğunu gösterir, birine ait olduğunu değil.
 */
public final class TcKimlikNo {

    private TcKimlikNo() {
    }

    public static boolean gecerliMi(String no) {
        if (no == null || no.length() != 11 || no.charAt(0) == '0') {
            return false;
        }
        int[] h = new int[11];
        for (int i = 0; i < 11; i++) {
            char c = no.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
            h[i] = c - '0';   // '7' - '0' = 7: karakterin sayı değeri
        }
        int tekler = h[0] + h[2] + h[4] + h[6] + h[8];
        int ciftler = h[1] + h[3] + h[5] + h[7];
        // Java'da % negatif sonuç verebilir (-3 % 10 = -3); Math.floorMod hep 0..9 verir
        if (Math.floorMod(tekler * 7 - ciftler, 10) != h[9]) {
            return false;
        }
        int ilkOnToplam = tekler + ciftler + h[9];
        return ilkOnToplam % 10 == h[10];
    }
}
