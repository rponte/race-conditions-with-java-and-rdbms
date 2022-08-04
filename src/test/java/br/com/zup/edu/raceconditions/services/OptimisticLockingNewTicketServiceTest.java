package br.com.zup.edu.raceconditions.services;

import base.SpringBootIntegrationTest;
import br.com.zup.edu.raceconditions.model.optimistic.Event2;
import br.com.zup.edu.raceconditions.model.optimistic.Event2Repository;
import br.com.zup.edu.raceconditions.model.optimistic.Ticket2Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OptimisticLockingNewTicketServiceTest extends SpringBootIntegrationTest {

	@Autowired
	private OptimisticLockingNewTicketService optimisticLockingNewTicketService;

	@Autowired
	private Event2Repository event2Repository;
	@Autowired
	private Ticket2Repository ticket2Repository;

	private Event2 EVENT;

	@BeforeEach
	public void setUp() {
		ticket2Repository.deleteAll();
		event2Repository.deleteAll();
		this.EVENT = event2Repository.save(new Event2("ZupCon", 5));
	}

	@Test
	void shouldBuyNoMoreThanMaxTickets_usingPessimisticLockingNewTicketService() throws InterruptedException {

		assertEquals(5,
				event2Repository.getMaxTickets(EVENT.getId()));

		doSyncAndConcurrently(10, customerName -> {
			optimisticLockingNewTicketService.buyNewTicket(EVENT.getId(), customerName);
		});

		Long ticketCount = ticket2Repository.countByEvent(EVENT);
		LOGGER.info("[?] ticketCount = {}", ticketCount);
		assertThat(ticketCount)
				.isLessThanOrEqualTo(5);
	}

}
