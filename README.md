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
| 10 | Fatura | Builder deseni, değiştirilemez sınıf, savunmacı kopya, `toBuilder`, `EnumMap`, `BigDecimal` yuvarlama | 12 | [`gun10`](src/main/java/com/semih/gunluk/gun10) |
| 11 | Metin editörü | `Deque` ile iki yığın, geri al / ileri al, `ArrayDeque` vs `Stack`, sınırlı geçmiş, `sealed` + kendi tersini bilen işlemler | 12 | [`gun11`](src/main/java/com/semih/gunluk/gun11) |
| 12 | Satış analizi | ikili arama, iki işaretçi, sabit ve değişken kayan pencere, Big-O, kaba kuvvetle karşılaştırmalı rastgele testler | 11 | [`gun12`](src/main/java/com/semih/gunluk/gun12) |
| 13 | Sayaç ve fiyat karşılaştırıcı | yarış durumu, `AtomicLong`, `ConcurrentHashMap.computeIfAbsent`, `ExecutorService` (try-with-resources), `CompletableFuture`, zaman aşımı, `CountDownLatch`, `CyclicBarrier` | 11 | [`gun13`](src/main/java/com/semih/gunluk/gun13) |
| 14 | Toplu SMS | virtual thread (Java 21), `newVirtualThreadPerTaskExecutor`, platform thread havuzuyla karşılaştırma, `Semaphore` ile eş zamanlı sınır | 9 | [`gun14`](src/main/java/com/semih/gunluk/gun14) |

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

## Gün 10: Fatura (Builder ve değiştirilemez nesneler)

Bir satış faturası: kalemler, KDV oranları ve indirim. Fatura bir kez oluşturulduktan sonra hiçbir alanı değişmiyor; değişiklik gerekiyorsa yeni bir fatura üretiliyor.

```java
Fatura fatura = Fatura.builder()
        .no("SE-2026-0042")
        .musteri("Ayşe Yılmaz")
        .tarih(LocalDate.of(2026, 10, 26))
        .kalem("Klavye", "750.00", 2, KdvOrani.YUZDE_20)
        .kalem("Java kitabı", "450.00", 1, KdvOrani.YUZDE_10)
        .indirim(10)
        .build();

fatura.araToplam();     // 1950.00
fatura.indirimTutari(); // 195.00
fatura.kdvDokumu();     // {YUZDE_10=40.50, YUZDE_20=270.00}
fatura.genelToplam();   // 2065.50

Fatura yeni = fatura.toBuilder().indirim(20).build();
yeni.genelToplam();     // 1836.00
fatura.genelToplam();   // 2065.50, asıl fatura değişmedi
fatura.kalemler().add(...);   // UnsupportedOperationException
```

**Neler öğrendim:**
- **Neden Builder?** Beş-altı parametreli bir constructor'da `new Fatura("42", "Ayşe", tarih, kalemler, 10)` gibi bir çağrıda hangi değerin ne olduğu okunmuyor, sırası da kolayca karışıyor. `.musteri(...).indirim(10)` her değeri adıyla veriyor; isteğe bağlı alanlar (indirim, tarih) hiç yazılmayabiliyor.
- **Kurallar tek yerde:** Zorunlu alanlar `build()` içinde kontrol ediliyor. Numarası ya da kalemi olmayan yarım bir fatura hiç oluşamıyor.
- **Değiştirilemez sınıf:** Sınıf `final`, alanlar `final`, setter yok, constructor `private`. Böyle bir nesne thread'ler arasında güvenle paylaşılabiliyor ve "bunu kim değiştirdi?" sorusu hiç çıkmıyor.
- **Savunmacı kopya:** Kalemler `List.copyOf` ile kopyalanıp saklanıyor. Builder'a `build()`'den sonra eklenen kalem faturayı etkilemiyor; dışarıya verilen liste de salt okunur.
- **`toBuilder`:** Değiştirilemez nesnede "indirimi değiştir" demek, mevcut faturadan başlayıp yenisini üretmek demek.
- **Kuruş hesabı:** Önce her KDV oranının matrahına indirim uygulanıyor, sonra KDV hesaplanıyor; her adım `RoundingMode.HALF_UP` ile iki haneye yuvarlanıyor. Böylece dökümdeki satırlar toplamla kuruşu kuruşuna eşit çıkıyor.

