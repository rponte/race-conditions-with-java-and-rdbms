package br.com.zup.edu.raceconditions.account.services;

import br.com.zup.edu.raceconditions.account.model.AccountRepository;
import br.com.zup.edu.raceconditions.account.model.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class AtomicUpdateUsingWhereClauseTransferService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransferRepository transferRepository;

    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {

        int updatedRows = accountRepository.debitAndValidateBalance(fromAccountId, amount);
        if (updatedRows == 0) {
            throw new IllegalStateException("there's not enough balance");
        };

        updatedRows = accountRepository.creditBalance(toAccountId, amount);
        if (updatedRows == 0) {
            throw new IllegalStateException("to-account does not exist: " + fromAccountId);
        };

        transferRepository
                .insertNewTransfer(fromAccountId, toAccountId, amount);
    }

}
