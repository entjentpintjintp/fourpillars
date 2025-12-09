package com.kolloseum.fourpillars.application.service;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;

public interface RateLimitService {

    Bucket resolveBucket(String key);

    boolean tryConsume(String key);
}