| Dosya | Görevi |
|---|---|
| `Fatura` | Değiştirilemez fatura, hesaplar ve iç `Builder` sınıfı |
| `FaturaKalemi` | Ürün, birim fiyat, adet, KDV oranı (`record`, doğrulamalı) |
| `KdvOrani` | %1, %10, %20 (`enum`) |

---

## Gün 11: MetinEditoru (`Deque` ile geri al / ileri al)

Ctrl+Z ve Ctrl+Y destekleyen küçük bir metin editörü. Her düzenleme kendi tersini bildiği için geri almak, tersini uygulamaktan ibaret.

```java
MetinEditoru editor = new MetinEditoru(100);   // en fazla 100 adım geçmiş
editor.yaz("Java çok güzel");
editor.sil(4, 4);     // "Java güzel"
editor.yaz("!");      // "Java güzel!"

editor.geriAl();      // "Java güzel"
editor.geriAl();      // "Java çok güzel"
editor.ileriAl();     // "Java güzel"
```

**Neler öğrendim:**
- **İki yığın:** Yapılan her düzenleme "geri alınacaklar" yığınına giriyor. Geri alınan, oradan çıkıp "ileri alınacaklar" yığınına geçiyor. Son giren ilk çıktığı için (LIFO) en son düzenleme ilk geri alınıyor.
- **`ArrayDeque`, `Stack` değil:** `Stack`, her metodu `synchronized` olan eski `Vector`'den türüyor; tek thread'li bir editörde boşuna yavaş. `Deque` arayüzü hem yığın (`push`/`pop`) hem kuyruk olarak kullanılabiliyor.
- **Sınırlı geçmiş:** Geçmiş 100 adımı geçince en eski adım yığının dibinden atılıyor (`removeLast`). `ArrayDeque` iki uçta da O(1) çalıştığı için bu ucuz; `ArrayList`'in başından silmek O(n) olurdu.
- **Yeni düzenleme ileriyi siler:** Geri alıp sonra yeni bir şey yazınca ileri alınacaklar temizleniyor. Aksi hâlde "geri alınmış gelecek" ile yeni metin birbirine karışırdı.
- **Silinen yazıyı sakla:** `Silme` sadece "4. karakterden 4 karakter" değil silinen metnin kendisini de tutuyor; geri alırken aynısını yerine koymak için başka yolu yok.
- **Kendi tersini bilen işlemler:** `Duzenleme` bir `sealed interface`; `Ekleme`'nin tersi `Silme`, `Silme`'nin tersi `Ekleme`. Editör düzenlemenin türünü bilmek zorunda kalmıyor.

| İşlem | `ArrayDeque` | `ArrayList` | `LinkedList` |
|---|---|---|---|
| Başa ekle / baştan çıkar | O(1) | O(n) | O(1) |
| Sona ekle / sondan çıkar | O(1) | O(1) | O(1) |
| İndeksle erişim | yok | O(1) | O(n) |

| Dosya | Görevi |
|---|---|
| `MetinEditoru` | Metin, iki yığın, geçmiş sınırı, konum kontrolleri |
| `Duzenleme` | `Ekleme` ve `Silme` (`sealed interface` + `record`) |

---

## Gün 12: SatisAnalizi (Algoritmalar ve Big-O)

Bir e-ticaret sitesinin fiyat ve satış verileri üzerinde, mülakatlarda en sık çıkan üç teknik. Fiyatlar kuruş cinsinden.

