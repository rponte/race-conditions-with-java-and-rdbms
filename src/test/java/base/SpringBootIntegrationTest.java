package base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

@SpringBootTest
@ActiveProfiles("test")
public abstract class SpringBootIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootIntegrationTest.class);

    /**
     * Starts many threads concurrently to execute the <code>buyNewTicketOperation</code> at the same time.
     * This method only returns after all threads have been executed.
     */
    protected void doSyncAndConcurrently(int threadCount, Consumer<String> buyNewTicketOperation) throws InterruptedException {

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {

            String customerName = "Thread-" + i;

            new Thread(() -> {
                try {
                    startLatch.await();
                    buyNewTicketOperation.accept(customerName);
                } catch (Exception e) {
                    LOGGER.error("error while buying a new ticket for {}: {}", customerName, e.getMessage());
                } finally {
                    endLatch.countDown();
                }
            }).start();
        }

        startLatch.countDown();
        endLatch.await();
    }
}
