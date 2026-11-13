package com.semih.gunluk.gun39;

public class FarkliParaBirimi extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FarkliParaBirimi(String birinci, String ikinci) {
        super(birinci + " ile " + ikinci + " doğrudan toplanamaz; önce kurla çevir");
    }
}
