package br.com.zup.edu.raceconditions.services;

import br.com.zup.edu.raceconditions.model.EventRepository;
import br.com.zup.edu.raceconditions.model.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BulkInsertAndSerializableNewTicketService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Transactional(
        isolation = Isolation.SERIALIZABLE
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
