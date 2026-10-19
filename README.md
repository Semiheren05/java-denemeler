# java-denemeler

[![Testler](https://github.com/Semiheren05/java-denemeler/actions/workflows/ci.yml/badge.svg)](https://github.com/Semiheren05/java-denemeler/actions/workflows/ci.yml)

Her gün bir Java alıştırması. Her günün kodu kendi paketinde, testleriyle birlikte duruyor; GitHub Actions her push'ta tüm testleri çalıştırıyor.

**Teknolojiler:** Java 21 · Maven · JUnit 5 · GitHub Actions

## Yapı

```
src/main/java/com/semih/gunluk/gunNN   o günün kodu
src/test/java/com/semih/gunluk/gunNN   o günün testleri
```

## Çalıştırma

```bash
mvn test
```

## Günler

| Gün | Konu | Klasör |
|---|---|---|
| 01 | `BankaHesabi`: para yatırma/çekme, transfer, yetersiz bakiye için özel exception, işlem geçmişi; para için `BigDecimal` | [`gun01`](src/main/java/com/semih/gunluk/gun01) |
| 02 | `KelimeSayaci`: `Map` ile kelime sayma, Türkçe büyük/küçük harf, noktalama ve kesme işareti temizliği, en sık N kelime | [`gun02`](src/main/java/com/semih/gunluk/gun02) |

## Lisans

[MIT](LICENSE) © 2026 Semih Eren
