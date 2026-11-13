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
| 15 | Stok bildirimleri | observer deseni, tipli olay yayını, `sealed` olaylar, `CopyOnWriteArrayList`, `AutoCloseable` abonelik, dinleyici hatalarını yalıtmak | 10 | [`gun15`](src/main/java/com/semih/gunluk/gun15) |
| 16 | Hava durumu servisi | decorator (önbellek, tekrar deneme), adapter, factory, kalıtım yerine bileşim, `addSuppressed`, testte elle ilerletilen `Clock` | 10 | [`gun16`](src/main/java/com/semih/gunluk/gun16) |
| 17 | Doğrulayıcılar | T.C. kimlik no kontrol haneleri, IBAN mod 97 (`BigInteger`'sız), regex adlandırılmış grupları, `matches` vs `find`, `Math.floorMod`, `@NullAndEmptySource` | 10 | [`gun17`](src/main/java/com/semih/gunluk/gun17) |
| 18 | Üye kaydı | `Sonuc<T>` tipi (`sealed` + generic `record`), istisna yerine dönüş tipiyle hata, `map`/`flatMap`/`katla`, hataları biriktirme ve ilk hatada durma, generic record pattern | 11 | [`gun18`](src/main/java/com/semih/gunluk/gun18) |
| 19 | Doğrulama kütüphanesi | kendi anotasyonların (`@interface`), `@Retention`/`@Target`, reflection (`getDeclaredFields`, `setAccessible`), record bileşenleri, reflection sonucunu önbelleğe almak | 9 | [`gun19`](src/main/java/com/semih/gunluk/gun19) |
| 20 | Mini DI kapsayıcısı | bağımlılık enjeksiyonu, constructor reflection, singleton, arayüz bağlama, döngüsel bağımlılık tespiti, `SequencedSet`, `InvocationTargetException` | 10 | [`gun20`](src/main/java/com/semih/gunluk/gun20) |
| 21 | Metro rota planlayıcı | graf, komşuluk listesi, Dijkstra (`PriorityQueue`), BFS, yol geri izleme, Floyd-Warshall ile karşılaştırmalı rastgele test | 9 | [`gun21`](src/main/java/com/semih/gunluk/gun21) |
| 22 | LRU önbellek | `HashMap` + çift yönlü bağlı liste ile O(1), bekçi düğümler, `LinkedHashMap` `accessOrder` ve `removeEldestEntry`, isabet oranı | 10 | [`gun22`](src/main/java/com/semih/gunluk/gun22) |
| 23 | Not defteri API'si | JDK `HttpServer` ile Spring'siz REST, durum kodları (201, 204, 400, 404, 405, 413), `Location`/`Allow` başlıkları, elle JSON kaçışı, `HttpClient` ve `sendAsync` | 9 | [`gun23`](src/main/java/com/semih/gunluk/gun23) |
| 24 | Otomatik tamamlama | Trie (önek ağacı), en iyi k için `PriorityQueue` yığını, düğümde ön hesaplanmış sayaç, silerken boş dalları budamak, Türkçe küçük harf | 11 | [`gun24`](src/main/java/com/semih/gunluk/gun24) |
| 25 | JSON ayrıştırıcı | özyinelemeli iniş (recursive descent), `sealed` değer ağacı, kaçış dizileri ve `\u` kodları, satır/sütunlu hata, derinlik sınırı, `BigDecimal` ile hassas sayı, gidiş-dönüş testi | 11 | [`gun25`](src/main/java/com/semih/gunluk/gun25) |
| 26 | Sipariş durum makinesi | `enum` ile durum makinesi, izinli geçişler, `EnumSet` (`range`, `complementOf`), `EnumMap`, sabite özel gövde, ek kurallı geçiş (14 gün iade), ulaşılabilirlik testi | 10 | [`gun26`](src/main/java/com/semih/gunluk/gun26) |
| 27 | Bloom filtresi | olasılıklı veri yapısı, `BitSet`, tek hash'ten k hash (Kirsch–Mitzenmacher), 64 bitlik FNV-1a + SplitMix64, boyut ve yanılma oranı formülleri, ölçülen oranı formülle karşılaştırmak | 10 | [`gun27`](src/main/java/com/semih/gunluk/gun27) |
| 28 | Bellek ve değerle geçirme | stack ve heap, Java'nın her zaman değerle geçirmesi, referansın kopyası, aliasing ve savunmacı kopya, `final`'ın sınırı, String havuzu ve `intern`, `Integer` önbelleği, `StackOverflowError` | 10 | [`gun28`](src/main/java/com/semih/gunluk/gun28) |
| 29 | Kalıtım mı bileşim mi | `abstract class` ile `interface` farkı, şablon metot, `protected` kanca, `default` metot, kırılgan üst sınıf (`HashSet` sayacı), bileşim ve iletme, Liskov ihlali (`Stack`) | 10 | [`gun29`](src/main/java/com/semih/gunluk/gun29) |
| 30 | Refactoring katası | kod kokuları, karakterizasyon testi (sabit tohumlu 1.000 rastgele sipariş), ilkel takıntısını `enum` ve `record` ile çözmek, parametre nesnesi, erken dönüş, açık/kapalı ilkesi (kupon arayüzü), `double` yerine `BigDecimal` | 10 | [`gun30`](src/main/java/com/semih/gunluk/gun30) |
| 31 | Singleton ve doğru yazımları | `enum` singleton (yansıma ve serileştirmeye dayanıklı), holder deyimiyle tembel kurulum, `CyclicBarrier` ile yarışı göstermek, çift kontrollü kilit ve `volatile`, küresel durum sorunu, bağımlılık enjeksiyonu | 10 | [`gun31`](src/main/java/com/semih/gunluk/gun31) |
| 32 | Optional'ın doğru kullanımı | iç içe `null` kontrolü yerine `map`/`flatMap`/`filter` zinciri, `orElse` ile `orElseGet` farkı (sayaçla), `orElseThrow`, `or`, `Optional::stream`, `OptionalInt`, alan ve parametrede neden kullanılmaz | 10 | [`gun32`](src/main/java/com/semih/gunluk/gun32) |
| 33 | Kural motoru | hazır fonksiyonel arayüzler: `Predicate` (`and`, `or`, `negate`, `not`, kısa devre), `Function` (`andThen`, `compose`), `UnaryOperator` zinciri, `BiFunction`, `Consumer.andThen`; kuralları veri olarak tutmak | 10 | [`gun33`](src/main/java/com/semih/gunluk/gun33) |
| 34 | Banka transferi | `ReentrantLock`, deadlock'u testle göstermek (`ThreadMXBean.findDeadlockedThreads`), kilit sırasıyla önlemek, `tryLock` ile süreli deneme, `lockInterruptibly`, `Condition` ile bekleme, eşzamanlı yükte toplam paranın korunması | 10 | [`gun34`](src/main/java/com/semih/gunluk/gun34) |
| 35 | Kütüphane yönetimi (mini proje) | OOP, `Collection`, Stream (`groupingBy`, sıralama), `Optional`, `java.time` ve `Clock`, gecikme cezası (`BigDecimal`), dosyaya kaydetme ve yükleme, test edilebilir konsol komutları | 10 | [`gun35`](src/main/java/com/semih/gunluk/gun35) |
| 36 | Log analizcisi | `Files.lines` ile tembel okuma, regex adlı grupları, bozuk satırları saymak, yolu normalleştirmek (`/posts/12` → `/posts/{id}`), saatlik hata sayısı (`TreeMap`), p95 süre, `findFirst` ile erken durmak | 10 | [`gun36`](src/main/java/com/semih/gunluk/gun36) |
| 37 | Hız sınırlayıcı | sabit pencere ve sınırdaki açığı, kayan pencere, jeton kovası ve tembel dolum, `Retry-After` süresi, `ConcurrentHashMap.compute` ile atomik güncelleme, `Clock` ile test | 10 | [`gun37`](src/main/java/com/semih/gunluk/gun37) |
| 38 | Seyahat arama (CompletableFuture) | `thenCombine` ile paralel, `thenCompose` ile bağımlı çağrı, `allOf` ve `handle` ile kısmi sonuç, `anyOf`, `orTimeout` ve `completeOnTimeout`, `exceptionally`, `join`/`get` farkı, `thenApplyAsync` ve özel havuz | 10 | [`gun38`](src/main/java/com/semih/gunluk/gun38) |
| 39 | Para ve kuruş dağıtımı | değer nesnesi olarak para, `Currency` ve kuruş hanesi, `BigDecimal` `equals` tuzağı, eşit ve oranlı paylaştırma (en büyük kalan), `HALF_EVEN` ile `HALF_UP` farkı, KDV ayırma, Türkçe para biçimi | 10 | [`gun39`](src/main/java/com/semih/gunluk/gun39) |
| 40 | İş günleri ve saat dilimleri | iş günü ekleme ve sayma, `TemporalAdjuster` (sonraki iş günü, ayın son iş günü), sipariş kesim saati, `ZonedDateTime` ve `Instant`, yaz saati boşluğu ve çakışması, `Period` ile `Duration` farkı | 10 | [`gun40`](src/main/java/com/semih/gunluk/gun40) |
| 41 | Unicode ve slug | başlıktan slug (Türkçe harfler, `Normalizer` ile aksan atma), kelime sınırında kesme, `length` / kod noktası / grafem / UTF-8 bayt farkı, emojiyi bölmeden kısaltma, NFC ve NFD, Türkçe büyük-küçük harf tuzakları, Unicode rakamları | 10 | [`gun41`](src/main/java/com/semih/gunluk/gun41) |

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

## Gün 15: Depo ve OlayYayini (Observer deseni)

Stok azalınca ya da bitince haber veren bir depo. Depo kimin dinlediğini bilmiyor; sadece "şu oldu" diye yayınlıyor.

```java
OlayYayini<StokOlayi> olaylar = new OlayYayini<>();
Depo depo = new Depo(olaylar, 5);   // kritik seviye 5

olaylar.aboneOl(StokOlayi.StokAzaldi.class, olay -> System.out.println("Azaldı: " + olay));
olaylar.aboneOl(StokOlayi.StokBitti.class, olay -> System.out.println("Bitti: " + olay.urun()));

depo.ekle("Klavye", 10);
depo.sat("Klavye", 6);   // Azaldı: StokAzaldi[urun=Klavye, kalan=4, kritikSeviye=5]
depo.sat("Klavye", 4);   // Bitti: Klavye

try (Abonelik abonelik = olaylar.aboneOl(StokOlayi.class, System.out::println)) {
    // blok bitince abonelik kendiliğinden kapanıyor
}
```

**Neler öğrendim:**
- **Gevşek bağlılık:** Depo e-posta göndermiyor, paneli güncellemiyor, tedarikçiye sipariş açmıyor; bunları bilmiyor bile. Yeni bir tepki eklemek için depoya dokunmak gerekmiyor, sadece yeni bir dinleyici abone oluyor.
- **Türe göre abonelik:** `aboneOl(StokBitti.class, ...)` sadece biten stokları, `aboneOl(StokOlayi.class, ...)` hepsini alıyor. `Consumer<? super T>` sayesinde genel bir dinleyici özel olaylara da bağlanabiliyor.
- **`CopyOnWriteArrayList`:** Bir dinleyici olay sırasında yeni abonelik açarsa ya da kendi aboneliğini kapatırsa, düz `ArrayList` üzerinde dönerken `ConcurrentModificationException` alınır. Bu liste her değişiklikte kopyalanıyor, dönen döngü eski kopyada devam ediyor. Okuması çok, yazması az listeler için.
- **Bir dinleyicinin hatası diğerlerini bozmasın:** E-posta sunucusu kapalı diye panel güncellenmeden kalmamalı, satış da geri alınmamalı. Hatalar toplanıp çağırana veriliyor.
- **Eşiği geçiş anında haber ver:** Stok 4'ten 3'e inince tekrar "azaldı" denmiyor; sadece eşiğin üstünden altına inildiği an bir kez. Yoksa her satışta aynı uyarı gelir.
- **`AutoCloseable` abonelik:** Abonelik bir `close()` metodu; try-with-resources ile kullanılınca blok bitince kendiliğinden iptal oluyor, dinleyici unutulup bellekte kalmıyor.

| Dosya | Görevi |
|---|---|
| `Depo` | Stok, kritik seviye, olayları yayınlama |
| `OlayYayini` | Genel (generic) olay yayını, türe göre abonelik |
| `Abonelik` | İptal edilebilir abonelik (`AutoCloseable`) |
| `StokOlayi` | `StokEklendi`, `StokAzaldi`, `StokBitti` (`sealed interface` + `record`) |

---

## Gün 16: Hava durumu servisi (Decorator, Adapter, Factory)

Uygulama tek bir arayüze, `HavaDurumuServisi`'ne soruyor. Arkada üç desen birlikte çalışıyor: yabancı bir API'yi bizim arayüzümüze uyduran bir **adapter**, ona önbellek ve tekrar deneme ekleyen **decorator**'lar, hepsini doğru sırayla kuran bir **factory**.

```java
// Yabancı API şehri plaka koduyla istiyor, sıcaklığı "72.5F" diye metin olarak veriyor
HavaDurumuServisi servis = HavaDurumuServisleri.uretim(yabanciApi, Clock.systemUTC());

servis.sicaklik("İstanbul");   // 22.5 (santigrat)
servis.sicaklik("İstanbul");   // 22.5, 10 dakika boyunca önbellekten; API'ye gidilmiyor

// Factory'nin kurduğu zincir:
// OnbellekliServis → TekrarDeneyenServis (3 deneme) → YabanciApiAdaptoru → YabanciHavaApi
```

**Neler öğrendim:**
- **Adapter:** Değiştiremediğim bir kütüphaneyi kendi arayüzüme uyduruyor; şehir adını plaka koduna, Fahrenheit metnini santigrada çeviriyor. Yarın başka bir sağlayıcıya geçersem sadece adaptör değişiyor.
- **Decorator:** Aynı arayüzü uygulayıp içinde başka bir servis taşıyor ve ona davranış ekliyor. Önbellek ve tekrar deneme, servisi değiştirmeden üstüne giydiriliyor; istediğim kadarını, istediğim sırayla üst üste koyabiliyorum.
- **Kalıtım yerine bileşim:** Aynı şeyi alt sınıflarla yapsaydım `OnbellekliTekrarDeneyenServis` gibi her kombinasyon için ayrı bir sınıf gerekirdi.
- **Sıra önemli:** Önbellek en dışta: önbellekte varsa ağa hiç çıkılmıyor. Tekrar deneme içte olunca da sadece gerçek çağrılar tekrarlanıyor.
- **Neyi tekrar denemeli:** Zaman aşımı gibi geçici hatalar tekrar deneniyor, "bilinmeyen şehir" denenmiyor; beş kere sorsan da sonuç değişmez. Hepsi başarısız olunca son hata fırlatılıyor, öncekiler `addSuppressed` ile ona ekleniyor, kaybolmuyor.
- **Önbellek süresini test etmek:** 10 dakika beklemek yerine testte kendi `Clock` alt sınıfım var (`AyarlanabilirSaat`); `saat.ilerlet(Duration.ofMinutes(10))` ile zamanı ileri sarıyorum.

| Dosya | Görevi |
|---|---|
| `HavaDurumuServisi` | Uygulamanın kullandığı arayüz |
| `YabanciHavaApi` | Değiştiremediğimiz dış API'nin arayüzü |
| `YabanciApiAdaptoru` | Adapter: plaka kodu, Fahrenheit → santigrat |
| `OnbellekliServis` | Decorator: süreli önbellek (`Clock` ile) |
| `TekrarDeneyenServis` | Decorator: geçici hatalarda tekrar deneme |
| `HavaDurumuServisleri` | Factory: zinciri doğru sırayla kurar |

---

## Gün 17: Doğrulayıcılar (T.C. kimlik no, IBAN, telefon)

Türkiye'deki formlarda en sık karşılaşılan üç alanın gerçek doğrulaması. "11 rakam mı?" diye bakmak yetmiyor; T.C. kimlik numarası ve IBAN'ın içinde yanlış yazımı yakalayan kontrol haneleri var.

```java
TcKimlikNo.gecerliMi("10000000146");   // true
TcKimlikNo.gecerliMi("10000000147");   // false: son hane tutmuyor

Iban.gecerliMi("TR33 0006 1005 1978 6457 8413 26");   // true
Iban.bicimle("tr330006100519786457841326");           // "TR33 0006 1005 1978 6457 8413 26"

Telefon.normalle("+90 (532) 123-4567");   // Optional[+905321234567]
Telefon.okunur("5321234567");             // Optional[0532 123 45 67]
Telefon.normalle("0212 123 45 67");       // Optional.empty: sabit hat
```

Örneklerdeki numaralar algoritmaya uyan ama kimseye ait olmayan test numaraları.

**Neler öğrendim:**
- **Kontrol haneleri:** T.C. kimlik numarasının 10. hanesi tek ve çift sıradaki hanelerden, 11. hanesi ilk on hanenin toplamından hesaplanıyor. Bir rakamı yanlış yazmak neredeyse her zaman yakalanıyor. Bu sadece biçimin doğru olduğunu gösterir, numaranın birine ait olduğunu değil.
- **`Math.floorMod`:** Java'da `-3 % 10` sonucu `-3`. Kontrol hanesi hesabında ara değer negatif olabildiği için `floorMod` kullanıyorum; sonuç hep 0–9 arasında.
- **IBAN mod 97:** İlk dört karakter sona alınıyor, harfler sayıya çevriliyor (A=10 … Z=35), ortaya çıkan 30 haneli sayının 97'ye bölümünden kalan 1 olmalı. Bu sayı `long`'a sığmıyor; `BigInteger` yerine sayıyı soldan sağa okuyup her adımda sadece kalanı tutuyorum, kâğıt üzerinde uzun bölme gibi.
- **Karakterin sayı değeri:** `c - '0'` bir rakam karakterini sayıya çeviriyor; `Character.digit(c, 36)` hem rakamları hem harfleri (A=10) tek seferde çeviriyor.
- **Adlandırılmış gruplar:** `(?<operator>5\d{2})` ile yakalanan parçayı `m.group("operator")` diye adıyla alıyorum; desene grup eklense de numaralar kaymıyor.
- **`matches` ile `find` farkı:** `matches` bütün metnin desene uymasını istiyor. `find` olsaydı 13 haneli bir sayının içindeki 10 hane de "geçerli" sayılırdı.

| Dosya | Görevi |
|---|---|
| `TcKimlikNo` | Uzunluk, ilk hane ve iki kontrol hanesi |
| `Iban` | Biçim, Türkiye için 26 karakter, mod 97, dörtlü gruplama |
| `Telefon` | Cep numarasını E.164 (`+90...`) ve okunur biçime getirme |

---

## Gün 18: Sonuc ve UyeKaydi (İstisnasız hata yönetimi)

Bir üyelik formunu doğrulayan servis. Gün 17'deki doğrulayıcıları kullanıyor ve hiç istisna fırlatmıyor: sonuç ya `Basarili` (üye) ya `Hatali` (hata listesi).

```java
UyeKaydi kayit = new UyeKaydi(Year.of(2026));

kayit.kaydet(new KayitFormu("Ayşe Yılmaz", "10000000146", "TR33 0006 1005 1978 6457 8413 26",
        "0532 123 45 67", "1998"));
// Basarili[deger=Uye[ad=Ayşe Yılmaz, tcKimlikNo=10000000146, iban=TR33 0006 1005 1978 6457 8413 26,
//          telefon=+905321234567, dogumYili=1998]]

kayit.kaydet(new KayitFormu(" ", "10000000147", "TR00", "0212 123 45 67", "bin dokuz yüz"));
// Hatali[hatalar=[Ad boş olamaz, T.C. kimlik numarası geçersiz, IBAN geçersiz,
//                 Cep telefonu numarası geçersiz, Doğum yılı sayı olmalı]]

String cevap = switch (sonuc) {
    case Sonuc.Basarili<Uye>(Uye uye) -> "Hoş geldin " + uye.ad();
    case Sonuc.Hatali<Uye>(List<String> hatalar) -> hatalar.size() + " hata";
};
```

**Neler öğrendim:**
- **Beklenen hata istisna değil:** Yanlış girilmiş bir form alanı programın bozulması değil, olağan bir durum. `Sonuc<Uye>` dönüş tipi "bu başarısız olabilir" diye imzada yazıyor; çağıran bunu görmezden gelemiyor. İstisnalar gerçekten beklenmeyen durumlara kalıyor.
- **`sealed` + `switch`:** `Sonuc` sadece `Basarili` ya da `Hatali` olabildiği için `switch` iki durumu da ele alınca `default` gerekmiyor. Generic record pattern (`case Basarili<Uye>(Uye uye)`) değeri doğrudan çıkarıyor.
- **`map` ve `flatMap`:** `map` başarılı değeri dönüştürüyor, hatalıysa fonksiyonu hiç çağırmıyor. `flatMap` sıradaki adım da başarısız olabiliyorsa; yaş ve "aynı T.C. ile ikinci kayıt" kuralları bu şekilde zincirleniyor ve ilk hatada duruyor.
- **Biriktirmek mi, durmak mı:** Form alanları birbirinden bağımsız; hepsi birden kontrol edilip bütün hatalar tek seferde gösteriliyor, kullanıcı beş kez gönder-düzelt yapmıyor. İş kuralları ise sırayla çalışıyor; reşit olmayan birinin T.C.'sinin kayıtlı olup olmadığına bakmanın anlamı yok.
- **Eski kodla köprü:** `Integer.parseInt` istisna fırlatıyor; `Sonuc.dene(...)` onu sonuca çeviriyor. `Optional` dönen `Telefon.normalle` için de `Sonuc.optionaldan(...)` var.
- **`katla`:** İki durumu tek bir değere indiriyor, ör. bir controller'da `201` ya da `400` cevabı.

| Dosya | Görevi |
|---|---|
| `Sonuc` | `Basarili` / `Hatali`, `map`, `flatMap`, `katla`, `dene`, `tumHatalar` (`sealed interface` + generic `record`) |
| `UyeKaydi` | Alan doğrulama (hatalar biriktiriliyor) ve iş kuralları (ilk hatada duruluyor) |
| `KayitFormu` | Forma yazılanlar, ham metin (`record`) |
| `Uye` | Doğrulanmış, düzenlenmiş üye (`record`) |

---

## Gün 19: Dogrulayici (Anotasyonlar ve reflection)

Spring'deki `@NotBlank`, `@Size` ve `@Valid`'in küçük bir benzeri. Kurallar alanların üstüne anotasyon olarak yazılıyor, `Dogrulayici` onları çalışırken reflection ile okuyup uyguluyor.

```java
public record UyelikFormu(
        @Zorunlu @Uzunluk(min = 2, max = 50) String ad,
        @Zorunlu @Desen(value = "[^@\\s]+@[^@\\s]+\\.[a-z]{2,}", mesaj = "geçerli bir e-posta olmalı") String eposta,
        @Aralik(min = 18, max = 120) int yas,
        @Desen(value = "\\d{5}", mesaj = "5 haneli olmalı") String postaKodu) {
}

new Dogrulayici().dogrula(new UyelikFormu("A", "ayse@", 15, "347"));
// [ad: 2 ile 50 karakter arasında olmalı, eposta: geçerli bir e-posta olmalı,
//  yas: 18 ile 120 arasında olmalı, postaKodu: 5 haneli olmalı]
```

**Neler öğrendim:**
- **Anotasyon tek başına hiçbir şey yapmaz:** `@Zorunlu` sadece bir işaret. Onu okuyup bir şey yapan kod olmazsa etkisi yok. Spring'in anotasyonları da böyle çalışıyor; arkada onları okuyan bir kod var.
- **`@Retention(RUNTIME)`:** Varsayılan `CLASS`; anotasyon derlenmiş dosyada duruyor ama çalışırken görünmüyor ve `getAnnotation` `null` dönüyor. Testte iki anotasyonu yan yana koyup bunu gösterdim. `@Target(FIELD)` da nereye yazılabileceğini sınırlıyor.
- **Reflection:** `getDeclaredFields()` sınıfın alanlarını, `field.get(nesne)` değerini veriyor; derleme anında hiç bilinmeyen bir sınıf çalışırken inceleniyor. `private` alanı okumak için `setAccessible(true)` gerekiyor.
- **Record'larda sıra:** `getDeclaredFields()` sıra garantisi vermiyor; record'larda `getRecordComponents()` sırası garanti, hatalar ona göre diziliyor. Record bileşenine yazılan anotasyon `@Target(FIELD)` sayesinde gizli alana da geçiyor.
- **Reflection pahalı:** Her sınıfın kurallı alanları bir kez bulunup `ConcurrentHashMap`'te saklanıyor. 1.001 doğrulamada reflection sadece iki kez (iki sınıf için) çalışıyor; bunun testi var.

| Dosya | Görevi |
|---|---|
| `Dogrulayici` | Alanları tarar, kuralları uygular, sınıf bilgisini önbelleğe alır |
| `Zorunlu`, `Uzunluk`, `Aralik`, `Desen` | Kural anotasyonları |
| `Ihlal` | Alan adı ve mesaj (`record`) |
| `UyelikFormu` | Örnek form |

---

## Gün 20: Kapsayici (Spring'siz bağımlılık enjeksiyonu)

Spring'in `ApplicationContext`'inin çok küçük bir benzeri. Sınıflar ihtiyaç duydukları nesneleri `new` ile kendileri oluşturmuyor, constructor'da istiyor; kapsayıcı reflection ile bakıp hepsini doğru sırayla kuruyor.

```java
public class SiparisServisi {
    public SiparisServisi(SiparisDeposu depo, BildirimServisi bildirimler) { ... }
}

Kapsayici kapsayici = new Kapsayici()
        .bagla(SiparisDeposu.class, BellekSiparisDeposu.class);   // arayüz → uygulama

SiparisServisi servis = kapsayici.getir(SiparisServisi.class);
servis.siparisVer("Ayşe", "250.00");   // Siparis[no=1, musteri=Ayşe, tutar=250.00]
kapsayici.getir(BildirimServisi.class).gonderilenler();
// [Ayşe, 1 numaralı siparişin alındı]: servisin kullandığıyla aynı nesne

new Kapsayici().getir(Tavuk.class);
// KapsayiciHatasi: Döngüsel bağımlılık: Tavuk → Yumurta → Tavuk
```

**Neler öğrendim:**
- **Bağımlılık enjeksiyonu:** `SiparisServisi` hangi depoyla çalışacağını bilmiyor, sadece `SiparisDeposu` arayüzünü istiyor. Üretimde kapsayıcı bellek ya da veritabanı deposunu veriyor, testte elle istediğimi veriyorum. Spring'in `@Autowired`'ı da aslında bunu yapıyor.
- **Constructor'dan bağımlılık bulmak:** `getConstructors()` ve `getParameterTypes()` hangi tiplerin gerektiğini söylüyor; kapsayıcı önce onları (özyinelemeyle onların bağımlılıklarını da) kurup sonra `newInstance` çağırıyor. Birden fazla constructor varsa `@Enjekte` işaretlisi seçiliyor.
- **Singleton:** Her tip bir kez kuruluyor ve saklanıyor; servis ile test aynı depo nesnesini görüyor. Spring bean'lerinin varsayılanı da bu.
- **Döngü tespiti:** Tavuk yumurtayı, yumurta tavuğu isterse sonsuz özyineleme olurdu. Kurulmakta olan tipler bir `SequencedSet`'te (Java 21, sırayı koruyan küme) tutuluyor; bir tip ikinci kez gelirse döngü "Tavuk → Yumurta → Tavuk" diye raporlanıyor. Spring de aynı durumda `BeanCurrentlyInCreationException` veriyor.
- **`InvocationTargetException`:** Reflection ile çağrılan constructor hata fırlatırsa hata bu sarmalayıcının içinde geliyor; asıl nedeni `getCause()` ile çıkarıp anlaşılır bir mesajla iletiyorum.

| Dosya | Görevi |
|---|---|
| `Kapsayici` | Bağlama, kurma, singleton saklama, döngü tespiti |
| `Enjekte` | Kullanılacak constructor'ı işaretler |
| `KapsayiciHatasi` | Kurulamayan nesne için hata |
| `SiparisServisi`, `SiparisDeposu`, `BellekSiparisDeposu`, `BildirimServisi`, `Siparis` | Örnek uygulama |

---

## Gün 21: MetroAgi (Graflar, Dijkstra ve BFS)

İstanbul raylı sisteminden küçük bir parça üzerinde iki durak arası rota: en kısa sürede mi, en az durakla mı? (Süreler örnek.)

```java
MetroAgi ag = new MetroAgi()
        .hatEkle("M2", 2, "Yenikapı", "Vezneciler", "Haliç", "Şişhane", "Taksim", "Osmanbey")
        .hatEkle("Marmaray", 4, "Yenikapı", "Sirkeci", "Üsküdar")
        .hatEkle("T1", 4, "Sirkeci", "Karaköy", "Tophane", "Kabataş")
        .baglantiEkle("Kabataş", "Taksim", 3, "F1");

ag.enHizli("Sirkeci", "Taksim");
// Rota[duraklar=[Sirkeci, Yenikapı, Vezneciler, Haliç, Şişhane, Taksim], dakika=12, aktarma=1]

ag.enAzDurak("Sirkeci", "Taksim");
// Rota[duraklar=[Sirkeci, Karaköy, Tophane, Kabataş, Taksim], dakika=15, aktarma=1]
```

**Neler öğrendim:**
- **Graf:** Duraklar düğüm, aralarındaki yollar kenar, süreler kenarın ağırlığı. Komşuluk listesiyle (`Map<String, List<Baglanti>>`) tutuluyor; her durak sadece gidebildiği yerleri biliyor.
- **BFS en az durağı bulur:** Önce 1 durak ötedekiler, sonra 2 durak ötedekiler... sıradan bir kuyrukla. Hedefe ilk varıldığı an en az durakla varılmış oluyor ama süreye hiç bakmıyor.
- **Dijkstra en kısa süreyi bulur:** Kuyruk yerine `PriorityQueue`; her seferinde o ana kadar en yakın durak çıkıyor. Ağırlıklar pozitif olduğu için o durağa daha kısa bir yol bulunamaz. Negatif ya da sıfır süre bu yüzden reddediliyor.
- **Eski kayıtları atla:** Bir durağa sonradan daha kısa yol bulununca eski kayıt kuyrukta kalıyor; çıktığında "bundan iyisi var" diye atlanıyor. Bunu yakalayan ayrı bir test var.
- **Yolu geri izlemek:** Her durak için "nereden geldim" saklanıyor, varıştan başlangıca geri yürünüp liste ters çevriliyor. Hat değişimleri de aynı yolda sayılıyor.
- **Kaba kuvvetle karşılaştırma:** Floyd-Warshall O(n³) ama çok basit; yüz rastgele ağda her durak çifti için Dijkstra'yla aynı süreyi bulduğunu test ediyorum.

| Dosya | Görevi |
|---|---|
| `MetroAgi` | Graf, Dijkstra, BFS, rota geri izleme |
| `Baglanti` | Hedef durak, dakika, hat (`record`) |
| `Rota` | Duraklar, toplam süre, aktarma sayısı (`record`) |

---

## Gün 22: LruOnbellek (Önbellek ve bağlı liste)

Sınırlı boyutlu bir önbellek: dolunca en uzun süredir kullanılmayan kayıt atılıyor (LRU, Least Recently Used). Mülakatların klasik sorusu; önce kendi O(1) çözümüm, sonra Java'nın hazır parçalarıyla 10 satırlık hâli.

```java
LruOnbellek<String, String> onbellek = new LruOnbellek<>(3);
onbellek.koy("/urun/1", "Klavye");
onbellek.koy("/urun/2", "Mouse");
onbellek.koy("/urun/3", "Monitör");
onbellek.getir("/urun/1");              // okumak da "kullanmak": /urun/1 tazelendi
onbellek.koy("/urun/4", "Kulaklık");    // dolu: en eski /urun/2 atılıyor

onbellek.kullanimSirasi();   // [/urun/4, /urun/1, /urun/3]
onbellek.getir("/urun/2");   // Optional.empty
onbellek.isabetOrani();      // 0.5
```

**Neler öğrendim:**
- **İki yapı birlikte:** `HashMap` anahtarı O(1)'de buluyor ama sırayı bilmiyor; bağlı liste sırayı tutuyor ama aramak O(n). Map'te değer olarak liste düğümünü saklayınca ikisinin iyi yanı birleşiyor: hem bulmak hem en eskiyi atmak O(1).
- **Çift yönlü bağlı liste:** Bir düğümü listenin ortasından çıkarmak için önceki ve sonraki komşusunun işaretçilerini değiştirmek yetiyor. Tek yönlü listede öncekini bulmak için baştan yürümek gerekirdi.
- **Bekçi (sentinel) düğümler:** Başta ve sonda içi boş iki sahte düğüm var; liste hiç boş olmuyor, "ilk eleman mı, son eleman mı?" diye özel durum yazmak gerekmiyor.
- **`LinkedHashMap`'in gizli özelliği:** `new LinkedHashMap<>(16, 0.75f, true)`: üçüncü parametre sırayı ekleme yerine son erişime göre tutuyor. `removeEldestEntry`'yi ezince LRU önbellek 10 satırda hazır. Gerçek projede bu (ya da Caffeine) kullanılır.
- **İki sürümü karşılaştırmak:** 20.000 rastgele okuma/yazma işleminde iki sürümün her adımda aynı sonucu ve sonunda aynı sırayı verdiğini test ediyorum.

| Dosya | Görevi |
|---|---|
| `LruOnbellek` | `HashMap` + çift yönlü bağlı liste, atılınca bildirim, isabet oranı |
| `KisaLruOnbellek` | Aynısı `LinkedHashMap` ile |

---

## Gün 23: NotDefteriSunucusu (Spring'siz HTTP: `HttpServer` ve `HttpClient`)

JDK'nın içinde gelen `HttpServer` ile küçük bir REST API. Spring Boot'un bizim yerimize yaptıklarını (yolu ayırmak, metoda göre dallanmak, durum kodu, başlıklar, JSON) burada elle yapıyorum. Testler gerçek bir sunucu açıp Java'nın `HttpClient`'ıyla istek atıyor.

```text
POST   /notlar        "Spring Boot'a çalış"   → 201, Location: /notlar/1
                                                 {"id":1,"metin":"Spring Boot'a çalış"}
GET    /notlar                                → 200  [{"id":1,"metin":"Spring Boot'a çalış"}]
GET    /notlar/99                             → 404  {"hata":"99 numaralı not yok"}
DELETE /notlar/1                              → 204  (gövde yok)
PUT    /notlar                                → 405, Allow: GET, POST
POST   /notlar        "   "                   → 400  {"hata":"Not boş olamaz"}
POST   /notlar        (1001 karakter)         → 413
```

**Neler öğrendim:**
- **Durum kodları anlam taşıyor:** Oluşturunca `201` ve yeni kaydın adresi `Location` başlığında; silince `204` ve gövde yok; yanlış metotta `405` ve `Allow` başlığında geçerli metotlar; çok büyük gövdede `413`. Hepsi `200` dönen bir API'de istemci sonucu anlamak için gövdeyi okumak zorunda kalır.
- **JSON'u elle yazmak tehlikeli:** Metni tırnak içine olduğu gibi koyunca içindeki `"` JSON'u bozuyor, hatta araya sahte alan eklenebiliyor. Tırnak, ters bölü ve satır sonu kaçış dizisine çevriliyor; bunun testi var. Jackson'ın neden var olduğunu anladım.
- **Uzunluk bayt cinsinden:** `sendResponseHeaders`'a verilen uzunluk karakter değil bayt sayısı; "ş" UTF-8'de 2 bayt. Karakter sayısı verilseydi Türkçe harfli cevaplar kesik giderdi.
- **Son çare hata yakalayıcı:** Beklenmeyen bir hata olursa `500` ve genel bir mesaj dönüyor, iç ayrıntı dışarı çıkmıyor. Spring'deki `@ExceptionHandler(Exception.class)`'ın elle yazılmışı.
- **Testte port 0:** Sunucu `0` portuyla açılınca işletim sistemi boş bir port seçiyor; testler sabit bir portla çakışmıyor. `@AfterEach` her testten sonra sunucuyu kapatıyor.
- **Eşzamanlılık bir arada:** Sunucu her isteği bir virtual thread'de işliyor (Gün 14), notlar `ConcurrentHashMap`'te, numaralar `AtomicLong`'dan (Gün 13). Testte `HttpClient.sendAsync` ile 100 istek birden gönderip numaraların çakışmadığını doğruluyorum.

| Dosya | Görevi |
|---|---|
| `NotDefteriSunucusu` | Sunucu, yönlendirme, durum kodları, başlıklar |
| `Not` | Not numarası ve metni (`record`) |
| `Json` | Metin kaçışı ve hata gövdesi |

---

## Gün 24: OtomatikTamamlama (Trie ve en iyi k)

Bir arama kutusu için otomatik tamamlama: "ist" yazılınca en çok arananlar önce olmak üzere "istanbul", "istasyon"...

```java
OtomatikTamamlama tamamlama = new OtomatikTamamlama();
tamamlama.ara("istanbul");   // (5 kez)
tamamlama.ara("istasyon");   // (2 kez)
tamamlama.ara("istek");      // (2 kez)
tamamlama.ara("izmir");      // (4 kez)

tamamlama.oneriler("İst", 2);
// [Oneri[kelime=istanbul, siklik=5], Oneri[kelime=istasyon, siklik=2]]
tamamlama.onekSayisi("ist");   // 3
tamamlama.iceriyorMu("ist");   // false: ağaçta bir yol, ama kelime değil
```

**Neler öğrendim:**
- **Trie (önek ağacı):** Her düğüm bir harf, kökten bir düğüme giden yol bir önek. "istanbul" ile "istasyon" "ista" kısmını paylaşıyor. Bir önekle başlayan kelimelere ulaşmak, kaç kelime olursa olsun önek uzunluğu kadar adım; listede her kelimeye `startsWith` sormak bütün listeyi gezmek demek.
- **Düğümde hazır sayaç:** Her düğüm kendisinden geçen kelime sayısını tutuyor; "ist ile kaç kelime başlıyor?" sorusu alt ağacı gezmeden önek uzunluğu kadar adımda cevaplanıyor. Bedeli, ekleme ve silmede yol boyunca sayaçları güncellemek.
- **En iyi k için yığın:** Bütün adayları sıralamak yerine k boyutlu bir `PriorityQueue` tutuyorum; tepede en kötüsü duruyor, yeni gelen daha iyiyse en kötü atılıyor. 1.000 aday arasından 5 öneri için 1.000 elemanlı sıralama yok.
- **Silerken budamak:** Silinen kelimenin artık başka hiçbir kelimenin parçası olmayan harfleri ağaçtan koparılıyor; yoksa boş dallar birikirdi. Başka kelimenin öneki olan bir kelime ("is") silinince hiçbir düğüm atılmıyor, sadece "burada kelime bitiyor" işareti kalkıyor. İkisinin de testi var.
- **Türkçe küçük harf:** `toLowerCase(Locale.forLanguageTag("tr-TR"))`: "I" → "ı", "İ" → "i". Varsayılan dil ayarına bırakılsa "IĞDIR" yanlış küçülürdü.

| Dosya | Görevi |
|---|---|
| `OtomatikTamamlama` | Trie, öneriler, önek sayısı, silme |
| `Oneri` | Kelime ve aranma sayısı (`record`) |

---

## Gün 25: JsonAyristirici (Kütüphanesiz JSON)

Jackson kullanmadan JSON metnini bir değer ağacına çeviren ayrıştırıcı ve ağacı tekrar metne çeviren yazıcı.

```java
JsonDegeri kok = JsonAyristirici.ayristir("{ \"ad\": \"Ayşe\", \"diller\": [\"Java\", \"SQL\"], \"yas\": 27 }");
// Nesne[alanlar={ad=Metin[deger=Ayşe], diller=Dizi[elemanlar=[Metin[deger=Java], Metin[deger=SQL]]],
//                yas=Sayi[deger=27]}]

JsonYazici.yaz(kok);   // {"ad":"Ayşe","diller":["Java","SQL"],"yas":27}

JsonAyristirici.ayristir("""
        {
          "ad": "Ayşe",
          "yas": 27
          "sehir": "İzmir"
        }""");
// JsonHatasi: 4. satır, 3. sütun: ',' ya da '}' bekleniyordu
```

**Neler öğrendim:**
- **Özyinelemeli iniş:** JSON'un dilbilgisindeki her kural için bir metot var (`nesne()`, `dizi()`, `metin()`, `sayi()`). `deger()` sıradaki karaktere bakıp hangisinin çağrılacağına karar veriyor: `{` ise nesne, `[` ise dizi. Nesnenin içinde yine `deger()` çağrıldığı için iç içe yapılar kendiliğinden çözülüyor.
- **`sealed` değer ağacı:** JSON'da altı tür var: nesne, dizi, metin, sayı, mantıksal ve `null`. `JsonDegeri` sadece bunlar olabiliyor; yazıcıdaki `switch` altısını da ele aldığı için `default` yok. `null` için tek örneği olan bir `enum` kullandım.
- **Sayılar `double` değil `BigDecimal`:** `double` ile "12345678901234567890" okunup yazılınca son haneler bozulur, "0.1" de tam saklanamaz. JSON'un sayı kuralı da katı: "01", "+1", "1." ve ".5" geçersiz; hepsinin testi var.
- **İyi hata mesajı:** "Geçersiz JSON" yerine "4. satır, 3. sütun: ',' ya da '}' bekleniyordu". Konum karakter sırası olarak tutuluyor, hata anında satır ve sütuna çevriliyor.
- **Kötü niyetli girdi:** `[[[[...` diye 100.000 kez iç içe açılan bir girdi, özyinelemeli bir ayrıştırıcıda `StackOverflowError` ile bütün uygulamayı düşürebilir. 200 seviyelik bir sınır var. Aynı alanın iki kez yazılması da reddediliyor; JSON standardı bunu tanımsız bırakıyor.
- **Gidiş-dönüş testi:** `yaz(ayristir(json))` ilk metni aynen vermeli. Ayrıştırıcıyla yazıcıyı birbirine karşı test etmenin kolay bir yolu.

| Dosya | Görevi |
|---|---|
| `JsonAyristirici` | Metin → değer ağacı (özyinelemeli iniş) |
| `JsonYazici` | Değer ağacı → metin |
| `JsonDegeri` | `Nesne`, `Dizi`, `Metin`, `Sayi`, `Mantiksal`, `Bos` (`sealed interface`) |
| `JsonHatasi` | Satır ve sütunlu ayrıştırma hatası |

---

## Gün 26: Siparis ve SiparisDurumu (Enum ile durum makinesi)

Bir siparişin hangi durumdan hangisine geçebileceği kurallı: kargodaki sipariş iptal edilemez, teslim edilmeden iade olmaz, iade 14 günle sınırlı. Bu kurallar dağınık `if`'lerde değil, tek bir `enum`'da.

```text
OLUSTURULDU → ODENDI → HAZIRLANIYOR → KARGODA → TESLIM_EDILDI → IADE_EDILDI
     └───────────┴───────────┴──→ IPTAL_EDILDI
```

```java
Siparis siparis = new Siparis("SE-1001", Clock.systemUTC());
siparis.gec(SiparisDurumu.ODENDI, "Kartla ödendi");
siparis.gec(SiparisDurumu.TESLIM_EDILDI, "Atlayalım");
// GecersizGecisHatasi: SE-1001: Ödeme onaylandı → Teslim edildi olmaz.
//                      Buradan gidilebilecekler: [HAZIRLANIYOR, IPTAL_EDILDI]

siparis.iptalEt("Vazgeçtim");   // KARGODA'dan sonra olsaydı hata
siparis.gecmis();               // her adım: önceki, yeni, zaman, açıklama
```

**Neler öğrendim:**
- **Durum makinesi:** `setDurum(...)` yok; durum sadece `gec(...)` ile değişiyor ve her geçiş `enum`'daki kurala göre kontrol ediliyor. Yeni bir durum eklemek tek bir yerde değişiklik demek; kurallar kod tabanına dağılmıyor.
- **Geçişler neden `switch`'te:** `KARGODA(EnumSet.of(TESLIM_EDILDI))` gibi bir constructor parametresi derlenmiyor; `KARGODA` tanımlanırken `TESLIM_EDILDI` henüz yok (ileri referans). Geçişleri `sonrakiler()` metoduna koyunca sorun kalmıyor; derleyici bütün durumların ele alındığını da kontrol ediyor.
- **`EnumSet`:** İçeride tek bir `long`'un bitleri; `contains` bir bit kontrolü. `EnumSet.range(OLUSTURULDU, HAZIRLANIYOR)` sıradaki ilk üç durumu, `complementOf` "geri kalan hepsi"ni veriyor.
- **`EnumMap`:** Anahtarı enum olan bir harita; içeride `ordinal` sırasına göre bir dizi. `HashMap`'ten hızlı ve tanım sırasıyla dolaşılıyor; paneldeki durum sayıları hep aynı sırada.
- **Sabite özel gövde:** `IPTAL_EDILDI { ... }` sadece o sabit için metodu eziyor; iptal mesajı farklı, diğerleri ortak metodu kullanıyor. Bu sabit kendi anonim alt sınıfı oluyor (`getClass()` farklı, `getDeclaringClass()` aynı).
- **Ölü durum testi:** Grafı BFS ile gezip (Gün 21) her duruma başlangıçtan ulaşılabildiğini test ediyorum; yanlışlıkla ulaşılamaz bir durum eklenirse test kırılıyor.

| Dosya | Görevi |
|---|---|
| `SiparisDurumu` | Durumlar, izinli geçişler, görünen ad, müşteri mesajı (`enum`) |
| `Siparis` | Kurallı geçiş, iptal, 14 günlük iade, geçmiş |
| `DurumDegisimi` | Önceki, yeni, zaman, açıklama (`record`) |
| `GecersizGecisHatasi` | İzinsiz geçiş için hata |
| `SiparisPaneli` | Durum sayıları (`EnumMap`), açık durumlar (`EnumSet.complementOf`) |

---

## Gün 27: BloomFiltresi (Olasılıklı veri yapısı)

"Bu kullanıcı adı alınmış mı?" sorusuna veritabanına gitmeden, çok az bellekle cevap veren bir küme. Cevabı `false` ise kelime **kesinlikle** yok; `true` ise **muhtemelen** var. Ara sıra, ayarlanan oranda yanılıyor; o zaman veritabanına sormak gerekiyor.

```java
BloomFiltresi filtre = BloomFiltresi.icin(10_000, 0.01);   // 10.000 kullanıcı adı, %1 yanılma
filtre.bitSayisi();    // 95851 bit (≈ 12 KB)
filtre.hashSayisi();   // 7

for (int i = 0; i < 10_000; i++) filtre.ekle("kullanici" + i);

filtre.belkiVar("kullanici42");   // true: eklenmişti
filtre.belkiVar("semih");         // false: kesinlikle eklenmemiş

// 100.000 hiç eklenmemiş kelimeyle ölçüm:
// yanılma %1,007 (formülün tahmini %1,004), bitlerin %51,8'i dolu
```

**Neler öğrendim:**
- **Nasıl çalışıyor:** m bitlik bir dizi ve k hash fonksiyonu. Eklerken kelimenin k hash'inin gösterdiği k bit 1 yapılıyor; sorarken k bitin hepsine bakılıyor. Biri bile 0'sa kelime hiç eklenmemiş. Hepsi 1 ise ya eklenmiş ya da başka kelimeler o bitleri tesadüfen 1 yapmış. Bu yüzden yanlış negatif hiç olmuyor, yanlış pozitif oluyor.
- **Çok az bellek:** Kelimeler saklanmıyor, sadece bitler. 1 milyon kullanıcı adı %1 yanılmayla 9,6 milyon bitte (≈ 1,2 MB) duruyor; aynı adlar bir `HashSet<String>`'te onlarca MB tutar. Bedeli: silme yok, listeleme yok, ara sıra "belki".
- **Boyut formülden:** `m = −n·ln(p) / (ln 2)²` ve `k = (m/n)·ln 2`. 1.000 eleman ve %1 için 9.586 bit ve 7 hash. Doğru k seçilince filtre dolduğunda bitlerin yarısı 1 oluyor; testte ölçüyorum.
- **Tek hash'ten k hash:** 7 ayrı hash fonksiyonu yazmak yerine 64 bitlik tek bir hash'in iki yarısından `h1 + i·h2` ile yedi konum türetiliyor (Kirsch–Mitzenmacher). `String.hashCode()` yetmiyor: 32 bit ve "Aa" ile "BB" aynı sonucu veriyor. Onun yerine FNV-1a ve bitleri karıştıran SplitMix64 son adımı var.
- **`BitSet`:** Bitleri `long` dizisinde paketleyip tutuyor; `set`, `get` ve 1 olan bitleri sayan `cardinality` hazır. İki filtrenin birleşimi tek bir `or`.
- **Kapasiteyi aşmak:** Beklenenin beş katı eleman eklenince bitlerin %95'inden fazlası doluyor ve filtre neredeyse her şeye "belki" diyor. Bu yüzden boyut baştan doğru seçilmeli.

| Dosya | Görevi |
|---|---|
| `BloomFiltresi` | Boyut hesabı, ekleme, sorgu, yanılma tahmini, birleşim, 64 bitlik hash |

---

## Gün 28: SepetIslemleri ve Kampanya (Bellek ve değerle geçirme)

Mülakatın klasik sorusu: "Java referansla mı geçirir, değerle mi?" Cevap: **her zaman değerle.** Ama referans tipinde kopyalanan değer nesnenin kendisi değil, adresi. Bu gün o farkı testlerle gösteriyor.

```text
  stack (çağıran)          heap
  sepet ──────────────►  [Sepet: Klavye, Mouse]
  stack (metot)            ▲
  s     ─────────────────┘   aynı adresin kopyası
```

```java
Sepet sepet = new Sepet();
sepet.ekle("Klavye");

SepetIslemleri.hediyeEkle(sepet);    // s.ekle(...): nesnenin içi değişti, çağıran görür
SepetIslemleri.yanlisBosalt(sepet);  // s = new Sepet(): sadece metodun kopyası değişti
sepet.urunler();                     // [Klavye, Hediye paketi]: boşalmadı

List<String> liste = new ArrayList<>(List.of("Kalem"));
HataliKampanya hatali = new HataliKampanya(liste);   // listeyi kopyalamadan saklıyor
liste.add("Defter");
hatali.kapsiyorMu("Defter");          // true: kampanya habersizce değişti
```

**Neler öğrendim:**
- **Değerle geçirme:** Metoda giden her parametre kopyalanıyor. İlkel tipte kopya sayının kendisi; referans tipinde nesnenin adresi. Metot o adres üzerinden nesnenin **içini** değiştirebiliyor, ama parametresine yeni bir nesne atarsa çağıranın değişkeni etkilenmiyor. Java'da C++'taki gibi bir `swap(a, b)` yazılamamasının sebebi bu.
- **Stack ve heap:** Yerel değişkenler ve parametreler thread'in stack'inde, `new` ile kurulan nesneler heap'te. Stack her çağrıda bir çerçeve büyüyor; özyinelemede 10 milyon seviye `StackOverflowError` veriyor, aynı toplam döngüyle tek çerçevede bitiyor. Testte 512 MB stack'li bir `Thread` kurup aynı özyinelemeyi 1 milyon seviyede çalıştırdım: stack boyutu thread başına ayarlanabiliyor.
- **Aliasing ve savunmacı kopya:** İki değişken aynı nesneyi gösteriyorsa biri değişince öteki de "değişiyor". Dışarıdan gelen listeyi saklarken `List.copyOf` ile kopyalamak şart. `final` sadece alanın başka bir listeye yönlendirilmesini engelliyor, listenin içini dondurmuyor.
- **String havuzu:** `"sem" + "ih"` derleme anında birleşiyor ve havuzdaki `"semih"` ile aynı nesne (`==` true). `new String("semih")` ve çalışma anında birleştirilen metin yeni nesne; `intern()` havuzdakini döndürüyor. Ders: String'i her zaman `equals` ile karşılaştır.
- **`Integer` önbelleği:** `Integer.valueOf` −128 ile 127 arasını önbellekten veriyor; `127 == 127` true, `128 == 128` false. Kutulanmış sayıyı da `equals` ile karşılaştır. `null` bir `Integer`'ı `int`'e açmak `NullPointerException`.

| Dosya | Görevi |
|---|---|
| `Sepet` | İçi değişebilen örnek nesne |
| `SepetIslemleri` | İçi değiştirmek, parametreye yeni nesne atamak (bilerek hatalı), ilkel tip kopyası |
| `HataliKampanya` | Listeyi kopyalamadan saklıyor: aliasing (bilerek hatalı) |
| `Kampanya` | Savunmacı kopya (`List.copyOf`) |
| `Ozyineleme` | Aynı toplam özyinelemeyle ve döngüyle |

---

## Gün 29: Hesap ve SayanKume (Kalıtım mı bileşim mi)

İki soru: "abstract class ile interface farkı ne?" ve "neden kalıtım yerine bileşim?" İlki için banka hesapları, ikincisi için kaç eleman eklendiğini sayan bir küme.

```java
Hesap vadeli = new VadeliHesap("TR-2", new BigDecimal("0.48"));
vadeli.paraYatir(new BigDecimal("1000"));
vadeli.paraCek(new BigDecimal("300"));   // 303.00 düştü: %1 vade bozma cezası

HataliSayanKume<String> kalitim = new HataliSayanKume<>();   // extends HashSet
kalitim.addAll(List.of("a", "b", "c"));
kalitim.denemeSayisi();   // 6: HashSet.addAll içeride bizim add'imizi çağırdı

SayanKume<String> bilesim = new SayanKume<>();               // içinde bir Set tutuyor
bilesim.hepsiniEkle(List.of("a", "b", "c"));
bilesim.denemeSayisi();   // 3
```

**Neler öğrendim:**
- **Abstract class:** Alanı, constructor'ı ve ortak akışı olan bir iskelet. `Hesap` bakiyeyi ve çekim kurallarını tutuyor; alt sınıf sadece `cekimUcreti`'ni dolduruyor. `paraCek` bir **şablon metot**: `final`, adımların sırası sabit, alt sınıf sırayı bozamıyor.
- **Interface:** Durumu olmayan bir yetenek ("faiz getirebilir"). `default` metotla ortak hesap verebiliyor ama alan tutamıyor. Bir sınıf tek bir sınıftan türeyip istediği kadar interface uygulayabiliyor: `VadeliHesap extends Hesap implements FaizGetiren`.
- **`protected`:** Kanca metot alt sınıflara açık, dışarıdaki koda kapalı. Polimorfizm: listede `Hesap` olarak duran iki hesap kendi ücretini uyguluyor.
- **Kırılgan üst sınıf:** `HashSet`'ten türeyen sayaç, `addAll` içeride `add`'i çağırdığı için 3 eleman için 6 sayıyor. Alt sınıf, üst sınıfın dokümante edilmemiş iç ayrıntısına bağımlı oldu; JDK değişirse davranış da değişebilir.
- **Bileşim:** `SayanKume` bir kümeyi içinde tutup işi ona iletiyor. İçteki küme ne yaparsa yapsın sayaç doğru; üstelik `HashSet` yerine `TreeSet` verilebiliyor. Kural: "X bir Y'dir" gerçekten doğruysa kalıtım, "X bir Y kullanır" ise bileşim.
- **Liskov ihlali:** JDK'nın kendi hatası: `Stack extends Vector`, o yüzden `stack.add(0, ...)` yığının dibine eleman sokabiliyor. Yığın gerekiyorsa `ArrayDeque`.

| Dosya | Görevi |
|---|---|
| `Hesap` | Ortak bakiye, şablon metot `paraCek`, `protected abstract` kanca |
| `VadesizHesap` | Ücretsiz çekim |
| `VadeliHesap` | %1 ceza, `FaizGetiren` uyguluyor |
| `FaizGetiren` | `default` metotla aylık faiz |
| `HataliSayanKume` | `HashSet`'ten türeyen sayaç: kırılgan üst sınıf (bilerek hatalı) |
| `SayanKume` | Bileşimle sayaç, iç küme değiştirilebilir |

---

## Gün 30: SiparisHesaplayici (SOLID ve clean code ile refactoring)

Çalışan ama kötü bir metodu adım adım temizledim. Eski kod `eski` paketinde olduğu gibi duruyor; yeni kod `yeni` paketinde. İkisinin aynı sonucu verdiğini test kanıtlıyor.

```java
// Önce: dört iş tek metotta, her şey String ve double
double hesapla(String musteriTipi, List<String[]> urunler, String sehir, String kupon) {
    double t = 0;
    for (String[] u : urunler) {
        if (u[3].equals("ELEKTRONIK")) t += f * a * 1.20; else if ...
    }
    if (musteriTipi.equals("ALTIN")) t = t - t * 0.10; ...
    if (kupon != null) { if (kupon.equals("YENI50")) { if (t >= 500) ...
    if (t < 750) { if (sehir.equals("Istanbul") || ...
    return Math.round(t * 100) / 100.0;
}

// Sonra: adımlar adlarıyla, sonuç bir döküm
public Fiyatlandirma hesapla(Siparis siparis) {
    BigDecimal kdvliToplam = kdvliToplam(siparis);
    BigDecimal musteriIndirimi = kdvliToplam.multiply(siparis.musteriTipi().indirimOrani());
    BigDecimal indirimli = kdvliToplam.subtract(musteriIndirimi);
    BigDecimal kuponIndirimi = kuponIndirimi(siparis, indirimli);
    BigDecimal kuponlu = indirimli.subtract(kuponIndirimi);
    BigDecimal kargo = KargoUcreti.hesapla(kuponlu, siparis.sehir());
    return new Fiyatlandirma(...);
}
```

| Koku | Çözüm |
|---|---|
| Uzun metot | Her adım adlı küçük bir metot; `hesapla` içindekiler sayfası gibi okunuyor |
| İlkel takıntısı (`String[]`, `"ALTIN"`) | `SiparisKalemi` record'u, `Kategori` ve `MusteriTipi` enum'ları |
| Sihirli sayılar (`1.20`, `750`, `29.99`) | Oranlar enum'da, kargo sabitleri `KargoUcreti`'nde adlarıyla |
| İç içe `if` | Erken dönüş, `Set.contains`, `Optional` zinciri |
| `t`, `u`, `f`, `a` | Ne olduğunu söyleyen adlar |
| Yeni kupon için metodu açmak | `Kupon` arayüzü; kuponlar kurucudan bir `Map` olarak geliyor |
| Tek sayı dönüyor | `Fiyatlandirma`: KDV'li toplam, iki indirim, kargo, ödenecek |

**Neler öğrendim:**
- **Önce karakterizasyon testi:** Refactoring "davranışı değiştirmeden yapıyı değiştirmek". Bunu kanıtlamak için önce eski kodun ne yaptığını kilitleyen bir test yazdım: sabit tohumlu (`new Random(42)`) 1.000 rastgele siparişi iki koda da verip sonuçları karşılaştırıyor. Her adımdan sonra çalıştırdım; kırmızıya dönerse son adım hatalı.
- **Bilerek kalan tek fark:** Eski kod `double` ile hesaplayıp en sonda yuvarlıyor, yeni kod `BigDecimal` kullanıyor. Kuruşun yarısında biten tutarlarda 1 kuruş fark çıkabiliyor; test bu yüzden 0,01 toleransla karşılaştırıyor. `0.1 + 0.2 != 0.3`: para `double`'da tutulmaz.
- **İlkel takıntısının bedeli:** Eski koda `"ELEKTRONİK"` (Türkçe İ) gelince eşleşme tutmuyor ve ürün sessizce "diğer" sayılıyor: %20 yerine %10 KDV. Yeni kodda bu yazım derlenmiyor bile; metinden çevrilirken `Kategori.valueOf` hemen hata veriyor.
- **SOLID'den ikisi:** Tek sorumluluk (kargo kuralı `KargoUcreti`'nde, KDV kalemde, indirim müşteri tipinde) ve açık/kapalı (yüzdelik yeni bir kupon tek satır lambda; hesaplayıcıya dokunmadan ekleniyor, testi var).
- **Davranışı koru, sonra değiştir:** Bilinmeyen kupon kodunu sessizce yok saymak belki yanlış, ama eski kod öyle yapıyordu. Refactoring sırasında davranışı değiştirmedim; değiştirmek ayrı bir iş ve ayrı bir commit olmalı.

