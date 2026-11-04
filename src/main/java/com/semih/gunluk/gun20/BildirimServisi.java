package com.semih.gunluk.gun20;

import java.util.ArrayList;
import java.util.List;

/** Gönderilen bildirimleri sadece listede tutuyor (gerçekte e-posta). */
public class BildirimServisi {

    private final List<String> gonderilenler = new ArrayList<>();

    public void gonder(String mesaj) {
        gonderilenler.add(mesaj);
    }

    public List<String> gonderilenler() {
        return List.copyOf(gonderilenler);
    }
}
