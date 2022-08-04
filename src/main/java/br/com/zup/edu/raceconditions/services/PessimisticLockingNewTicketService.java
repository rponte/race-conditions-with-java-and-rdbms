package br.com.zup.edu.raceconditions.services;

import br.com.zup.edu.raceconditions.model.Event;
import br.com.zup.edu.raceconditions.model.EventRepository;
import br.com.zup.edu.raceconditions.model.Ticket;
import br.com.zup.edu.raceconditions.model.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PessimisticLockingNewTicketService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Transactional
    public void buyNewTicket(Long eventId, String customerName) {

        Event event = eventRepository.findByIdWithPessimisticLocking(eventId).orElseThrow(() -> {
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