| Dosya | Görevi |
|---|---|
| `eski/SiparisHesaplayici` | Başlangıç kodu, bilerek kötü; değiştirilmiyor |
| `yeni/SiparisHesaplayici` | Aynı kurallar, küçük metotlar, kuponlar dışarıdan |
| `yeni/Siparis`, `yeni/SiparisKalemi` | Parametre nesnesi ve kalem (`record`) |
| `yeni/Kategori`, `yeni/MusteriTipi` | KDV ve indirim oranları (`enum`) |
| `yeni/Kupon` | Kupon arayüzü, `sabitTutar` fabrikası |
| `yeni/KargoUcreti` | Kargo kuralı ve sabitleri |
| `yeni/Fiyatlandirma` | Sonucun dökümü (`record`) |

---

## Gün 31: Ayarlar ve TembelTekil (Singleton ve doğru yazımları)

"Bir sınıftan tek bir örnek olsun." Basit görünüyor ama dört şekilde bozuluyor: aynı anda gelen thread'ler, yansıma (reflection), serileştirme ve testler arasında taşınan durum. Her yazımı aynı saldırıyla denedim: 8 thread bir `CyclicBarrier`'da bekleyip aynı anda örnek istiyor.

```java
// 1) Önerilen: tek elemanlı enum
public enum Ayarlar { INSTANCE; ... }

// 2) Tembel kurulum gerekiyorsa: holder deyimi
public static TembelTekil ornek() { return Tutucu.ORNEK; }
private static final class Tutucu { private static final TembelTekil ORNEK = new TembelTekil(); }

// 3) Bilerek hatalı: kontrol ile atama arasında başka thread araya giriyor
if (ornek == null) { ornek = new HataliTekil(); }   // 8 thread → birden fazla örnek

// 4) Çift kontrollü kilit: volatile şart
private static volatile CiftKontrolluTekil ornek;
```

