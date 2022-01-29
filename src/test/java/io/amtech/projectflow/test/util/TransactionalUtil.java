package io.amtech.projectflow.test.util;

import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

public class TransactionalUtil {
    @Transactional
    public <T> T txRun(Supplier<T> supplier) {
        return supplier.get();
    }

    @Transactional
    public void txRun(Runnable runnable) {
        runnable.run();
    }
}
