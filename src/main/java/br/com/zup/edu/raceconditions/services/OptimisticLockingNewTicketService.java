package br.com.zup.edu.raceconditions.services;

import br.com.zup.edu.raceconditions.model.Ticket;
import br.com.zup.edu.raceconditions.model.optimistic.Event2;
import br.com.zup.edu.raceconditions.model.optimistic.Event2Repository;
import br.com.zup.edu.raceconditions.model.optimistic.Ticket2;
import br.com.zup.edu.raceconditions.model.optimistic.Ticket2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OptimisticLockingNewTicketService {

    @Autowired
    private Event2Repository eventRepository;
    @Autowired
    private Ticket2Repository ticketRepository;

    @Transactional
    public void buyNewTicket(Long eventId, String customerName) {

        Event2 event = eventRepository.findById(eventId).orElseThrow(() -> {
           return new IllegalStateException("event not found");
        });

        Long ticketCount = ticketRepository.countByEvent(event);
        if (ticketCount >= event.getMaxTickets()) {
            throw new IllegalStateException("there's no more tickets to sell");
        }

        Ticket2 newTicket = new Ticket2(event, customerName);
        ticketRepository.save(newTicket);
    }
}