| Yazım | 8 thread aynı anda | Yansıma | Serileştirme |
|---|---|---|---|
| `enum` | tek örnek | engelli (`IllegalArgumentException`) | aynı nesne döner |
| Holder deyimi | tek örnek | `setAccessible` ile ikinci örnek kurulabiliyor | ek kod gerekir (`readResolve`) |
| Kontrolsüz `if` | **birden fazla örnek** | kırılır | kırılır |
| Çift kontrollü kilit | tek örnek | kırılır | ek kod gerekir |

**Neler öğrendim:**
- **`enum` neden en iyisi:** JVM sabiti sınıf yüklenirken bir kez ve thread-safe kuruyor. `Constructor.newInstance` enum için açıkça reddediliyor, serileştirme de sabiti adıyla geri buluyor. Sıfır ek kod.
- **Holder deyimi:** İç sınıf ancak `ornek()` ilk çağrıldığında yükleniyor; sınıf yükleme zaten thread-safe olduğu için kilit gerekmiyor. Dış sınıfın başka bir üyesini (`surum()`) kullanmak örneği kurmuyor; testte kurulum sayacıyla kontrol ediyorum.
- **Yarışı görünür kılmak:** Hatalı sürümün kurucusu 50 ms uyuyor (bağlantı açmak gibi). Bariyer 8 thread'i aynı anda bıraktığı için hepsi `null` görüyor; tek çekirdekte bile (`taskset -c 0`) birden fazla örnek çıktı.
- **Neden `volatile`:** Onsuz JVM, nesne tamamen kurulmadan referansı alana yazabiliyor; ikinci thread kilide girmeden yarı kurulmuş nesneyi alabiliyor. Holder deyimi aynı işi daha sade yaptığı için genelde o tercih ediliyor.
- **Asıl sorun küresel durum:** `GlobalSayac` testler arasında sayıyı taşıyor; bir test "SP-1" bekleyemiyor, sonuç önceki testlere bağlı. `SiparisNumaralari` bağımlılığını kurucudan alınca testte `() -> 7` gibi sahte bir üretici verilebiliyor. Spring'deki `@Component` bean'leri zaten varsayılan olarak tek örnek; "tek olsun" kararını sınıfın kendisi değil konteyner veriyor.

