package br.com.zup.edu.raceconditions.services;

import base.SpringBootIntegrationTest;
import br.com.zup.edu.raceconditions.model.Event;
import br.com.zup.edu.raceconditions.model.EventRepository;
import br.com.zup.edu.raceconditions.model.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SerializableIsolationLevelNewTicketServiceTest extends SpringBootIntegrationTest {

	@Autowired
	private SerializableIsolationLevelNewTicketService serializableIsolationLevelNewTicketService;

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

	/**
	 * Caused by: org.postgresql.util.PSQLException: ERROR: could not serialize access due to read/write dependencies among transactions
	 *   Detail: Reason code: Canceled on identification as a pivot, during commit attempt.
	 *   Hint: The transaction might succeed if retried.
	 */
	@Test
	void shouldBuyNoMoreThanMaxTickets() throws InterruptedException {

		assertEquals(5,
				eventRepository.getMaxTickets(EVENT.getId()));

		doSyncAndConcurrently(10, customerName -> {
			serializableIsolationLevelNewTicketService.buyNewTicket(EVENT.getId(), customerName);
		});

		Long ticketCount = ticketRepository.countByEvent(EVENT);
		LOGGER.info("[?] ticketCount = {}", ticketCount);
		assertThat(ticketCount)
				.isLessThanOrEqualTo(5);
	}

}
