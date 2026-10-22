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
| 03 | Not analizi | Streams, `groupingBy`, `partitioningBy`, `Optional`, `EnumMap` | 16 | [`gun03`](src/main/java/com/semih/gunluk/gun03) |
| 04 | Şekiller | `sealed interface`, `record`, `switch` pattern matching, record pattern, `when` | 12 | [`gun04`](src/main/java/com/semih/gunluk/gun04) |
| 05 | Tipli koleksiyonlar | generics, sınırlı tip parametresi, wildcard, PECS, generic `record` | 12 | [`gun05`](src/main/java/com/semih/gunluk/gun05) |
| 06 | Kargo ücreti | strateji deseni, `@FunctionalInterface`, lambda, sarmalayan strateji, açık/kapalı prensibi | 13 | [`gun06`](src/main/java/com/semih/gunluk/gun06) |
| 07 | Kitap kataloğu | `equals`/`hashCode` sözleşmesi, `Comparable`, `Comparator`, `HashSet`/`TreeSet`, Türkçe `Collator` | 12 | [`gun07`](src/main/java/com/semih/gunluk/gun07) |
| 08 | CSV ürün okuyucu | checked/unchecked exception, özel exception ve `cause`, try-with-resources, `java.nio.file`, `@TempDir` | 9 | [`gun08`](src/main/java/com/semih/gunluk/gun08) |
| 09 | Randevu defteri | `java.time` (`LocalDateTime`, `Duration`, `DayOfWeek`), `Clock` ile test edilebilir zaman, aralık çakışması | 14 | [`gun09`](src/main/java/com/semih/gunluk/gun09) |

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

## Gün 03: NotAnalizi

Bir sınıfın notlarından ortalama, en yüksek/en düşük, harf notu dağılımı ve bölüm ortalamaları çıkaran sınıf. Döngü yok, hepsi Streams API ile.

```java
NotAnalizi sinif = new NotAnalizi(List.of(
        new Ogrenci("Ali", "Bilgisayar", 95),
        new Ogrenci("Ayşe", "Bilgisayar", 88),
        new Ogrenci("Deniz", "Elektrik", 58)));

sinif.ortalama();              // OptionalDouble[80.33...]
sinif.enYuksek();              // Optional[Ogrenci[ad=Ali, ...]]
sinif.harfNotunaGoreGrupla();  // {AA=[Ali], BA=[Ayşe], FD=[Deniz]}
sinif.gecenVeKalanlar();       // {false=[Deniz], true=[Ali, Ayşe]}
```

**Neler öğrendim:**
- **Collectors:** `groupingBy` ile harf notuna ve bölüme göre gruplama, `mapping` ile gruptan sadece adları alma, `counting` ve `averagingInt` ile sayma ve ortalama, `partitioningBy` ile geçen/kalan diye ikiye ayırma.
- **`Optional` ve `OptionalDouble`:** Boş sınıfın ortalaması ya da en yüksek notu yoktur. `null` veya 0 dönmek yerine "değer olmayabilir" bilgisini tipte taşıyorum.
- **`EnumMap` ve `TreeMap`:** `groupingBy`'a hangi map'i kullanacağını vererek sonucu sıralı aldım: harf notları AA'dan FF'ye, bölümler alfabetik.
- **Tekrar kullanılan `Comparator`:** Başarı sırası (puan azalan, eşitse ad) tek bir sabitte. `enYuksek`, `enDusuk` ve `ilkN` hep onu kullanıyor.
- **`IntSummaryStatistics`:** En düşük, en yüksek, toplam, ortalama ve sayıyı listeyi bir kez gezerek veriyor.
- **`record` doğrulaması:** `Ogrenci`'nin compact constructor'ı 0–100 dışındaki puanları ve boş adları daha nesne oluşmadan reddediyor.

| Dosya | Görevi |
|---|---|
| `NotAnalizi` | Streams ile tüm istatistikler |
| `Ogrenci` | Ad, bölüm, puan (`record`, doğrulamalı) |
| `HarfNotu` | AA–FF harf notları ve alt sınırları (`enum`), DD ve üstü geçer |

---

## Gün 04: Şekiller (Java 21)

Daire, dikdörtgen, kare ve üçgenin alan ve çevresini Java 21'in yeni özellikleriyle hesaplayan küçük bir model.

```java
Sekil sekil = new Ucgen(3, 4, 5);

SekilHesaplayici.alan(sekil);    // 6.0 (Heron formülü)
SekilHesaplayici.cevre(sekil);   // 12.0
SekilHesaplayici.tarif(sekil);   // "çeşitkenar üçgen"
SekilHesaplayici.tanimla(null);  // "boş (null)"

new Ucgen(1, 2, 10);             // IllegalArgumentException: bu kenarlarla üçgen olmaz
```