| Dosya | Görevi |
|---|---|
| `Ayarlar` | `enum` singleton |
| `TembelTekil` | Holder deyimiyle tembel singleton |
| `HataliTekil` | Kontrolsüz `if`: yarış (bilerek hatalı) |
| `CiftKontrolluTekil` | Çift kontrollü kilit, `volatile` |
| `GlobalSayac` | Küresel durumun sorununu gösteren sayaç |
| `NumaraUretici`, `SiraliNumaraUretici` | Enjekte edilebilir bağımlılık ve gerçek uygulaması |
| `SiparisNumaralari` | Küresel sürüm ile kurucudan bağımlılık alan sürüm yan yana |

---

## Gün 32: KargoAdresi (Optional'ın doğru ve yanlış kullanımı)

"Müşterinin kargo şehri ne?" sorusu dört yerde boş kalabilir: müşteri yok, adresi yok, şehir `null`, şehir boşluk. Aynı soru iki yazımla:

```java
// Önce: iç içe null kontrolleri
Musteri musteri = depo.bulYaDaNull(id);
if (musteri != null) {
    Adres adres = musteri.adresYaDaNull();
    if (adres != null) {
        String sehir = adres.sehir();
        if (sehir != null && !sehir.isBlank()) return sehir;
    }
}
return BILINMIYOR;

// Sonra: Optional zinciri
return depo.bul(id)
        .flatMap(Musteri::adres)        // adres() de Optional döndürüyor: flatMap
        .map(Adres::sehir)              // sehir() düz değer: map (null dönerse boş Optional olur)
        .filter(sehir -> !sehir.isBlank())
        .orElse(BILINMIYOR);
```

