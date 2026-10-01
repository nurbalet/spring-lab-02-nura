package kz.iitu.springlab02.aspect;

import kz.iitu.springlab02.audit.Measured;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

@Aspect
@Component
@Order(4)
public class MetricsAspect {

    private static final Logger log = LoggerFactory.getLogger(MetricsAspect.class);

    public static class Stats {
        final LongAdder count = new LongAdder();
        final LongAdder totalNanos = new LongAdder();
        public long count() { return count.sum(); }
        public long avgMs() {
            long c = count.sum();
            return c == 0 ? 0 : (totalNanos.sum() / c) / 1_000_000;
        }
    }

    private final Map<String, Stats> metrics = new ConcurrentHashMap<>();

    @Around("@annotation(measured)")
    public Object measure(ProceedingJoinPoint pjp, Measured measured) throws Throwable {
        long start = System.nanoTime();
        try {
            return pjp.proceed();
        } finally {
            long took = System.nanoTime() - start;
            String name = pjp.getSignature().toShortString();
            Stats s = metrics.computeIfAbsent(name, k -> new Stats());
            s.count.increment();
            s.totalNanos.add(took);
            log.info("[METRIC] {} — {} ms (calls: {})", name, took / 1_000_000, s.count());
        }
    }

    public Map<String, Map<String, Long>> snapshot() {
        Map<String, Map<String, Long>> out = new ConcurrentHashMap<>();
        metrics.forEach((k, v) -> out.put(k, Map.of(
                "count", v.count(),
                "avgMs", v.avgMs()
        )));
        return out;
    }
}