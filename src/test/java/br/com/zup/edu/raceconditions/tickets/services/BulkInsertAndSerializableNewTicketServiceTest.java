package br.com.zup.edu.raceconditions.tickets.services;

import base.SpringBootIntegrationTest;
import br.com.zup.edu.raceconditions.tickets.model.Event;
import br.com.zup.edu.raceconditions.tickets.model.EventRepository;
import br.com.zup.edu.raceconditions.tickets.model.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BulkInsertAndSerializableNewTicketServiceTest extends SpringBootIntegrationTest {

	@Autowired
	private BulkInsertAndSerializableNewTicketService bulkInsertAndSerializableNewTicketService;

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
	void shouldBuyNoMoreThanMaxTickets() throws InterruptedException {

		assertEquals(5,
				eventRepository.getMaxTickets(EVENT.getId()));

		doSyncAndConcurrently(10, customerName -> {
			bulkInsertAndSerializableNewTicketService.buyNewTicket(EVENT.getId(), customerName);
		});

		Long ticketCount = ticketRepository.countByEvent(EVENT);
		LOGGER.info("[?] ticketCount = {}", ticketCount);
		assertThat(ticketCount)
				.isLessThanOrEqualTo(5);
	}

}
