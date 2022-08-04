package br.com.zup.edu.raceconditions.services;

import base.SpringBootIntegrationTest;
import br.com.zup.edu.raceconditions.model.Event;
import br.com.zup.edu.raceconditions.model.EventRepository;
import br.com.zup.edu.raceconditions.model.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleNewTicketServiceTest extends SpringBootIntegrationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNewTicketServiceTest.class);

	@Autowired
	private SimpleNewTicketService simpleNewTicketService;

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
			simpleNewTicketService.buyNewTicket(EVENT.getId(), customerName);
		});

		assertEquals(5, ticketRepository.countByEvent(EVENT));
	}

}
