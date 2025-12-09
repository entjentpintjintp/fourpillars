package com.kolloseum.fourpillars.application.service.impl;

import com.kolloseum.fourpillars.application.service.TotpService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TotpServiceImpl implements TotpService {

    private final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

    @Override
    public String generateSecret() {
        final GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        return key.getKey();
    }

    @Override
    public String getQrCodeUrl(String secret, String account) {
        return "otpauth://totp/FourPillars:" + account + "?secret=" + secret + "&issuer=FourPillars";
    }

    @Override
    public boolean verify(String secret, int code) {
        return googleAuthenticator.authorize(secret, code);
    }
}
