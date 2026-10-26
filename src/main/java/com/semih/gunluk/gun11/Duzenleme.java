package com.semih.gunluk.gun11;

/**
 * Metin üzerinde yapılmış tek bir değişiklik. Her düzenleme kendini uygulamayı ve kendi
 * tersini üretmeyi biliyor: geri almak, tersini uygulamak demek.
 */
public sealed interface Duzenleme permits Duzenleme.Ekleme, Duzenleme.Silme {

    void uygula(StringBuilder metin);

    Duzenleme tersi();

    /** {@code konum}a {@code yazi} eklendi. Tersi: aynı yeri silmek. */
    record Ekleme(int konum, String yazi) implements Duzenleme {

        @Override
        public void uygula(StringBuilder metin) {
            metin.insert(konum, yazi);
        }

        @Override
        public Duzenleme tersi() {
            return new Silme(konum, yazi);
        }
    }

    /**
     * {@code konum}dan başlayan {@code yazi} silindi. Silinen yazı saklanıyor, çünkü geri
     * alırken aynısını yerine koymak gerekiyor.
     */
    record Silme(int konum, String yazi) implements Duzenleme {

        @Override
        public void uygula(StringBuilder metin) {
            metin.delete(konum, konum + yazi.length());
        }

        @Override
        public Duzenleme tersi() {
            return new Ekleme(konum, yazi);
        }
    }
}