```java
// default yok: Sekil sealed olduğu için derleyici tüm şekilleri biliyor
return switch (sekil) {
    case Daire(double r)                   -> 2 * Math.PI * r;
    case Dikdortgen(double en, double boy) -> 2 * (en + boy);
    case Kare(double kenar)                -> 4 * kenar;
    case Ucgen(double a, double b, double c) -> a + b + c;
};
```

**Neler öğrendim:**
- **`sealed interface`:** `Sekil`'i sadece `permits` listesindeki dört tip uygulayabiliyor. Bu yüzden `switch`'te `default` gerekmiyor. Yeni bir şekil ekleyip bir `switch`'e eklemeyi unutursam kod **derlenmiyor**, hata çalışma zamanına kalmıyor.
- **`switch` pattern matching:** `instanceof` ve cast zinciri yerine `case Daire d ->` ile tipe göre dallanma.
- **Record pattern:** `case Dikdortgen(double en, double boy)` ile record'un alanları doğrudan değişkenlere açılıyor.
- **`when` koşulları:** `case Ucgen u when u.eskenarMi()` gibi koşullu desenler. Java desenleri yukarıdan aşağı denediği için özel durumlar genelden önce yazılmalı.
- **`case null`:** Java 21'de `null` da `switch` içinde ele alınabiliyor, ayrıca kontrol gerekmiyor.
- **Doğrulamalı `record`:** Compact constructor'lar sıfır, negatif, `NaN` ve sonsuz ölçüleri, üçgen eşitsizliğini sağlamayan kenarları reddediyor.

| Dosya | Görevi |
|---|---|
| `Sekil` | `sealed interface`, ortak doğrulama |
| `Daire`, `Dikdortgen`, `Kare`, `Ucgen` | Şekiller (`record`) |
| `SekilHesaplayici` | Alan, çevre, tarif ve tanımlama (`switch` desenleri) |

---

## Gün 05: SiraliListe (Generics)

Her türlü karşılaştırılabilir tipi kabul eden, elemanlarını hep sıralı tutan bir liste ve birkaç generic yardımcı metot.

```java
SiraliListe<Integer> notlar = new SiraliListe<>();
notlar.hepsiniEkle(List.of(70, 95, 40));
notlar.ekle(85);
notlar.listele();                        // [40, 70, 85, 95]
notlar.enBuyuk();                        // Optional[95]

new Cift<>("Semih", 25).tersCevir();     // Cift[birinci=25, ikinci=Semih]

List<Number> sayilar = new ArrayList<>();
GenelAraclar.kopyala(List.of(1, 2), sayilar);   // Integer listesi Number listesine yazılabiliyor
GenelAraclar.ayir(List.of(1, 2, 3, 4), n -> n % 2 == 0);
// Cift[birinci=[2, 4], ikinci=[1, 3]]
```

**Neler öğrendim:**
- **Neden generics?** `List` yerine `List<Integer>` yazınca yanlış tipte eleman eklemek derleme hatası oluyor, okurken de cast gerekmiyor. Hata çalışma zamanından derleme zamanına taşınıyor.
- **Sınırlı tip parametresi:** `SiraliListe<T extends Comparable<? super T>>` "sadece kendi arasında sıralanabilen tipler" demek. `Object` gibi sıralanamayan bir tiple liste oluşturmaya çalışınca kod derlenmiyor.
- **PECS kuralı:** "Producer Extends, Consumer Super". Sadece okuduğum koleksiyon `? extends T`, sadece yazdığım koleksiyon `? super T`. Bu sayede `kopyala` bir `List<Integer>`'ı `List<Number>`'a aktarabiliyor.
- **Generic metot:** `<T, R> List<R> donustur(...)` gibi tip parametresi sınıfta değil metotta da tanımlanabiliyor; derleyici tipleri argümanlardan çıkarıyor.
- **İkili arama ile ekleme:** `Collections.binarySearch` bulamayınca `-(eklenecek yer) - 1` döndürüyor. Bunu kullanarak elemanı doğru yere koyuyorum, listeyi her seferinde baştan sıralamıyorum.
- **Generic `record`:** `Cift<A, B>` iki farklı tipi taşıyabiliyor; `tersCevir` dönüş tipini de `Cift<B, A>` olarak çeviriyor.

| Dosya | Görevi |
|---|---|
| `SiraliListe` | Her zaman sıralı, tipli liste (`Iterable`) |
| `Cift` | İki değerlik generic `record` |
| `GenelAraclar` | `enBuyuk`, `kopyala`, `donustur`, `ayir` generic metotları |

