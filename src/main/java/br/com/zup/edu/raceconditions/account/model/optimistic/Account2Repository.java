package br.com.zup.edu.raceconditions.account.model.optimistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface Account2Repository extends JpaRepository<Account2, Long> {

    @Transactional
    @Query("""
           select c.balance
             from Account2 c
            where c.id = :accountId
           """)
    public BigDecimal getBalance(Long accountId);

    @Transactional
    @Query("select c from Account2 c where c.id = :accountId")
    public Optional<Account2> findByIdWithOptimisticLocking(Long accountId);

}
