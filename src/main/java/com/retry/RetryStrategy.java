package com.retry;

public interface RetryStrategy {
    long nextDelay(int attempt) throws Exception;
}
