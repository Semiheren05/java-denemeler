package com.semih.gunluk.gun23;

/**
 * Sadece metin üretmek için minik bir yardımcı. Metni olduğu gibi tırnak içine koymak tehlikeli:
 * içinde {@code "} varsa JSON bozulur, hatta araya sahte alanlar eklenebilir. Bu yüzden özel
 * karakterler kaçış dizisine çevriliyor.
 */
final class Json {

    private Json() {
    }

    static String metin(String deger) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : deger.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));   // diğer kontrol karakterleri
                    } else {
                        sb.append(c);   // Türkçe harfler olduğu gibi; gövde UTF-8
                    }
                }
            }
        }
        return sb.append('"').toString();
    }

    static String hata(String mesaj) {
        return "{\"hata\":" + metin(mesaj) + "}";
    }
}
