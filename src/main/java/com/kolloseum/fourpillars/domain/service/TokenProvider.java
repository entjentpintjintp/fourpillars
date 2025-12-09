package com.kolloseum.fourpillars.domain.service;

import com.kolloseum.fourpillars.domain.model.vo.TokenPayload;

public interface TokenProvider {
    
    String generateToken(TokenPayload payload);
    
    TokenPayload parseToken(String token);
    
    void blacklistToken(String token);
}