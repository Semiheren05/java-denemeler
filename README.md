# java-denemeler

[![Testler](https://github.com/Semiheren05/java-denemeler/actions/workflows/ci.yml/badge.svg)](https://github.com/Semiheren05/java-denemeler/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-21-orange)
![JUnit](https://img.shields.io/badge/JUnit-5-green)
[![Lisans: MIT](https://img.shields.io/badge/Lisans-MIT-blue.svg)](LICENSE)

Her gün bir Java alıştırması. Her günün kodu kendi paketinde, birim testleriyle birlikte duruyor. GitHub Actions her push'ta tüm testleri çalıştırıyor.

**Teknolojiler:** Java 21 · Maven · JUnit 5 · GitHub Actions

## İlerleme

| Gün | Konu | Öne çıkanlar | Test | Kod |
|---|---|---|---|---|
| 01 | Banka hesabı | `BigDecimal`, özel exception, `record`, `enum`, değiştirilemez liste | 14 | [`gun01`](src/main/java/com/semih/gunluk/gun01) |
| 02 | Kelime sayacı | `Map.merge`, regex, Türkçe `Locale`, Streams ile sıralama | 15 | [`gun02`](src/main/java/com/semih/gunluk/gun02) |

---

## Gün 01: BankaHesabi

Para yatırma, çekme ve hesaplar arası transfer yapan basit bir banka hesabı.

```java
BankaHesabi semih = new BankaHesabi("TR001", "Semih");
BankaHesabi ayse  = new BankaHesabi("TR002", "Ayşe");

semih.paraYatir(new BigDecimal("200"));
semih.transferEt(ayse, new BigDecimal("75"));   // semih: 125.00, ayşe: 75.00
semih.paraCek(new BigDecimal("500"));           // YetersizBakiyeException
```

**Neler öğrendim:**
- **Para için neden `BigDecimal`?** `double` ikilik tabanda çalıştığı için `0.1 + 0.2` tam `0.3` etmez (`0.30000000000000004`). Para hesabında bu kabul edilemez, bu yüzden `BigDecimal` kullanıp tutarları 2 haneye (kuruş) yuvarladım.
- **Özel exception:** Bakiye yetmediğinde `YetersizBakiyeException` fırlatılıyor. Hata, mevcut bakiyeyi ve istenen tutarı da taşıyor. Hata durumunda bakiye değişmiyor.
- **Tutarlı transfer:** Önce para çekiliyor, sonra yatırılıyor. Bakiye yetmezse iki hesap da değişmeden kalıyor.
- **Kapsülleme:** İşlem geçmişi (`record Islem`) dışarıya `List.copyOf` ile değiştirilemez bir kopya olarak veriliyor.
- **Girdi doğrulama:** Boş, sıfır, negatif ya da kuruştan küçük tutarlar `IllegalArgumentException` ile reddediliyor.

| Dosya | Görevi |
|---|---|
| `BankaHesabi` | Hesap işlemleri ve doğrulama |
| `YetersizBakiyeException` | Bakiye yetmediğinde fırlatılan hata |
| `Islem` | Geçmişteki tek kayıt (`record`) |
| `IslemTuru` | `YATIRMA` / `CEKME` (`enum`) |

---

## Gün 02: KelimeSayaci

Bir metindeki kelimeleri sayan ve en sık geçenleri bulan sınıf.

```java
KelimeSayaci sayac = new KelimeSayaci();

sayac.say("IŞIK ışık, Semih'in kodu.");
// {ışık=2, semih=1, kodu=1}

sayac.enSikKelimeler("elma armut elma kiraz elma armut", 2);
// [KelimeSayisi[kelime=elma, sayi=3], KelimeSayisi[kelime=armut, sayi=2]]
```

**Neler öğrendim:**
- **`Map.merge`:** `sayilar.merge(kelime, 1, Integer::sum)` tek satırda "kelime yoksa 1 ekle, varsa bir artır" demek.
- **Türkçe büyük/küçük harf:** Sıradan `toLowerCase()` "I" harfini "i" yapar, Türkçede doğrusu "ı". `Locale.forLanguageTag("tr-TR")` ile "IŞIK" → "ışık", "İstanbul" → "istanbul" doğru oluyor.
- **Regex ile temizlik:** Harf (`\p{L}`) ve rakam (`\p{N}`) dışındaki her şey ayırıcı sayılıyor, böylece noktalama ve fazla boşluklar sorun çıkarmıyor. Kesme işaretli ekler atılıyor: "Semih'in" → "semih".
- **Streams ile sıralama:** En sık N kelime, `Comparator.comparingInt(...).reversed().thenComparing(...)` ile çoktan aza sıralanıyor. Eşit sayıda geçenler alfabetik diziliyor, sonuç her zaman aynı.

| Dosya | Görevi |
|---|---|
| `KelimeSayaci` | Sayma, en sık N kelime, toplam kelime sayısı |
| `KelimeSayisi` | Kelime ve kaç kez geçtiği (`record`) |

---

## Proje yapısı

```
src/
├── main/java/com/semih/gunluk/
│   ├── gun01/   BankaHesabi, YetersizBakiyeException, Islem, IslemTuru
│   └── gun02/   KelimeSayaci, KelimeSayisi
└── test/java/com/semih/gunluk/
    ├── gun01/   BankaHesabiTest
    └── gun02/   KelimeSayaciTest
```

Her yeni gün `gunNN` adında yeni bir paket olarak ekleniyor.

## Çalıştırma

Java 21 ve Maven gerekir.

```bash
git clone https://github.com/Semiheren05/java-denemeler.git
cd java-denemeler

mvn test                          # bütün testler
mvn test -Dtest=BankaHesabiTest   # sadece bir günün testleri
```

## Lisans

[MIT](LICENSE) © 2026 Semih Eren
