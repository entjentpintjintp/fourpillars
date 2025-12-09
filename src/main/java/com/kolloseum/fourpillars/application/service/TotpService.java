package com.kolloseum.fourpillars.application.service;

public interface TotpService {
    String generateSecret();

    String getQrCodeUrl(String secret, String account);

    boolean verify(String secret, int code);
}