**Neler öğrendim:**
- **`map` ile `flatMap`:** Adım düz bir değer döndürüyorsa `map`, `Optional` döndürüyorsa `flatMap`. `map` içinde `null` dönen bir adım zinciri kırmıyor, boş `Optional`'a çeviriyor. Eski ve yeni yazımın beş durumda aynı sonucu verdiğini test ediyorum.
- **`orElse` ile `orElseGet`:** `orElse(varsayilan())` argümanı Optional dolu olsa bile hesaplıyor; varsayılan bir veritabanı sorgusuysa boşuna çalışıyor. `orElseGet(() -> varsayilan())` sadece boşken. Testte bir sayaçla kanıtladım: dolu Optional'da `orElse` 1 kez üretti, `orElseGet` hiç.
- **Bulunamamak hata ise:** `orElseThrow(() -> new MusteriBulunamadi(id))`. Kontrolsüz `get()` aynı işi anlamsız bir `NoSuchElementException` ile yapıyor.
- **`or` ve `stream`:** `onbellek.or(() -> depo.bul(id))` önbellek doluysa depoya hiç gitmiyor. `.flatMap(Optional::stream)` bir id listesinden bulunamayanları atıp bulunanları açıyor.
- **Nerede kullanılmaz:** `Optional` dönüş tipi için. Alan, parametre ya da koleksiyon elemanı olarak kullanılmıyor. `Musteri` alanı düz (null olabilir) tutuyor, dışarıya `Optional<Adres> adres()` veriyor. `Optional` döndüren bir metodun `null` döndürmesi en kötüsü: çağıran `isPresent()` derken `NullPointerException` alıyor. İlkel değer için `OptionalInt`, kutulama yok.

