package com.semih.gunluk.gun16;

/**
 * Dışarıdan aldığımız (değiştiremediğimiz) bir kütüphane. API'si bizimkine hiç benzemiyor:
 * şehri plaka koduyla istiyor, sıcaklığı Fahrenheit ve metin olarak veriyor ("72.5F").
 */
public interface YabanciHavaApi {

    String currentTemperature(int cityCode);
}
