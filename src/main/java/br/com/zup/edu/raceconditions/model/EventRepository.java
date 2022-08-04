package br.com.zup.edu.raceconditions.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
           select e.maxTickets 
             from Event e
            where e.id = :eventId
           """)
    public Integer getMaxTickets(Long eventId);
}
