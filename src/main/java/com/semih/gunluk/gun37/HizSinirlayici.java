package com.semih.gunluk.gun37;

/** Anahtar genelde istemcinin IP'si ya da kullanıcı id'si: sınır anahtar başına ayrı sayılıyor. */
public interface HizSinirlayici {

    boolean izinVer(String anahtar);
}
