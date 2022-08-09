package br.com.zup.edu.raceconditions.account.services;

import br.com.zup.edu.raceconditions.account.model.optimistic.Account2;
import br.com.zup.edu.raceconditions.account.model.optimistic.Account2Repository;
import br.com.zup.edu.raceconditions.account.model.optimistic.Transfer2;
import br.com.zup.edu.raceconditions.account.model.optimistic.Transfer2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class OptimisticLockingTransferService {

    @Autowired
    private Account2Repository account2Repository;
    @Autowired
    private Transfer2Repository transfer2Repository;

    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {

        Account2 fromAccount = account2Repository.findById(fromAccountId).orElseThrow(() -> {
            throw new IllegalStateException("from-account does not exist: " + fromAccountId);
        });

        Account2 toAccount = account2Repository.findById(toAccountId).orElseThrow(() -> {
            throw new IllegalStateException("to-account does not exist: " + toAccountId);
        });

        Transfer2 transfer = fromAccount.transferTo(toAccount, amount);
        transfer2Repository.save(transfer);
    }

}
