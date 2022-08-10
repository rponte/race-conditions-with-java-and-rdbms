package br.com.zup.edu.raceconditions.account.services;

import br.com.zup.edu.raceconditions.account.model.AccountRepository;
import br.com.zup.edu.raceconditions.account.model.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

@Service
public class NaiveAtomicUpdateTransferService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransferRepository transferRepository;

    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {

        BigDecimal balance = accountRepository.getBalance(fromAccountId);
        if (balance.compareTo(ZERO) < 0) {
            throw new IllegalStateException("there's not enough balance");
        }

        accountRepository.debitOnlyBalance(fromAccountId, amount);
        accountRepository.creditBalance(toAccountId, amount);

        transferRepository
                .insertNewTransfer(fromAccountId, toAccountId, amount);
    }

}
