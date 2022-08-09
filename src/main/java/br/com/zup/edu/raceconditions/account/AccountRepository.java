package br.com.zup.edu.raceconditions.account;

import br.com.zup.edu.raceconditions.tickets.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Transactional
    @Query("""
           select c.balance
             from Account c
            where c.id = :accountId
           """)
    public BigDecimal getBalance(Long accountId);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Account c where c.id = :accountId")
    public Optional<Account> findByIdWithPessimisticLocking(Long accountId);


    @Transactional
    @Modifying
    @Query(nativeQuery = true,
           value = """
                   update account
                      set balance = balance - :amount
                    where id = :accountId
                      and (balance - :amount) >= 0
                   """
    )
    public int debitAndValidateBalance(Long accountId, BigDecimal amount);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = """
                   update account
                      set balance = balance + :amount
                    where id = :accountId
                   """
    )
    public int creditBalance(Long accountId, BigDecimal amount);

}
