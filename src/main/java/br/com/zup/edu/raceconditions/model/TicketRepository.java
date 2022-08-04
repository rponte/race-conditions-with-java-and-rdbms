package br.com.zup.edu.raceconditions.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Transactional
    public Long countByEvent(Event event);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
           value = """
                   insert into ticket
                            (event_id, customer_name, created_at)
                         values
                            (:eventId, :customerName, now())
                   """
    )
    public void insertNewTicket(Long eventId, String customerName);
}
