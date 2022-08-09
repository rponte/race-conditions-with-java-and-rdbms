package br.com.zup.edu.raceconditions.tickets.services;

import br.com.zup.edu.raceconditions.tickets.model.EventRepository;
import br.com.zup.edu.raceconditions.tickets.model.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BulkInsertAndRepeatableReadWithoutUpdatingEventNewTicketService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TicketRepository ticketRepository;

    /**
     * https://gist.github.com/rponte/618fdc1b10350b6551ea8f7c8ddf83d3
     */
    @Transactional(
        isolation = Isolation.REPEATABLE_READ
    )
    public void buyNewTicket(Long eventId, String customerName) {

        Optional<Integer> event = eventRepository.findEventByIdIfTheresRemainingTickets(eventId);
        if (event.isEmpty()) {
            throw new IllegalStateException("there's no more tickets to sell");
        };

        ticketRepository.insertNewTicket(
                eventId,
                customerName
        );
    }
}
