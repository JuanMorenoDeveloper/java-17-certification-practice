package ve.com.proitcsolution;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class Ch13Concurrency {
    @Test
    void whenCompareAndSet_thenGetExpectedResult(){
        var atomicInteger = new AtomicInteger(0);

        atomicInteger.compareAndSet(0, 1);

        assertThat(atomicInteger.get()).isEqualTo(1);
    }
}
