package com.kolloseum.fourpillars.infrastructure.redis;

import com.kolloseum.fourpillars.application.service.RateLimitService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitServiceImpl implements RateLimitService {

    private final ProxyManager<byte[]> proxyManager;

    @Override
    public Bucket resolveBucket(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return proxyManager.builder()
                .build(keyBytes, this::getBucketConfiguration);
    }

    @Override
    public boolean tryConsume(String key) {
        Bucket bucket = resolveBucket(key);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            log.debug("Rate limit token consumed: key={}, remaining={}", key, probe.getRemainingTokens());
            return true;
        } else {
            log.warn("Rate limit exceeded: key={}, waitFor={}ns", key, probe.getNanosToWaitForRefill());
            return false;
        }
    }

    private BucketConfiguration getBucketConfiguration() {
        return BucketConfiguration.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(30)
                        .refillGreedy(30, Duration.ofMinutes(1))
                        .build())
                .build();
    }
}