| Dosya | Görevi |
|---|---|
| `Musteri` | Alan düz, erişim `Optional` |
| `Adres` | Şehir ve posta kodu (`record`, kirli veri olabilir) |
| `MusteriDeposu` | `bul` (Optional), eski stil `bulYaDaNull`, bilerek hatalı `hataliBul` |
| `KargoAdresi` | Eski ve yeni yazım, `orElseThrow`, `or`, `stream`, `OptionalInt` |
| `MusteriBulunamadi` | Bulunamama hatası |

---

## Gün 33: KuralMotoru (Hazır fonksiyonel arayüzler)

Kredi başvurusu değerlendirmesi. Kurallar `if` zinciri değil, bir listede duran veriler: adı, koşulu (`Predicate`) ve red sebebi. Motor kuralların ne olduğunu bilmiyor, sırayla deniyor ve bütün sebepleri birden döndürüyor.

```java
Predicate<Basvuru> guvenilir = IYI_KREDI_NOTU.or(YUKSEK_GELIR.and(DENEYIMLI));

KuralMotoru motor = new KuralMotoru()
        .ekle(new Kural("yas", b -> b.yas() >= 18, "18 yaşından küçükler başvuramaz"))
        .ekle(new Kural("guven", guvenilir, "Kredi notu düşük ve gelir/deneyim yetersiz"))
        .dinle(karar -> denetimKaydi.add(karar));

motor.degerlendir(basvuru);
// Karar[basvuran=Ayşe, onay=false, sebepler=[18 yaşından küçükler..., Kredi notu düşük...]]
```

| Arayüz | Ne alır, ne döner | Bu günde |
|---|---|---|
| `Predicate<T>` | `T` → `boolean` | Kurallar; `and`, `or`, `negate`, `Predicate.not` |
| `Function<T, R>` | `T` → `R` | Başvurudan ham puan; `andThen`, `compose` |
| `UnaryOperator<T>` | `T` → `T` | Puana sırayla uygulanan düzeltmeler |
| `BiFunction<T, U, R>` | `T`, `U` → `R` | Puan ve vadeden faiz oranı |
| `Consumer<T>` | `T` → (yok) | Karar dinleyicileri; `andThen` ile birden fazla |

**Neler öğrendim:**
- **Kural bir veri:** Yeni kural motoru değiştirmeden `ekle` ile, kaldırmak `cikar("ad")` ile. Kurallar `LinkedHashMap`'te olduğu için sebepler hep aynı sırada çıkıyor.
- **İlk hatada durmamak:** Kullanıcıya "önce adını yaz", düzeltince "yaşın tutmuyor" demek yerine bütün sebepler tek seferde.
- **`and` kısa devre yapıyor:** `RESIT.and(krediBurosundaTemiz)`: ilk koşul tutmazsa ikinci (dış servise giden) hiç çalışmıyor. Ucuz kontrol önce yazılıyor; testte sayaçla kanıtladım.
- **`andThen` ile `compose` sırası:** `f.andThen(g)` önce `f`, `f.compose(g)` önce `g`. `×2` ve `+10` ile 10'dan 30 ve 40 çıkıyor.
- **Düzeltmeleri zincirlemek:** Bir `UnaryOperator` listesi `reduce` ile tek fonksiyona katlanıyor; boş listede `UnaryOperator.identity()` sayıyı olduğu gibi bırakıyor.

| Dosya | Görevi |
|---|---|
| `Basvuru` | Başvuru verisi (`record`) |
| `Kural` | Ad, koşul, red sebebi (`record`) |
| `Karar` | Onay ve sebepler (`record`) |
| `KuralMotoru` | Kural ekleme/çıkarma, değerlendirme, `Consumer` dinleyiciler |
| `KrediKurallari` | Hazır koşullar, standart kural seti, puan fonksiyonları, faiz tablosu |

---

## Gün 34: TransferServisi (ReentrantLock ve deadlock)

İki hesap arası transfer iki kilit istiyor. A→B ve B→A aynı anda gelirse, kilitleri parametre sırasıyla alan kod kilitlenip kalıyor (deadlock). Bunu önce bilerek yaşattım, sonra iki yolla önledim.

```text
Thread 1: A→B   A'yı aldı ──────► B'yi bekliyor ─┐
Thread 2: B→A   B'yi aldı ──────► A'yı bekliyor ─┘  ikisi de sonsuza kadar
```

```java
// Çözüm 1: kilitleri her zaman aynı sırayla al (küçük id önce)
Hesap ilk = kaynak.id() < hedef.id() ? kaynak : hedef;
Hesap ikinci = ilk == kaynak ? hedef : kaynak;
ilk.kilit().lock();
try {
    ikinci.kilit().lock();
    try {
        kaynak.dus(tutar);
        hedef.ekle(tutar);
    } finally {
        ikinci.kilit().unlock();
    }
} finally {
    ilk.kilit().unlock();
}

// Çözüm 2: süreli dene, alamazsan vazgeç ve sonra tekrar dene
if (!kaynak.kilit().tryLock(200, TimeUnit.MILLISECONDS)) return false;
```

**Neler öğrendim:**
- **Deadlock'u test etmek:** Hatalı serviste iki thread ilk kilidi aldıktan sonra bir `CyclicBarrier`'da buluşuyor, sonra ikinci kilide uzanıyor; deadlock garanti. `ThreadMXBean.findDeadlockedThreads()` kilitlenen iki thread'i buluyor (`synchronized` yanında `ReentrantLock`'u da görüyor). Kilitler `lockInterruptibly` ile alındığı için test sonunda thread'ler `interrupt` ile kurtarılıyor; test asılı kalmıyor.
- **Kilit sırası:** Bütün transferler kilitleri aynı sırayla alırsa döngüsel bekleme oluşamıyor. 8 thread ile 10.000 karşılıklı transfer bitiyor; 10 hesap arasında 16.000 rastgele transfer denemesinden sonra toplam para kuruşu kuruşuna aynı ve hiçbir hesap eksiye düşmüyor.
- **`tryLock`:** Kilit başkasındayken 200 ms bekleyip `false` dönüyor. Ters sırayla kilit isteyen iki thread bile vazgeç-tekrar dene döngüsüyle sonunda bitiriyor.
- **Hep `finally`:** `unlock` `finally`'de; yetersiz bakiye istisnasından sonra iki kilidin de boş olduğunu test ediyorum. Önce düşülüyor, sonra ekleniyor: düşme başarısız olursa hiçbir şey değişmemiş oluyor.
- **`ReentrantLock` neden "reentrant":** Kilidi tutan thread aynı kilidi tekrar alabiliyor (`getHoldCount` 2); `bakiye()` de kilit aldığı için bu şart.
- **`Condition`:** `bekleVeCek` bakiye yetene kadar kilidi bırakıp uyuyor, para yatınca `signalAll` ile uyanıp tekrar bakıyor. Uyanmak koşulun sağlandığı anlamına gelmediği için kontrol `if` değil `while`.

| Dosya | Görevi |
|---|---|
| `Hesap` | Kuruş cinsinden bakiye, `ReentrantLock`, `Condition` ile bekleyen çekim |
| `TransferServisi` | Sıralı kilitle transfer, `tryLock` ile süreli deneme |
| `HataliTransferServisi` | Parametre sırasıyla kilit: deadlock (bilerek hatalı) |
| `YetersizBakiye` | Bakiye yetmediğinde hata |

---

## Gün 35: Kutuphane (Mini proje: kütüphane yönetimi)

Önceki günlerin konularını bir arada kullanan küçük bir uygulama: ödünç alma, iade, gecikme cezası, raporlar, dosyaya kaydetme ve konsol komutları.

| Kural | Değer |
|---|---|
| Ödünç süresi | 14 gün |
| Üye başına | en fazla 3 kitap |
| Uzatma | bir kez, 7 gün; gecikmişse olmaz |
| Gecikmiş kitabı olan | yeni kitap alamaz |
| Ceza | günde 5 TL, en fazla 100 TL |

```java
KomutYorumlayici konsol = new KomutYorumlayici(kutuphane);
konsol.calistir("ver 111 U1");     // Verildi, son teslim 2026-11-16
konsol.calistir("ver 111 U2");     // Hata: 'Tutunamayanlar' şu an başka bir üyede
// 16 gün sonra
konsol.calistir("geciken");        // Gecikenler: [111 U1]
konsol.calistir("iade 111");       // İade alındı, ceza 10 TL
```

