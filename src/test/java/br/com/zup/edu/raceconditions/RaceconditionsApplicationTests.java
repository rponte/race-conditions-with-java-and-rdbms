package br.com.zup.edu.raceconditions;

import br.com.zup.edu.raceconditions.model.Event;
import br.com.zup.edu.raceconditions.model.EventRepository;
import br.com.zup.edu.raceconditions.model.TicketRepository;
import br.com.zup.edu.raceconditions.services.PessimisticLockingNewTicketService;
import br.com.zup.edu.raceconditions.services.SimpleNewTicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class RaceconditionsApplicationTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaceconditionsApplicationTests.class);

	@Autowired
	private SimpleNewTicketService simpleNewTicketService;
	@Autowired
	private PessimisticLockingNewTicketService pessimisticLockingNewTicketService;

	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private TicketRepository ticketRepository;

	private Event EVENT;

	@BeforeEach
	public void setUp() {
		ticketRepository.deleteAll();
		eventRepository.deleteAll();
		this.EVENT = eventRepository.save(new Event("ZupCon", 5));
	}

	@Test
	void shouldBuyNoMoreThanMaxTickets_usingSimpleNewTicketService() throws InterruptedException {

		assertEquals(5,
				eventRepository.getMaxTickets(EVENT.getId()));

		doSyncAndConcurrently(10, customerName -> {
			simpleNewTicketService.buyNewTicket(EVENT.getId(), customerName);
		});

		assertEquals(5, ticketRepository.countByEvent(EVENT));
	}

	@Test
	void shouldBuyNoMoreThanMaxTickets_usingPessimisticLockingNewTicketService() throws InterruptedException {

		assertEquals(5,
				eventRepository.getMaxTickets(EVENT.getId()));

		doSyncAndConcurrently(10, customerName -> {
			pessimisticLockingNewTicketService.buyNewTicket(EVENT.getId(), customerName);
		});

		assertEquals(5, ticketRepository.countByEvent(EVENT));
	}

	private void doSyncAndConcurrently(int threadCount, Consumer<String> buyNewTicketOperation) throws InterruptedException {

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
