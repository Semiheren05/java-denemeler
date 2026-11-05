package com.semih.gunluk.gun25;

import java.util.Map;
import java.util.stream.Collectors;

/** Ağacı yeniden JSON metnine çevirir (boşluksuz). Ayrıştırıcının tersi. */
public final class JsonYazici {

    private JsonYazici() {
    }

    public static String yaz(JsonDegeri deger) {
        return switch (deger) {
            case JsonDegeri.Nesne(Map<String, JsonDegeri> alanlar) -> alanlar.entrySet().stream()
                    .map(alan -> metin(alan.getKey()) + ":" + yaz(alan.getValue()))
                    .collect(Collectors.joining(",", "{", "}"));
            case JsonDegeri.Dizi dizi -> dizi.elemanlar().stream()
                    .map(JsonYazici::yaz)
                    .collect(Collectors.joining(",", "[", "]"));
            case JsonDegeri.Metin(String s) -> metin(s);
            case JsonDegeri.Sayi(var sayi) -> sayi.toString();
            case JsonDegeri.Mantiksal(boolean b) -> String.valueOf(b);
            case JsonDegeri.Bos bos -> "null";
        };
    }

    private static String metin(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> sb.append(c < 0x20 ? String.format("\\u%04x", (int) c) : String.valueOf(c));
            }
        }
        return sb.append('"').toString();
    }
}
