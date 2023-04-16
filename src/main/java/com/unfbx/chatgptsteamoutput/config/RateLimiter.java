package com.unfbx.chatgptsteamoutput.config;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimiter {
    private long lastRequestTime = System.currentTimeMillis();
    private long interval;
    private int maxRequests;
    private int tokens;

    public RateLimiter(long interval, int maxRequests) {
        this.interval = interval;
        this.maxRequests = maxRequests;
        this.tokens = maxRequests;
    }

    public synchronized boolean allowRequest() {
        long currentTime = System.currentTimeMillis();
        tokens += (currentTime - lastRequestTime) / interval;
        if (tokens > maxRequests) {
            tokens = maxRequests;
        }
        if (tokens > 0) {
            tokens--;
            lastRequestTime = currentTime;
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        RateLimiter rateLimiter = new RateLimiter(1000, 1); // 每秒最多处理10个请求
        for (int i = 0; i < 200; i++) {
            Random random = new Random();
            int Random = random.nextInt(1000);
            if (i==50){
                Thread.sleep(3000);
            }
            Thread.sleep(Random);
            if (rateLimiter.allowRequest()) {
                System.out.println("处理请求" + i);

            } else {
                System.out.println("请求被限流" + i);
            }
        }
    }
}