**Neler öğrendim:**
- **Zaman dışarıdan:** `Kutuphane` "bugün"ü `Clock`'tan alıyor. Testte saati günlerce ileri sarıp ceza, uzatma ve gecikme kurallarını saniyesinde deniyorum (Gün 9'daki ders).
- **Değiştirilemez kayıt:** `Odunc` bir `record`; iade ve uzatma yeni bir kayıt üretiyor (`iadeEdildi`, `uzatildi`). Kayıt geçmişi bozulmuyor.
- **Raporlar Stream ile:** En çok okunan yazarlar `groupingBy` + `counting`, sonra sayıya göre azalan ve eşitlikte ada göre sıralama. Gecikenler en çok geciken önce.
- **Dosya biçimi:** Her satırın başında türünü söyleyen bir etiket (`K;...`, `O;...`). `split(";", -1)`: `-1` olmazsa sondaki boş alan (henüz iade yok) atılıyor ve satır kısa kalıyor. Ayırıcı `;` kitap adında olursa dosya bozulacağı için `Kitap` bunu kurarken reddediyor. Kaydedip geri yükleyince her şeyin aynı olduğunu `@TempDir` ile test ediyorum.
- **Konsol test edilebilir:** `Scanner` döngüsü sadece satır okuyup `calistir`'a veriyor; asıl iş bir `String` alıp `String` döndüren metotta. Hatalar istisna olarak değil, kullanıcıya gösterilecek mesaj olarak dönüyor.

| Dosya | Görevi |
|---|---|
| `Kitap` | ISBN, ad, yazar (`record`, ayırıcı kontrolü) |
| `Odunc` | Ödünç kaydı, gecikme günü (`record`) |
| `Kutuphane` | Kurallar, ceza, uzatma, raporlar |
| `KutuphaneDosyasi` | Metin dosyasına kaydetme ve yükleme |
| `KomutYorumlayici` | `ver`, `iade`, `geciken` komutları |
| `KutuphaneHatasi` | Kural ihlali hatası |

---

## Gün 36: LogAnalizi (Büyük log dosyasını okumak)

Bir erişim logundan rapor: kaç istek, kaç bozuk satır, saat saat sunucu hataları, en yavaş uç noktalar.

```text
2026-11-12T10:05:00.000Z ERROR [http-3] POST /api/contact 503 1200ms
java.net.SocketTimeoutException: Read timed out          ← bozuk satır, sayılıp atlanıyor
2026-11-12T10:30:00.000Z INFO  [http-1] GET /api/posts/7?sayfa=2 200 60ms
```

```java
Rapor r = LogAnalizi.analizEt(Path.of("erisim.log"), 3);
r.kayit();            // 7
r.bozukSatir();       // 3
r.saatlikHatalar();   // {2026-11-12T10:00=1, 2026-11-12T11:00=2}
r.enYavaslar();       // [POST /api/contact=1200, GET /api/posts/{id}=300]   (p95, ms)
r.durumSiniflari();   // {2xx=3, 4xx=1, 5xx=3}
```

**Neler öğrendim:**
- **Tembel okuma:** `Files.lines` dosyayı satır satır okuyan bir akış; bellekte bir anda tek satır var. `Files.readAllLines` 2 GB'lık bir logda belleği bitirir. Akış bir dosya tuttuğu için try-with-resources ile kapatılıyor. İlk sunucu hatasını arayan `findFirst`, 50.003 satırlık dosyada 3. satırda buldu ve kalanını hiç okumadı (`peek` sayacıyla test).
- **Regex adlı grupları:** `(?<durum>\d{3})` ve `m.group("durum")`; sütunların yeri değişse kod okunur kalıyor. Desen `static final`: her satırda yeniden derlenmiyor. Uymayan satır istisna değil, boş `Optional`.
- **Yolu normalleştirmek:** `/api/posts/12` ve `/api/posts/7?sayfa=2` aynı uç nokta: sorgu dizesi atılıyor, sayı olan parçalar `{id}` oluyor. Yoksa 200.000 farklı yol 200.000 ayrı satır olurdu.
- **Ortalama değil p95:** Ortalama, tek bir çok yavaş isteği binlerce hızlı isteğin içinde gizliyor. p95 "isteklerin %95'i bu süreden kısa" demek; "en yakın sıra" yöntemiyle sıralı listenin ⌈0,95·n⌉. elemanı.
- **Sıralı gruplama:** `groupingBy(..., TreeMap::new, counting())` saatleri zamana göre sıralı veriyor; `truncatedTo(ChronoUnit.HOURS)` saat başına yuvarlıyor.

| Dosya | Görevi |
|---|---|
| `LogKaydi` | Ayrıştırılmış satır (`record`) |
| `LogAyristirici` | Regex ile ayrıştırma, yol normalleştirme |
| `LogAnalizi` | Tembel okuma, rapor, ilk hata, yüzdelik |
| `Rapor` | Toplamlar, saatlik hatalar, en yavaşlar, durum sınıfları (`record`) |
| `LogOkumaHatasi` | Okuma hatası, asıl sebebi `cause`'da |

---

## Gün 37: JetonKovasi (Hız sınırlama algoritmaları)

"Bir IP dakikada en fazla 5 istek atabilsin." Üç yolla yazdım ve aynı senaryoda karşılaştırdım.

| Algoritma | Nasıl | Bellek (anahtar başına) | Zayıf yanı |
|---|---|---|---|
| Sabit pencere | Her dakikaya bir sayaç | 2 sayı | Dakika sınırında limitin 2 katı geçebiliyor |
| Kayan pencere | Son 60 saniyedeki istek zamanları | limit kadar zaman damgası | Bellek |
| Jeton kovası | Kova dolduruluyor, her istek bir jeton | 2 sayı | Kısa patlamaya (kapasite kadar) izin veriyor; çoğu zaman istenen de bu |

```java
JetonKovasi kova = new JetonKovasi(Clock.systemUTC(), 10, 2.0);   // en fazla 10 jeton, saniyede 2 dolum

if (!kova.izinVer(istemciIp)) {
    long bekle = kova.beklemeMs(istemciIp);   // 429 yanıtında Retry-After
}
```