---

## Gün 06: KargoHesaplayici (Strateji deseni)

Farklı kargo firmalarının ücretini hesaplayan ve en ucuz teklifi bulan bir hesaplayıcı. Her firmanın hesaplama yolu bir "strateji".

```java
KargoHesaplayici hesaplayici = new KargoHesaplayici();
hesaplayici.firmaEkle("Standart", KargoStratejileri.standart());   // 30 TL + kg başı 5 TL
hesaplayici.firmaEkle("Hızlı", KargoStratejileri.hizli());         // 50 TL + kg başı 8 TL (+20 TL uzak mesafe)
hesaplayici.firmaEkle("Kampanya",
        KargoStratejileri.ucretsizEsigi(new BigDecimal("300"), KargoStratejileri.standart()));

Gonderi paket = new Gonderi(2, 100, new BigDecimal("350"));
hesaplayici.enUcuz(paket);   // Optional[Teklif[firma=Kampanya, ucret=0.00]]

// Yeni bir strateji için sınıf yazmak şart değil, lambda yeterli
hesaplayici.firmaEkle("Km başı", g -> BigDecimal.valueOf(g.mesafeKm()).multiply(new BigDecimal("0.10")));
```

**Neler öğrendim:**
- **Strateji deseni:** "Ücret nasıl hesaplanır?" sorusunu bir arayüzün arkasına koydum. `KargoHesaplayici`'nın içinde hiç `if (firma.equals(...))` yok; yeni firma eklemek mevcut kodu değiştirmiyor (açık/kapalı prensibi).
- **`@FunctionalInterface`:** Tek soyut metodu olan arayüz. Derleyici ikinci bir metot eklenirse hata veriyor ve arayüzü lambda ya da metot referansıyla doldurabiliyorum.
- **Stratejiyi döndüren fabrika metotları:** `KargoStratejileri.standart()` bir sınıf değil, bir lambda döndürüyor. Kod kısa kalıyor ama her strateji yine ayrı ayrı test edilebiliyor.
- **Sarmalayan strateji:** `ucretsizEsigi` başka bir stratejiyi alıp üstüne "sepet eşiği geçerse bedava" davranışı ekliyor. Asıl stratejiye dokunmadan yeni kural eklemenin yolu (decorator fikri).
- **İki aşamalı sıralama:** Teklifler `Comparator.comparing(Teklif::ucret).thenComparing(Teklif::firma)` ile sıralanıyor; eşit fiyatlı firmalar her seferinde aynı sırada geliyor.
- **`LinkedHashMap` ve `putIfAbsent`:** Firmalar eklenme sırasını koruyor, aynı ada ikinci kayıt tek satırda reddediliyor.

| Dosya | Görevi |
|---|---|
| `KargoUcretStratejisi` | Strateji arayüzü (`@FunctionalInterface`) |
| `KargoStratejileri` | Hazır stratejiler: standart, hızlı, sabit, ücretsiz kargo eşiği |
| `KargoHesaplayici` | Firmaları tutar, teklif ve en ucuzu bulur |
| `Gonderi` | Ağırlık, mesafe, sepet tutarı (`record`, doğrulamalı) |
| `Teklif` | Firma ve ücret (`record`) |

---

## Gün 07: Katalog (`equals`, `hashCode`, `Comparable`)

Aynı kitabı iki kez kabul etmeyen, kitapları başlığa, yıla ve yazara göre sıralayabilen bir katalog. Kitabın kimliği ISBN.

```java
Kitap a = new Kitap("978-975-0000-01-1", "Çalıkuşu", "Reşat Nuri Güntekin", 1922);
Kitap b = new Kitap("9789750000011", "Çalıkuşu (yeni baskı)", "R. N. Güntekin", 2020);

a.equals(b);                          // true: ISBN aynı
Katalog katalog = new Katalog();
katalog.ekle(a);                      // true
katalog.ekle(b);                      // false, bu kitap zaten var

katalog.sirala(Kitap.BASLIGA_GORE);   // Türkçe alfabeyle: Cevdet Bey..., Çalıkuşu, Dokuzuncu...
katalog.yazaraGore();                 // yazarlar alfabetik, her yazarın kitapları yıla göre
```