```java
int[] fiyatlar = {1999, 4500, 4500, 12990, 34900};            // sıralı
SatisAnalizi.butceyeUyanEnPahali(fiyatlar, 5000);              // 2 (ikinci 4500)

SatisAnalizi.hedefToplamliCift(new int[] {1500, 2500, 4000, 6000, 8500}, 10000);
// Optional[FiyatCifti[birinciIndeks=0, ikinciIndeks=4, birinciFiyat=1500, ikinciFiyat=8500]]

int[] satislar = {120, 340, 90, 410, 560, 30, 700};
SatisAnalizi.enIyiDonemSatisi(satislar, 3);    // 1290 (son üç gün)
SatisAnalizi.hedefeEnKisaSure(satislar, 1000); // 3 (410 + 560 + 30)
```

| Soru | Kaba kuvvet | Buradaki çözüm | Teknik |
|---|---|---|---|
| Bütçeye uyan en pahalı ürün | O(n) | O(log n) | İkili arama |
| Toplamı hedefi tutan iki ürün | O(n²) | O(n) | İki işaretçi |
| En iyi k günlük satış | O(n·k) | O(n) | Sabit kayan pencere |
| Hedefe ulaşan en kısa dönem | O(n²) | O(n) | Değişken kayan pencere |

**Neler öğrendim:**
- **İkili arama** her adımda aralığın yarısını eliyor; 1 milyon fiyatta en fazla 20 adım. Ortayı `sol + (sag - sol) / 2` ile buluyorum, `(sol + sag) / 2` çok büyük dizilerde `int` taşmasına yol açabilir. "Uyan bulundu ama daha iyisi sağda olabilir" diye aramaya devam etmek, aynı fiyattan birden fazla olduğunda en sağdakini veriyor.
- **İki işaretçi** sıralı olmaya dayanıyor: toplam küçükse onu büyütmenin tek yolu soldakini sağa kaydırmak, büyükse sağdakini sola. Her adımda bir işaretçi ilerlediği için toplam en fazla n adım.
- **Kayan pencere:** Bir sonraki pencerenin toplamını baştan toplamak yerine giren günü ekleyip çıkan günü çıkarıyorum; her pencere O(1).
- **Değişken pencere** sadece negatif olmayan sayılarda çalışıyor: pencereyi daraltmak toplamı hiç büyütmemeli. Bu yüzden negatif satış reddediliyor.
- **Kaba kuvvetle karşılaştırma:** Hızlı çözümleri, yavaş ama açıkça doğru O(n²) çözümlerle yüzlerce rastgele dizi üzerinde karşılaştıran testler var. Sabit tohumlu (`new Random(42)`) olduğu için her çalıştırmada aynı diziler üretiliyor, hata çıkarsa tekrar edilebiliyor.

| Dosya | Görevi |
|---|---|
| `SatisAnalizi` | Dört algoritma (`static` metotlar) |
| `FiyatCifti` | Bulunan iki ürün ve indeksleri (`record`) |

---

## Gün 13: SayfaSayaci ve FiyatKarsilastirici (Eşzamanlılık)

İki küçük eşzamanlılık problemi: birçok thread'den aynı anda gelen ziyaretleri kaybetmeden saymak, ve bir ürünün fiyatını birkaç tedarikçiye aynı anda sorup en ucuzunu seçmek.

