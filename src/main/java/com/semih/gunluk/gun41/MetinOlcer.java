package com.semih.gunluk.gun41;

import java.nio.charset.StandardCharsets;
import java.text.BreakIterator;

/**
 * Bir metnin "uzunluğu" dört farklı şey olabilir:
 * <ul>
 *   <li>{@code length()}: UTF-16 birimi ({@code char}) sayısı. Emoji gibi BMP dışı karakterler 2 birim.</li>
 *   <li>{@code codePointCount}: Unicode karakter (kod noktası) sayısı.</li>
 *   <li>Grafem: kullanıcının ekranda "bir harf" gördüğü birim; 👍🏽 iki kod noktası, tek grafem.</li>
 *   <li>UTF-8 bayt: veritabanı ve ağ sınırları bununla ölçülür.</li>
 * </ul>
 */
public final class MetinOlcer {

    private MetinOlcer() {
    }

    public static int grafemSayisi(String metin) {
        BreakIterator it = BreakIterator.getCharacterInstance();
        it.setText(metin);
        int sayi = 0;
        while (it.next() != BreakIterator.DONE) {
            sayi++;
        }
        return sayi;
    }

    public static int utf8Bayt(String metin) {
        return metin.getBytes(StandardCharsets.UTF_8).length;
    }

    /** <b>Bilerek hatalı:</b> {@code substring} bir emojinin ortasından kesebilir. */
    public static String hataliKisalt(String metin, int enFazla) {
        return metin.length() <= enFazla ? metin : metin.substring(0, enFazla) + "…";
    }

    /** En fazla {@code enFazla} grafem; görünen karakterlerin hiçbiri yarım kalmıyor. */
    public static String kisalt(String metin, int enFazla) {
        BreakIterator it = BreakIterator.getCharacterInstance();
        it.setText(metin);
        int son = 0;
        for (int i = 0; i < enFazla; i++) {
            int sonraki = it.next();
            if (sonraki == BreakIterator.DONE) {
                return metin;
            }
            son = sonraki;
        }
        return it.next() == BreakIterator.DONE ? metin : metin.substring(0, son) + "…";
    }
}
