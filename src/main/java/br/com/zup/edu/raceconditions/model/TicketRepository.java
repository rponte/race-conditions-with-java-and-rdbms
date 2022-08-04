package br.com.zup.edu.raceconditions.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Transactional
    public Long countByEvent(Event event);

}