```java
SayfaSayaci sayac = new SayfaSayaci();
try (ExecutorService havuz = Executors.newFixedThreadPool(8)) {
    for (int t = 0; t < 8; t++) {
        havuz.submit(() -> {
            for (int i = 0; i < 10_000; i++) sayac.kaydet("/blog");
        });
    }
}   // close() bütün işlerin bitmesini bekliyor
sayac.sayi("/blog");   // 80000, her seferinde

try (ExecutorService havuz = Executors.newFixedThreadPool(4)) {
    FiyatKarsilastirici karsilastirici = new FiyatKarsilastirici(havuz, Duration.ofMillis(500));
    karsilastirici.enUcuz("Klavye", List.of(
            new Tedarikci("Ali Elektronik", urun -> new BigDecimal("799.90")),
            new Tedarikci("Deniz Bilişim", urun -> new BigDecimal("749.90")),
            new Tedarikci("Bozuk A.Ş.", urun -> { throw new IllegalStateException("Servis kapalı"); })));
    // Optional[Teklif[tedarikci=Deniz Bilişim, fiyat=749.90]]
}
```

**Neler öğrendim:**
- **Yarış durumu:** Aynı örneği düz bir `HashMap` ve `put(s, get(s) + 1)` ile denediğimde 80.000 yerine bir seferinde 22.746, bir sonrakinde 54.022 çıktı. İki thread aynı değeri okuyup ikisi de "+1" yazınca bir ziyaret kayboluyor; sonuç her seferinde farklı olduğu için bu hatayı yakalamak zor.
- **Atomik işlem:** `AtomicLong.incrementAndGet` "oku, bir ekle, yaz" adımlarını bölünmez tek bir işlem yapıyor. `ConcurrentHashMap.computeIfAbsent` da bir sayfanın sayacının tam bir kez oluşturulmasını garanti ediyor. Hiçbir yerde `synchronized` yok.
- **`ExecutorService` try-with-resources ile:** Java 19'dan beri `ExecutorService` `AutoCloseable`; blok bitince `close()` işlerin bitmesini bekliyor. Thread'leri elle `new Thread(...)` ile açmak yerine sabit boyutlu bir havuz kullanmak, thread sayısını kontrol altında tutuyor.
- **`CompletableFuture` ile paralel sorgu:** Her tedarikçi ayrı bir thread'de soruluyor; toplam süre cevap sürelerinin toplamı değil en yavaşı kadar. `completeOnTimeout` geç kalanı, `exceptionally` hata vereni "teklif yok" sayıyor; biri bozuk diye sonuç bozulmuyor.
- **Eşzamanlılığı test etmek:** "Paralel çalıştı mı?" sorusunu süre ölçerek test etmek kırılgan. Onun yerine `CyclicBarrier(3)` kullandım: üç tedarikçi de diğer ikisi gelene kadar bekliyor. Sorular sırayla gitseydi ilk tedarikçi diğerlerini hiç göremez ve hata verirdi. Sayaç testinde de `CountDownLatch` bütün thread'leri aynı anda başlatıp çakışma ihtimalini en yükseğe çıkarıyor.

| Dosya | Görevi |
|---|---|
| `SayfaSayaci` | Thread güvenli sayfa sayacı (`ConcurrentHashMap` + `AtomicLong`) |
| `FiyatKarsilastirici` | Tedarikçilere paralel sorgu, zaman aşımı ve hata yönetimi |
| `Tedarikci` | Tedarikçi adı ve fiyat kaynağı (`record`) |
| `FiyatKaynagi` | Fiyat sorgusu (`@FunctionalInterface`) |
| `Teklif` | Tedarikçi ve fiyat (`record`, doğrulamalı) |

---

## Gün 14: TopluSmsGonderici (Virtual thread'ler, Java 21)

Binlerce SMS'i aynı anda gönderen bir servis. Her gönderim cevap gelene kadar bekleyen bir ağ çağrısı; Java 21'in virtual thread'leri tam bu tür işler için.

```java
// Her SMS 100 ms süren bir ağ çağrısı
TopluSmsGonderici gonderici = new TopluSmsGonderici(bildirim -> Thread.sleep(100), 10_000);

gonderici.sanalThreadlerle(onBinSms);
// GonderimRaporu[basarili=10000, basarisiz=0, sure=PT0.218S]

gonderici.platformHavuzuyla(onBinSms, 200);
// GonderimRaporu[basarili=10000, basarisiz=0, sure=PT5.087S]

new TopluSmsGonderici(bildirim -> Thread.sleep(100), 500).sanalThreadlerle(onBinSms);
// aynı anda en fazla 500 gönderim: sure=PT2.032S
```

