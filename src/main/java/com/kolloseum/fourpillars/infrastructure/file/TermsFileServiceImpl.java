package com.kolloseum.fourpillars.infrastructure.file;

import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import com.kolloseum.fourpillars.domain.service.TermsFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Slf4j
@Service
public class TermsFileServiceImpl implements TermsFileService {

    @Value("${app.terms.file.path:classpath:terms}")
    private String termsBasePath;

    @Override
    public Optional<String> getTermsContent(TermsType termsType, String version) {
        try {
            String fileName = buildFileName(termsType, version);
            String fullPath = String.format("terms/%s/%s",
                    termsType.name().toLowerCase(),
                    fileName);

            ClassPathResource resource = new ClassPathResource(fullPath);

            if (!resource.exists()) {
                log.warn("Terms file not found: {}", fullPath);
                return Optional.empty();
            }

            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            log.debug("Successfully loaded terms file: {}", fullPath);
            return Optional.of(content);

        } catch (IOException e) {
            log.error("Failed to read terms file: {} v{}", termsType, version, e);
            return Optional.empty();
        }

    }

    @Override
    public String calculateFileHash(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(content.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            log.error("Hash calculation failed", e);
            throw new RuntimeException("Error occurred during hash calculation", e);
        }
    }

    @Override
    public boolean validateTermsIntegrity(TermsType termsType, String version,
            String backupContent, String expectedHash) {
        Optional<String> fileContent = getTermsContent(termsType, version);

        if (fileContent.isEmpty()) {
            log.warn("Integrity validation failed: file not found {} v{}", termsType, version);
            return false;
        }

        // 1. re파일 내용과 백업 내용 비교
        boolean contentMatches = fileContent.get().equals(backupContent);

        // 2. 파일 해시 검증
        String actualHash = calculateFileHash(fileContent.get());
        boolean hashMatches = actualHash.equals(expectedHash);

        if (!contentMatches) {
            log.error("Integrity validation failed: content mismatch {} v{}", termsType, version);
        }
        if (!hashMatches) {
            log.error("Integrity validation failed: hash mismatch {} v{} (expected: {}, actual: {})",
                    termsType, version, expectedHash, actualHash);
        }

        return contentMatches && hashMatches;
    }

    private String buildFileName(TermsType termsType, String version) {
        // ✅ 업로더 방식에 맞춤: privacy-terms-v1.0.0.md
        return String.format("%s-terms-%s.md",
                termsType.name().toLowerCase(), // privacy, service
                version); // v1.0.0
    }
}