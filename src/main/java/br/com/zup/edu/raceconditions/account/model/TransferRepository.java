package br.com.zup.edu.raceconditions.account.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = """
                   insert into transfer
                            (from_account_id, to_account_id, amount, created_at)
                         values
                            (:fromAccountId, :toAccountId, :amount, now())
                   """
    )
    public void insertNewTransfer(Long fromAccountId, Long toAccountId, BigDecimal amount);
}