**Neler öğrendim:**
- **Platform thread pahalı:** Her biri bir işletim sistemi thread'i ve kendi yığın belleği var; on binlercesi açılamıyor. Bu yüzden sabit bir havuz kullanılıyor ve işler sıra bekliyor: 200 thread'lik havuzla 10.000 SMS bende 5,1 saniye sürdü (10.000 / 200 × 100 ms).
- **Virtual thread ucuz:** JVM'in yönettiği hafif bir thread. `Thread.sleep` ya da bir ağ çağrısında beklerken altındaki işletim sistemi thread'ini bırakıyor, o thread başka bir işi yürütüyor. Her işe ayrı bir virtual thread açınca aynı 10.000 SMS 0,2 saniyede bitti.
- **Kod neredeyse aynı:** Sadece `Executors.newFixedThreadPool(200)` yerine `Executors.newVirtualThreadPerTaskExecutor()`. `CompletableFuture` zinciri ya da reaktif kütüphane gerekmeden, düz bloklayan kodla yüksek eşzamanlılık.
- **Thread ucuz, karşıdaki servis değil:** SMS sağlayıcısı aynı anda 10.000 isteği kaldırmayabilir. `Semaphore` aynı anda en fazla N gönderime izin veriyor; testte sayaçla ölçüp sınırın hiç aşılmadığını doğruluyorum.
- **İzin `finally`'de geri verilir:** Gönderim hata verse bile `release()` çağrılıyor. Yoksa her hatada sınır bir azalır ve sonunda herkes sonsuza kadar bekler; sınırı 1 olan ve hep hata veren bir servisle bunun testi var.
- **Virtual thread ne zaman işe yaramaz:** Bekleme değil hesaplama yapan (CPU'yu meşgul eden) işlerde; orada çekirdek sayısından fazla thread bir şey kazandırmıyor.

| Dosya | Görevi |
|---|---|
| `TopluSmsGonderici` | Virtual thread ve platform havuzuyla gönderim, `Semaphore` sınırı |
| `Bildirim` | Telefon ve mesaj (`record`, doğrulamalı) |
| `SmsServisi` | Dış servis (`@FunctionalInterface`, bloklayan çağrı) |
| `GonderimRaporu` | Başarılı, başarısız, süre (`record`) |

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
│   ├── gun09/   RandevuDefteri, Randevu, BosAralik, CakismaException
│   ├── gun10/   Fatura, FaturaKalemi, KdvOrani
│   ├── gun11/   MetinEditoru, Duzenleme
│   ├── gun12/   SatisAnalizi, FiyatCifti
│   ├── gun13/   SayfaSayaci, FiyatKarsilastirici, Tedarikci, FiyatKaynagi, Teklif
│   └── gun14/   TopluSmsGonderici, Bildirim, SmsServisi, GonderimRaporu
└── test/java/com/semih/gunluk/
    ├── gun01/   BankaHesabiTest
    ├── gun02/   KelimeSayaciTest
    ├── gun03/   NotAnaliziTest
    ├── gun04/   SekilHesaplayiciTest
    ├── gun05/   SiraliListeTest
    ├── gun06/   KargoHesaplayiciTest
    ├── gun07/   KatalogTest
    ├── gun08/   UrunCsvOkuyucuTest
    ├── gun09/   RandevuDefteriTest
    ├── gun10/   FaturaTest
    ├── gun11/   MetinEditoruTest
    ├── gun12/   SatisAnaliziTest
    ├── gun13/   SayfaSayaciTest, FiyatKarsilastiriciTest
    └── gun14/   TopluSmsGondericiTest
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
