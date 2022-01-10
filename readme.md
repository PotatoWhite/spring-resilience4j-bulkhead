# 종류
## semaphoreBulkhead
- 세마포어 계수기를 통해 동시성을 제공한다.
- 계수기 -> lock -> signal

```yaml
resilience4j:
  bulkhead:
    instances:
      sampleBulkhead:
        maxConcurrentCalls: 3
        maxWaitDuration: 1s
```

## threadBulkhead
- 쓰레드풀을 통해 동시성을 제공한다.
```yaml
resilience4j:
  thread-pool-bulkhead:
    instances:
      sampleThreadPoolBulkhead:
        maxThreadPoolSize: 3
        coreThreadPoolSize: 2
        queueCapacity: 1

```