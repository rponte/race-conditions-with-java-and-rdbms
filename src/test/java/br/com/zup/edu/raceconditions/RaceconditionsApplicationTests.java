package br.com.zup.edu.raceconditions;

import br.com.zup.edu.raceconditions.services.noob.Event;
import br.com.zup.edu.raceconditions.services.noob.EventRepository;
import br.com.zup.edu.raceconditions.services.noob.SimpleNewTicketService;
import br.com.zup.edu.raceconditions.services.noob.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class RaceconditionsApplicationTests {

	private static final int THREAD_COUNT = 10;
	private static final Logger LOGGER = LoggerFactory.getLogger(RaceconditionsApplicationTests.class);

	@Autowired
	private SimpleNewTicketService service;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private TicketRepository ticketRepository;

	private Event EVENT;

	@BeforeEach
	public void setUp() {
		eventRepository.deleteAll();
		this.EVENT = eventRepository.save(new Event("ZupCon", 5));
	}

	@Test
	void shouldBuyNoMoreThanMaxTickets() throws InterruptedException {

		assertEquals(5, eventRepository.getMaxTickets(EVENT.getId()));

		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch endLatch = new CountDownLatch(THREAD_COUNT);

		for (int i = 0; i < THREAD_COUNT; i++) {
			String customerName = "Thread-" + i;
			new Thread(() -> {
				try {
					startLatch.await();
					service.buyNewTicket(EVENT.getId(), customerName);
				} catch (Exception e) {
					LOGGER.error("error while buying a new ticket for " + customerName, e);
				} finally {
					endLatch.countDown();
				}
			}).start();
		}
		startLatch.countDown();
		endLatch.await();

		assertEquals(5, ticketRepository.count());
	}

}
