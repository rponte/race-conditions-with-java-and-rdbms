package br.com.zup.edu.raceconditions.services.noob;

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

        Ticket newTicket = new Ticket(event, customerName);
        ticketRepository.save(newTicket);
    }
}
