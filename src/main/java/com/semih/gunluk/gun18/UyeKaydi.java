package com.semih.gunluk.gun18;

import com.semih.gunluk.gun17.Iban;
import com.semih.gunluk.gun17.TcKimlikNo;
import com.semih.gunluk.gun17.Telefon;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Üye kaydı. Gün 17'deki doğrulayıcıları kullanıyor ve hiç istisna fırlatmıyor; sonucu
 * {@link Sonuc} olarak dönüyor.
 *
 * <p>İki aşama var:
 * <ol>
 *   <li><b>Alanlar</b> birbirinden bağımsız, hepsi birden kontrol ediliyor: kullanıcı beş alanı
 *       yanlış girdiyse beş hatayı birden görüyor, düzeltip tekrar gönderdikçe birer birer değil.</li>
 *   <li><b>İş kuralları</b> (yaş, aynı T.C. ile ikinci kayıt) alanlar geçerliyse sırayla
 *       kontrol ediliyor ({@code flatMap}); ilk hatada duruluyor.</li>
 * </ol>
 */
public class UyeKaydi {

    private static final int RESIT_YAS = 18;

    private final Year buYil;
    private final Set<String> kayitliTcler = new HashSet<>();
    private final List<Uye> uyeler = new ArrayList<>();

    public UyeKaydi(Year buYil) {
        this.buYil = buYil;
    }

    public Sonuc<Uye> kaydet(KayitFormu form) {
        return dogrula(form)
                .flatMap(this::resitMi)
                .flatMap(this::dahaOnceKayitliMi)
                .map(this::sakla);
    }

    public List<Uye> uyeler() {
        return List.copyOf(uyeler);
    }

    private Sonuc<Uye> dogrula(KayitFormu form) {
        Sonuc<String> ad = ad(form.ad());
        Sonuc<String> tc = TcKimlikNo.gecerliMi(form.tcKimlikNo())
                ? Sonuc.basarili(form.tcKimlikNo())
                : Sonuc.hatali("T.C. kimlik numarası geçersiz");
        Sonuc<String> iban = Iban.gecerliMi(form.iban())
                ? Sonuc.basarili(Iban.bicimle(form.iban()))
                : Sonuc.hatali("IBAN geçersiz");
        Sonuc<String> telefon = Sonuc.optionaldan(Telefon.normalle(form.telefon()), "Cep telefonu numarası geçersiz");
        Sonuc<Integer> yil = Sonuc.dene(() -> Integer.parseInt(form.dogumYili().trim()), "Doğum yılı sayı olmalı");

        List<String> hatalar = Sonuc.tumHatalar(ad, tc, iban, telefon, yil);
        if (!hatalar.isEmpty()) {
            return new Sonuc.Hatali<>(hatalar);
        }
        return Sonuc.basarili(new Uye(ad.deger(), tc.deger(), iban.deger(), telefon.deger(), yil.deger()));
    }

    private static Sonuc<String> ad(String ad) {
        if (ad == null || ad.isBlank()) {
            return Sonuc.hatali("Ad boş olamaz");
        }
        String temiz = ad.trim().replaceAll("\\s+", " ");
        return temiz.length() > 50 ? Sonuc.hatali("Ad en fazla 50 karakter olabilir") : Sonuc.basarili(temiz);
    }

    private Sonuc<Uye> resitMi(Uye uye) {
        int yas = buYil.getValue() - uye.dogumYili();
        if (yas < 0 || yas > 120) {
            return Sonuc.hatali("Doğum yılı gerçekçi değil: " + uye.dogumYili());
        }
        // Sadece yıl biliniyor: bu yıl 18'ine giren de reşit sayılıyor
        return yas >= RESIT_YAS ? Sonuc.basarili(uye) : Sonuc.hatali("Üye olmak için 18 yaşında olmak gerekir");
    }

    private Sonuc<Uye> dahaOnceKayitliMi(Uye uye) {
        return kayitliTcler.contains(uye.tcKimlikNo())
                ? Sonuc.hatali("Bu T.C. kimlik numarasıyla zaten bir üyelik var")
                : Sonuc.basarili(uye);
    }

    private Uye sakla(Uye uye) {
        kayitliTcler.add(uye.tcKimlikNo());
        uyeler.add(uye);
        return uye;
    }
}
