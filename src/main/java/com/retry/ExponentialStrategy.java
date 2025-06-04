package com.retry;

public class ExponentialStrategy implements RetryStrategy {

    private final double a;
    private final long maxDelay;

    public ExponentialStrategy(double a, long maxDelay) {
        if (a <= 0) throw new IllegalArgumentException("Основание должно быть положительным");
        if (maxDelay < 0) throw new IllegalArgumentException("Максимальная задержка не може быть отрицательной");

        this.a = a;
        this.maxDelay = maxDelay;
    }

    public ExponentialStrategy() {
        this(Math.E, Long.MAX_VALUE);
    }

    @Override
    public long nextDelay(int attemptCount) throws Exception {
        if (attemptCount < 0) {
            throw new IllegalArgumentException("Кол-во попыток должно быть положительным");
        }
        double rawDelay = Math.pow(a, attemptCount);
        long delay = (long) rawDelay;

        return Math.min(delay, maxDelay);
    }
}
