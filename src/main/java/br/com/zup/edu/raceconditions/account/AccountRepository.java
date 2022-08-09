package br.com.zup.edu.raceconditions.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Transactional
    @Query("""
           select c.balance
             from Account c
            where c.id = :accountId
           """)
    public BigDecimal getBalance(Long accountId);

}
