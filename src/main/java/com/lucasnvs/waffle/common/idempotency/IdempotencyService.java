package com.lucasnvs.waffle.common.idempotency;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class IdempotencyService {
    private final StringRedisTemplate redis;

    public IdempotencyService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public boolean tryLock(Long raffleId, Integer number) {
        String key = buildKey(raffleId, number);

        Boolean success = redis.opsForValue().setIfAbsent(key, "LOCKED", Duration.ofMinutes(5));

        return Boolean.TRUE.equals(success);
    }

    private String buildKey(Long raffleId, Integer number) {
        return "raffle:" + raffleId + ":ticket:" + number + ":lock";
    }
}

