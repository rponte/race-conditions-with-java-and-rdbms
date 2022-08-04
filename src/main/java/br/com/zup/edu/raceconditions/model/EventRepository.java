package br.com.zup.edu.raceconditions.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Transactional
    @Query("""
           select e.maxTickets 
             from Event e
            where e.id = :eventId
           """)
    public Integer getMaxTickets(Long eventId);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Event e where e.id = :eventId")
    public Optional<Event> findByIdWithPessimisticLocking(Long eventId);

}
