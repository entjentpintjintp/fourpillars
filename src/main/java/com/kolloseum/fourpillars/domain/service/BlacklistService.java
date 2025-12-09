package com.kolloseum.fourpillars.domain.service;

public interface BlacklistService {
    void addToBlacklist(String token, long expirationTimeInSeconds);
    boolean isBlacklisted(String token);
    void removeFromBlacklist(String token);
}