package com.semih.gunluk.gun26;

import java.time.Instant;

/** Geçmişteki tek bir adım: nereden nereye, ne zaman, neden. */
public record DurumDegisimi(SiparisDurumu onceki, SiparisDurumu yeni, Instant zaman, String aciklama) {
}
