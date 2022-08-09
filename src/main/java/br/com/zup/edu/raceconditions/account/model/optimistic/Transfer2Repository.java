package br.com.zup.edu.raceconditions.account.model.optimistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Transfer2Repository extends JpaRepository<Transfer2, Long> {

}