**Neler öğrendim:**
- **`equals` ve `hashCode` birlikte yazılır:** `HashSet` önce `hashCode` ile kovayı buluyor, sonra `equals` ile karşılaştırıyor. Testte sadece `equals` yazılmış bir sınıfla aynı kitabın sete **iki kez** girdiğini gösterdim.
- **Neden `record` değil?** `record` eşitliği tüm alanlardan hesaplıyor. Burada eşitlik sadece ISBN'e bağlı olduğu için sınıfı ve `equals`/`hashCode`'u kendim yazdım.
- **`Comparable` ile `equals` tutarlı olmalı:** Doğal sıralamayı da ISBN'e göre yaptım. Başlığa göre yapsaydım aynı başlıklı iki farklı kitabı `TreeSet` "aynı" sayıp birini atardı.
- **Birden fazla sıralama:** Doğal sıralama tek, ama `BASLIGA_GORE` ve `YILA_GORE` gibi istediğim kadar `Comparator` sabiti tanımlayabiliyorum.
- **Türkçe alfabe:** `String.compareTo` harfleri Unicode koduna göre sıralıyor, "Ç"yi "Z"den sonraya atıyor. `Collator` (`tr-TR`) ile "Ç" doğru yerine, "C" ile "D" arasına geliyor.
- **ISBN normalleştirme:** Tireli ve tiresiz yazılan ISBN'ler aynı kitabı gösteriyor; kimlik değeri constructor'da tek biçime çevriliyor.

| Dosya | Görevi |
|---|---|
| `Kitap` | ISBN kimlikli kitap: `equals`, `hashCode`, `compareTo`, sıralama sabitleri |
| `Katalog` | `HashSet` ile tekrarsız katalog, arama, sıralama, yazara göre gruplama |

---

## Gün 08: UrunCsvOkuyucu (Exception tasarımı ve dosya okuma)

Noktalı virgülle ayrılmış bir ürün dosyasını okuyan sınıf. Bozuk satırlar yüzünden bütün dosya çöpe gitmiyor: geçerli ürünler alınıyor, hatalı satırlar numarasıyla raporlanıyor.

```
kod;ad;fiyat;stok
U001;Kalem;12,50;100
U002;Defter;bedava;20
U003;Silgi;5
```

```java
try {
    OkumaSonucu sonuc = UrunCsvOkuyucu.oku(Path.of("urunler.csv"));
    sonuc.urunler();   // [Urun[kod=U001, ad=Kalem, fiyat=12.50, stok=100]]
    sonuc.hatalar();   // [HataliSatir[satirNo=3, ..., neden=Fiyat sayı değil: 'bedava'],
                       //  HataliSatir[satirNo=4, ..., neden=4 sütun bekleniyordu, 3 bulundu]]
} catch (DosyaOkunamadiException e) {
    // dosya yok, okunamıyor ya da başlık satırı yanlış
}
```

**Neler öğrendim:**
- **İki tür hata, iki farklı tepki:** Dosyanın kendisi okunamıyorsa devam etmenin anlamı yok, exception fırlatılıyor. Tek bir satır bozuksa o satır atlanıp raporlanıyor.
- **Checked mi, unchecked mı?** Dosya sistemi programın kontrolünde olmadığı için `DosyaOkunamadiException`'ı checked yaptım; çağıran kod onu yakalamak zorunda. Bozuk veri ise programcı hatası değil ama kurtarılabilir, onu `IllegalArgumentException` olarak içeride yakalayıp `HataliSatir`'a çeviriyorum.
- **Exception zinciri:** Asıl `IOException` yeni exception'a `cause` olarak veriliyor. Hata mesajı daha anlaşılır olurken orijinal bilgi kaybolmuyor (`hata.getCause()`).
- **try-with-resources:** `BufferedReader` `try (...)` içinde açıldığı için hata olsa da olmasa da kapatılıyor; `finally` bloğu yazmaya gerek yok.
- **`java.nio.file`:** `Files.newBufferedReader(path, UTF_8)` ile Türkçe karakterler bozulmadan okunuyor; dosya yoksa özel olarak `NoSuchFileException` geliyor.
- **Sonuç tipi:** `OkumaSonucu` hem ürünleri hem hataları taşıyan bir `record`. Hatalar exception olarak yukarı fırlamıyor, veri olarak dönüyor.
- **`@TempDir` ve `@CsvSource`:** Testlerde JUnit her test için geçici bir klasör açıp siliyor. Hatalı satır senaryolarını tek bir tabloda topladım.

| Dosya | Görevi |
|---|---|
| `UrunCsvOkuyucu` | Dosyayı okur, satırları çözer, hataları toplar |
| `Urun` | Kod, ad, fiyat, stok (`record`, doğrulamalı) |
| `OkumaSonucu` | Geçerli ürünler ve hatalı satırlar (`record`) |
| `HataliSatir` | Satır numarası, içerik ve neden (`record`) |
| `DosyaOkunamadiException` | Dosya düzeyindeki hatalar için checked exception |