**Neler öğrendim:**
- **Sabit pencerenin açığı:** 10:00:59.9'da 5 istek, 10:01:00.1'de 5 istek daha: ikisi farklı dakikaya düştüğü için 200 ms'de 10 istek geçiyor. Aynı senaryoda kayan pencere ikinci beşi reddediyor; testte ikisini yan yana koydum.
- **Tembel dolum:** Jeton kovası için zamanlayıcı yok. İstek geldiğinde son istekten bu yana geçen süre × dolum hızı kadar jeton ekleniyor. Binlerce IP için binlerce zamanlayıcı gerekmiyor. Jeton sayısı `double`: 250 ms'de yarım jeton birikiyor, yetmiyor; 500 ms'de bir jeton.
- **Atomik güncelleme:** "Oku, karar ver, yaz" adımları arasına başka bir thread girerse limit aşılır (Gün 13'teki yarış). `ConcurrentHashMap.compute` anahtar başına atomik; 16 thread 2.000 istek atınca tam 100 izin çıkıyor. Kayan pencerede kuyruk değiştirilebilir bir nesne olduğu için anahtar başına `synchronized`.
- **Zamana bağlı kod ve test:** Hepsi `Clock` alıyor; testte saati milisaniye milisaniye ilerletiyorum. `Thread.sleep` ile yazılmış bir test hem yavaş hem güvenilmez olurdu.

| Dosya | Görevi |
|---|---|
| `HizSinirlayici` | Ortak arayüz |
| `SabitPencere` | Dakika başına sayaç |
| `KayanPencere` | Son pencerenin zaman damgaları |
| `JetonKovasi` | Tembel dolumlu kova, bekleme süresi |

---

## Gün 38: SeyahatPlanlayici (CompletableFuture ile birleştirme)

Gün 13'te `CompletableFuture`'ın temelleri vardı; bu gün birden fazla dış servisi birleştirmek. Bir seyahat paketi için uçuş, otel ve döviz kuru gerekiyor. Uçuş ile otel birbirinden bağımsız, kur ise otelin para birimine bağlı.

```text
havayolları (aynı anda) ─► en ucuz uçuş ─────────────────┐
otel ─► (para birimi belli olunca) kur ─► otelin TL fiyatı ┴─► paket
```

```java
CompletableFuture<Ucus> ucus = enUcuzUcus(nereden, nereye);
CompletableFuture<OtelFiyati> otel = oteller.ara(nereye)
        .thenCompose(o -> kurlar.tlKarsiligi(o.paraBirimi())                    // bağımlı: sıralı
                .thenApply(kur -> new OtelFiyati(o, o.geceFiyati().multiply(kur).multiply(BigDecimal.valueOf(gece)))));
return ucus.thenCombine(otel, (u, of) -> new Paket(...));                       // bağımsız: paralel
```

| Metot | Ne yapar |
|---|---|
| `thenApply` | Sonucu dönüştürür |
| `thenCompose` | Sonuçla yeni bir asenkron çağrı başlatır, iç içe future'ı düzleştirir |
| `thenCombine` | İki bağımsız sonucu birleştirir |
| `allOf` / `anyOf` | Hepsini / ilk geleni bekler |
| `handle` / `exceptionally` | Hatayı değere çevirir |
| `orTimeout` / `completeOnTimeout` | Süre dolunca hata / yedek değer |

**Neler öğrendim:**
- **Paralel ve sıralı:** Her servis 400 ms sürüyorsa paket 800 ms'de çıkıyor (uçuş ve otel aynı anda, kur otelden sonra); hepsi sırayla olsa 1.200 ms. Testte süreyi ölçüyorum.
- **Kısmi sonuç:** Havayollarından biri 503 verse de arama çökmüyor: her cevaba `handle` ile "hata varsa `null`" deniyor, `allOf` bitince `null`'lar atılıp en ucuzu seçiliyor. Hepsi çökerse anlamlı bir `SeyahatBulunamadi`.
- **Zaman aşımı:** `orTimeout` süre dolunca `TimeoutException` ile bitiriyor; `completeOnTimeout` hata yerine son bilinen kuru veriyor.
- **Hata zincirde atlıyor:** Hata olunca aradaki `thenApply` adımları hiç çalışmıyor, ilk `exceptionally`'ye kadar atlanıyor.
- **`join` ile `get`:** Aynı hatayı `join` `CompletionException` (unchecked), `get` `ExecutionException` (checked) içine sarıyor; asıl hata ikisinde de `getCause()`.
- **Hangi thread:** `thenApplyAsync(fn, havuz)` işi verilen havuzda çalıştırıyor. Havuz verilmezse ortak `ForkJoinPool`; bloklayan dış çağrılar için ayrı havuz daha doğru. Sahte servisler `delayedExecutor` ile gecikiyor; beklerken thread tutmuyor.

| Dosya | Görevi |
|---|---|
| `SeyahatPlanlayici` | Paket, en ucuz uçuş, ilk cevap, zaman aşımı, hata kurtarma |
| `UcusServisi`, `OtelServisi`, `KurServisi` | Dış servis arayüzleri, `CompletableFuture` döndürüyor |
| `Ucus`, `Otel`, `Paket` | Veriler (`record`) |
| `SeyahatBulunamadi` | Hiç uçuş yoksa hata |

---

## Gün 39: Para (Kuruş kaybetmeden hesap)

100 TL'yi üç kişiye bölünce 33,33 × 3 = 99,99 çıkıyor; bir kuruş kayboluyor. Faturada, taksitte ve masraf paylaşımında bu kuruşlar birikiyor. `Para` sınıfı tutar ve birimi birlikte taşıyor ve hiçbir işlemde kuruş kaybetmiyor.

```java
Para.tl("100").dagit(3);                         // [33.34, 33.33, 33.33]
Para.tl("1000").oranlaDagit(List.of(50, 30, 20)); // [500.00, 300.00, 200.00]
Para.tl("99.99").kdvAyir(20);                    // [83.33, 16.66]: toplamı yine 99.99
Para.tl("10").topla(Para.of("10", "EUR"));        // FarkliParaBirimi: önce kurla çevir
Para.tl("12.345");                                // IllegalArgumentException: en fazla 2 ondalık
```

**Neler öğrendim:**
- **`BigDecimal.equals` tuzağı:** `10.5` ile `10.50` için `equals` false (ölçek farklı), `compareTo` 0. `Para` tutarı her zaman birimin kuruş hanesine çekiyor (`TRY` 2, `JPY` 0; `Currency.getDefaultFractionDigits()`), böylece `equals` doğru çalışıyor. Ölçeklemede `RoundingMode.UNNECESSARY`: yuvarlama gerekiyorsa sessizce kuruş atmak yerine hata.
- **Kuruşla hesapla:** Paylaştırma tam sayı kuruş üzerinden: taban pay `floorDiv`, artan kuruşlar baştakilere birer birer. Altı farklı tutarda (eksi tutar dahil) toplamın aynı kaldığını ve payların en fazla 1 kuruş farklı olduğunu test ediyorum.
- **En büyük kalan yöntemi:** Oranlı paylaştırmada herkes payının aşağı yuvarlanmışını alıyor, artan kuruşlar kesirli kısmı en büyük olanlara gidiyor.
- **`HALF_EVEN` (bankacı yuvarlaması):** `HALF_UP` tam yarıları hep yukarı yuvarlıyor; 1.000 tane x,xx5 değerde toplam 5 TL fazla çıkıyor. `HALF_EVEN` yarıyı çift olana yuvarlıyor; hatalar birbirini götürüyor ve toplam hata 0.
- **KDV'yi farktan hesapla:** Net tutar yuvarlanarak bulunuyor, KDV ise fiyat eksi net. İkisi ayrı ayrı yuvarlansaydı toplamları fiyattan bir kuruş sapabilirdi.
- **Biçim yerele göre:** Türkçede `1.234,50`: binlik ayırıcı nokta, ondalık virgül. `NumberFormat.getCurrencyInstance(tr-TR)`; elle `String.format` yazmaya gerek yok.

| Dosya | Görevi |
|---|---|
| `Para` | Tutar ve birim, toplama, çarpma, eşit ve oranlı paylaştırma, KDV ayırma, biçim |
| `FarkliParaBirimi` | Farklı birimler birlikte kullanılınca hata |

---

## Gün 40: IsGunuTakvimi ve ToplantiPlanlayici (İş günleri ve saat dilimleri)

Gün 9'da `LocalDateTime` vardı; bu gün iki zor yer: tatilleri bilen iş günü hesabı ve farklı şehirler arası saat.

```java
IsGunuTakvimi takvim = new IsGunuTakvimi(tatiller);   // tatiller dışarıdan: dini bayramlar her yıl değişiyor
takvim.isGunuEkle(LocalDate.of(2026, 10, 28), 1);     // 2026-11-02 (29 Ekim tatil, 30 köprü, hafta sonu)
LocalDate.of(2026, 10, 1).with(takvim.ayinSonIsGunu());   // maaş günü
takvim.tahminiTeslim(LocalDateTime.parse("2026-11-13T15:00"), 2);   // kesimden sonra: pazartesi işlenir, çarşamba teslim

Instant an = ToplantiPlanlayici.an(LocalDateTime.of(2026, 11, 13, 14, 0), ISTANBUL);
ToplantiPlanlayici.herkesIcin(an, List.of(ISTANBUL, NEW_YORK, LONDRA));
// {America/New_York=06:00, Europe/Istanbul=14:00, Europe/London=11:00}
```

**Neler öğrendim:**
- **`TemporalAdjuster`:** "Sonraki iş günü" ve "ayın son iş günü" kendi ayarlayıcılarım; `tarih.with(...)` ile kullanılıyor, JDK'nın `TemporalAdjusters.lastDayOfMonth()` ve `firstInMonth(MONDAY)` ayarlayıcıları gibi.
- **An ile yerel saat farkı:** "14:00" tek başına bir an değil, "14:00 İstanbul" bir an. Kural: `Instant` (UTC) saklanır, yerel saat sadece gösterirken hesaplanır. Aynı anın İstanbul ve Londra hâlleri `equals` ile eşit değil (bölge farklı), `isEqual` ile eşit.
- **Yaz saati her şeyi değiştiriyor:** Türkiye yaz saati uygulamıyor (hep +03:00) ama New York 1 Kasım 2026'da geri aldı. İstanbul 14:00 Ekim'de New York'ta 07:00, Kasım'da 06:00; aradaki fark 7 saatten 8 saate çıktı.
- **Boşluk ve çakışma:** 8 Mart 2026'da New York'ta saat 02:00'den 03:00'e atlıyor; 02:30 diye bir an yok, `atZone` onu 03:30'a kaydırıyor. 1 Kasım'da 01:30 iki kez yaşanıyor; `withEarlierOffsetAtOverlap` ve `withLaterOffsetAtOverlap` arasında tam 1 saat var.
- **`Period` ile `Duration`:** Yaz saatine geçilen gün `plus(Period.ofDays(1))` "yarın aynı saat" (aslında 23 saat sonra), `plus(Duration.ofHours(24))` tam 24 saat sonra (saat 13:00). Hatırlatıcı "yarın 12'de" ise `Period`, oturum süresi ise `Duration`.
- **Ay sonu:** 31 Ocak + 1 ay = 28 Şubat (artık yılda 29); `plusMonths` olmayan günü ayın son gününe çekiyor.

| Dosya | Görevi |
|---|---|
| `IsGunuTakvimi` | İş günü kontrolü, ekleme, sayma, ayarlayıcılar, kesim saatli teslim tahmini |
| `ToplantiPlanlayici` | Yerel saatten an, anın her bölgedeki karşılığı, boşluktaki saati yerleştirme |

---

## Gün 41: SlugUretici ve MetinOlcer (Unicode ve Türkçe metin)

Blog yazısının başlığından URL'deki adı üretmek basit görünüyor, ama Türkçe harfler, aksanlar ve emojiler işin içine girince Java'daki "karakter" kavramının ne kadar karışık olduğu çıkıyor.

```java
SlugUretici.slug("Java'da Çoklu İş Parçacığı ve Senkronizasyon!", 80);
// javada-coklu-is-parcacigi-ve-senkronizasyon
SlugUretici.slug("Café & Crème Brûlée", 80);        // cafe-creme-brulee
SlugUretici.slug("Merhaba 👋 Dünya 🌍", 80);         // merhaba-dunya
SlugUretici.benzersiz("java-notlari", alinmislar);  // java-notlari-2

"👍🏽".length();                       // 4  (UTF-16 birimi)
"👍🏽".codePointCount(0, 4);            // 2  (başparmak + ten rengi)
MetinOlcer.grafemSayisi("👍🏽");         // 1  (ekranda görünen)
MetinOlcer.utf8Bayt("🌍");             // 4
```

**Neler öğrendim:**
- **Slug sırası:** Önce Türkçe harfler elle çevriliyor, çünkü `ı` ayrıştırılınca `i`'ye dönmüyor, başka bir harf. Sonra `Normalizer.normalize(s, NFD)` harfi ve aksanı ayırıyor (`é` → `e` + `´`) ve `\p{M}` aksanları atıyor. Küçük harf `Locale.ROOT` ile, harf-rakam dışı her şey tireye, uzun slug kelime sınırında kesiliyor.
- **Dört farklı uzunluk:** `length()` UTF-16 birimi sayıyor; emoji 2 birim. Ekranda tek görünen 👨‍👩‍👧 aslında görünmez birleştiricilerle (ZWJ) bağlanmış 5 kod noktası. Kullanıcının gördüğü birim "grafem"; `BreakIterator.getCharacterInstance()` sayıyor. Veritabanı sütun sınırı ise çoğu zaman bayt: `ğ` 2, `€` 3, emoji 4 bayt.
- **Kısaltırken bölme:** `substring(0, 8)` "Merhaba👋" metninde emojinin yarısında kesiyor; ekranda `�` çıkıyor. Grafem sınırında kesen `kisalt` bunu yapmıyor.
- **Aynı görünen farklı metin:** `é` tek kod noktası da olabilir, `e` + birleşen aksan da. Ekranda aynı, `equals` false. Karşılaştırmadan (ve kaydetmeden) önce `Normalizer.Form.NFC`.
- **Türkçe harf tuzakları:** `"İSTANBUL".toLowerCase(Locale.ROOT)` noktalı `i̇` üretiyor (8 harf, 9 karakter); `"title".toUpperCase(tr)` `TİTLE` veriyor, teknik anahtarlarda (enum adı, HTTP başlığı) hata. `"sinir".equalsIgnoreCase("sınır")` true: farklı kelimeler eşit sayılıyor. Kural: kullanıcı metni için `tr-TR`, teknik değerler için `Locale.ROOT`.
- **Rakam tuzağı:** `Integer.parseInt("٣")` (Arapça-Hint rakamı) 3 döndürüyor ve `Character.isDigit` true diyor, ama regex `\d` varsayılan olarak sadece 0-9. Kimlik ya da telefon doğrularken `[0-9]` açıkça yazılmalı.

| Dosya | Görevi |
|---|---|
| `SlugUretici` | Başlıktan slug, kelime sınırında kesme, benzersiz slug |
| `MetinOlcer` | Grafem sayısı, UTF-8 bayt, emojiyi bölmeden kısaltma (ve bilerek hatalı hâli) |

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
│   ├── gun14/   TopluSmsGonderici, Bildirim, SmsServisi, GonderimRaporu
│   ├── gun15/   Depo, OlayYayini, Abonelik, StokOlayi
│   ├── gun16/   HavaDurumuServisi, YabanciApiAdaptoru, OnbellekliServis, TekrarDeneyenServis, HavaDurumuServisleri
│   ├── gun17/   TcKimlikNo, Iban, Telefon
│   ├── gun18/   Sonuc, UyeKaydi, KayitFormu, Uye
│   ├── gun19/   Dogrulayici, Zorunlu, Uzunluk, Aralik, Desen, Ihlal, UyelikFormu
│   ├── gun20/   Kapsayici, Enjekte, KapsayiciHatasi, SiparisServisi, SiparisDeposu, BellekSiparisDeposu, BildirimServisi, Siparis
│   ├── gun21/   MetroAgi, Baglanti, Rota
│   ├── gun22/   LruOnbellek, KisaLruOnbellek
│   ├── gun23/   NotDefteriSunucusu, Not, Json
│   ├── gun24/   OtomatikTamamlama, Oneri
│   ├── gun25/   JsonAyristirici, JsonYazici, JsonDegeri, JsonHatasi
│   ├── gun26/   Siparis, SiparisDurumu, DurumDegisimi, GecersizGecisHatasi, SiparisPaneli
│   ├── gun27/   BloomFiltresi
│   ├── gun28/   Sepet, SepetIslemleri, HataliKampanya, Kampanya, Ozyineleme
│   ├── gun29/   Hesap, VadesizHesap, VadeliHesap, FaizGetiren, HataliSayanKume, SayanKume
│   ├── gun30/   eski/SiparisHesaplayici, yeni/(SiparisHesaplayici, Siparis, SiparisKalemi, Kategori, MusteriTipi, Kupon, KargoUcreti, Fiyatlandirma)
│   ├── gun31/   Ayarlar, TembelTekil, HataliTekil, CiftKontrolluTekil, GlobalSayac, NumaraUretici, SiraliNumaraUretici, SiparisNumaralari
│   ├── gun32/   Musteri, Adres, MusteriDeposu, KargoAdresi, MusteriBulunamadi
│   ├── gun33/   Basvuru, Kural, Karar, KuralMotoru, KrediKurallari
│   ├── gun34/   Hesap, TransferServisi, HataliTransferServisi, YetersizBakiye
│   ├── gun35/   Kitap, Odunc, Kutuphane, KutuphaneDosyasi, KomutYorumlayici, KutuphaneHatasi
│   ├── gun36/   LogKaydi, LogAyristirici, LogAnalizi, Rapor, LogOkumaHatasi
│   ├── gun37/   HizSinirlayici, SabitPencere, KayanPencere, JetonKovasi
│   ├── gun38/   SeyahatPlanlayici, UcusServisi, OtelServisi, KurServisi, Ucus, Otel, Paket, SeyahatBulunamadi
│   ├── gun39/   Para, FarkliParaBirimi
│   ├── gun40/   IsGunuTakvimi, ToplantiPlanlayici
│   └── gun41/   SlugUretici, MetinOlcer
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
    ├── gun14/   TopluSmsGondericiTest
    ├── gun15/   DepoTest
    ├── gun16/   HavaDurumuServisiTest, AyarlanabilirSaat
    ├── gun17/   DogrulayicilarTest
    ├── gun18/   UyeKaydiTest
    ├── gun19/   DogrulayiciTest
    ├── gun20/   KapsayiciTest
    ├── gun21/   MetroAgiTest
    ├── gun22/   LruOnbellekTest
    ├── gun23/   NotDefteriSunucusuTest
    ├── gun24/   OtomatikTamamlamaTest
    ├── gun25/   JsonAyristiriciTest
    ├── gun26/   SiparisDurumuTest, AyarlanabilirSaat
    ├── gun27/   BloomFiltresiTest
    ├── gun28/   BellekTest
    ├── gun29/   KalitimVeBilesimTest
    ├── gun30/   RefactoringTest
    ├── gun31/   TekilTest
    ├── gun32/   OptionalTest
    ├── gun33/   KuralMotoruTest
    ├── gun34/   TransferTest
    ├── gun35/   KutuphaneTest, AyarlanabilirSaat
    ├── gun36/   LogAnaliziTest
    ├── gun37/   HizSinirlayiciTest, AyarlanabilirSaat
    ├── gun38/   SeyahatPlanlayiciTest
    ├── gun39/   ParaTest
    ├── gun40/   TarihVeSaatDilimiTest
    └── gun41/   UnicodeTest
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
