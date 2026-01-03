package com.lucasnvs.waffle.common.ratelimit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {

    private static final int LIMIT = 10;
    private static final Duration WINDOW = Duration.ofMinutes(1);

    private final StringRedisTemplate redis;

    public RateLimitService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public boolean allowRequest(String key) {
        Long count = redis.opsForValue().increment(key);

        if (count != null && count == 1) {
            redis.expire(key, WINDOW);
        }

        return count != null && count <= LIMIT;
    }
}

