package br.com.zup.edu.raceconditions.model.optimistic;

import br.com.zup.edu.raceconditions.model.optimistic.Event2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface Event2Repository extends JpaRepository<Event2, Long> {

    @Transactional
    @Query("""
           select e.maxTickets 
             from Event2 e
            where e.id = :eventId
           """)
    public Integer getMaxTickets(Long eventId);

    @Transactional
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("select e from Event2 e where e.id = :eventId")
    public Optional<Event2> findByIdWithPessimisticLocking(Long eventId);

}
