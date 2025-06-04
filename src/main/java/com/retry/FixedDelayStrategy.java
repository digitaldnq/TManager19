package com.retry;

public class FixedDelayStrategy implements RetryStrategy {

    private final long delay;

    public FixedDelayStrategy(long delay) {
        this.delay = delay;
    }

    @Override
    public long nextDelay(int attemptCount) throws Exception {
        return delay;
    }
}
