package br.com.zup.edu.raceconditions.services;

import br.com.zup.edu.raceconditions.model.Event;
import br.com.zup.edu.raceconditions.model.EventRepository;
import br.com.zup.edu.raceconditions.model.Ticket;
import br.com.zup.edu.raceconditions.model.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SerializableIsolationLevelNewTicketService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Transactional(
        isolation = Isolation.SERIALIZABLE
    )
    public void buyNewTicket(Long eventId, String customerName) {

        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
           return new IllegalStateException("event not found");
        });

        Long ticketCount = ticketRepository.countByEvent(event);
        if (ticketCount >= event.getMaxTickets()) {
            throw new IllegalStateException("there's no more tickets to sell");
        }

        Ticket newTicket = new Ticket(event, customerName);
        ticketRepository.save(newTicket);
    }
}
