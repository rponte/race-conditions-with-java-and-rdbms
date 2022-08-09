package br.com.zup.edu.raceconditions.tickets.services;

import br.com.zup.edu.raceconditions.tickets.model.Event;
import br.com.zup.edu.raceconditions.tickets.model.EventRepository;
import br.com.zup.edu.raceconditions.tickets.model.Ticket;
import br.com.zup.edu.raceconditions.tickets.model.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SimpleNewTicketService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Transactional
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