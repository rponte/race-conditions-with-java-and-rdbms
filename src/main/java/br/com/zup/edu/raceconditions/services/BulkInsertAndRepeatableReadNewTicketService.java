package br.com.zup.edu.raceconditions.services;

import br.com.zup.edu.raceconditions.model.EventRepository;
import br.com.zup.edu.raceconditions.model.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BulkInsertAndRepeatableReadNewTicketService {

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

        int updatedRows = eventRepository.updateEventIfTheresRemainingTickets(eventId);
        if (updatedRows == 0) {
            throw new IllegalStateException("there's no more tickets to sell");
        };

        ticketRepository.insertNewTicket(
                eventId,
                customerName
        );
    }
}
