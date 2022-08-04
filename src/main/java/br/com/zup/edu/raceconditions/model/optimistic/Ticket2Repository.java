package br.com.zup.edu.raceconditions.model.optimistic;

import br.com.zup.edu.raceconditions.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface Ticket2Repository extends JpaRepository<Ticket2, Long> {

    @Transactional
    public Long countByEvent(Event2 event);
}