---

## Gün 09: RandevuDefteri (`java.time`)

Bir kuaförün randevu defteri: mesai 09:00–18:00, pazar kapalı, çakışan randevu alınmıyor, gün içindeki boş saatler listelenebiliyor.

```java
RandevuDefteri defter = new RandevuDefteri(Clock.system(ZoneId.of("Europe/Istanbul")));
LocalDate cuma = LocalDate.of(2026, 10, 23);

defter.ekle(new Randevu("Ayşe", cuma.atTime(9, 0), Duration.ofMinutes(60)));
defter.ekle(new Randevu("Ali", cuma.atTime(12, 0), Duration.ofMinutes(30)));
defter.ekle(new Randevu("Semih", cuma.atTime(12, 15), Duration.ofMinutes(30)));
// CakismaException: Bu saatte başka bir randevu var: Ali, 12:00–12:30

defter.bosAraliklar(cuma, Duration.ofMinutes(30));
// [BosAralik[baslangic=10:00, bitis=12:00], BosAralik[baslangic=12:30, bitis=18:00]]
```

**Neler öğrendim:**
- **Doğru tipi seçmek:** Tarih için `LocalDate`, saat için `LocalTime`, ikisi birlikte `LocalDateTime`, süre için `Duration`. Eski `Date` sınıfı yerine hepsi değiştirilemez ve anlaşılır.
- **`Clock` ile test edilebilir zaman:** `LocalDateTime.now()` her çalıştırmada farklı sonuç verdiği için "geçmiş zamana randevu alınamaz" kuralını test etmek zor. Sınıf saati dışarıdan alıyor, testte `Clock.fixed(...)` ile zamanı donduruyorum.
- **Saat dilimi:** `Clock`'a `Europe/Istanbul` veriliyor. Sabit saat UTC `07:00` olarak yazılıp İstanbul'da `10:00` olarak okunuyor.
- **Aralık çakışması:** İki aralık, biri diğeri bitmeden başlıyorsa çakışır: `a.bas < b.bitis && b.bas < a.bitis`. Uç uca eklenen randevular (biri 10:00'da biterken öteki 10:00'da başlıyor) çakışmıyor.
- **Gece yarısı tuzağı:** 23:30'da başlayan 1 saatlik randevunun `toLocalTime()` ile bitişi `00:30` görünüyor ve mesai içinde sanılıyor. Önce bitişin aynı günde olduğunu kontrol ediyorum.
- **Hesaplanan alan:** `Randevu` bitiş saatini saklamıyor, `baslangic + sure` olarak hesaplıyor; iki değer hiçbir zaman birbiriyle çelişemiyor.

| Dosya | Görevi |
|---|---|
| `RandevuDefteri` | Kurallar, çakışma kontrolü, boş aralıklar, sıradaki randevu |
| `Randevu` | Müşteri, başlangıç, süre (`record`, doğrulamalı) |
| `BosAralik` | Gün içinde boş zaman dilimi (`record`) |
| `CakismaException` | Çakışan randevuyu taşıyan hata |

---

## Proje yapısı

```
src/
├── main/java/com/semih/gunluk/
│   ├── gun01/   BankaHesabi, YetersizBakiyeException, Islem, IslemTuru
│   ├── gun02/   KelimeSayaci, KelimeSayisi
│   ├── gun03/   NotAnalizi, Ogrenci, HarfNotu
│   ├── gun04/   Sekil, Daire, Dikdortgen, Kare, Ucgen, SekilHesaplayici
│   ├── gun05/   SiraliListe, Cift, GenelAraclar
│   ├── gun06/   Gonderi, KargoUcretStratejisi, KargoStratejileri, KargoHesaplayici, Teklif
│   ├── gun07/   Kitap, Katalog
│   ├── gun08/   UrunCsvOkuyucu, Urun, OkumaSonucu, HataliSatir, DosyaOkunamadiException
│   └── gun09/   RandevuDefteri, Randevu, BosAralik, CakismaException
└── test/java/com/semih/gunluk/
    ├── gun01/   BankaHesabiTest
    ├── gun02/   KelimeSayaciTest
    ├── gun03/   NotAnaliziTest
    ├── gun04/   SekilHesaplayiciTest
    ├── gun05/   SiraliListeTest
    ├── gun06/   KargoHesaplayiciTest
    ├── gun07/   KatalogTest
    ├── gun08/   UrunCsvOkuyucuTest
    └── gun09/   RandevuDefteriTest
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
