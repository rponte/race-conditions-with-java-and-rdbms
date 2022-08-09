package br.com.zup.edu.raceconditions.account.services;

import br.com.zup.edu.raceconditions.account.model.Account;
import br.com.zup.edu.raceconditions.account.model.AccountRepository;
import br.com.zup.edu.raceconditions.account.model.Transfer;
import br.com.zup.edu.raceconditions.account.model.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class SimpleTransferService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransferRepository transferRepository;

    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {

        Account fromAccount = accountRepository.findById(fromAccountId).orElseThrow(() -> {
            throw new IllegalStateException("from-account does not exist: " + fromAccountId);
        });

        Account toAccount = accountRepository.findById(toAccountId).orElseThrow(() -> {
            throw new IllegalStateException("to-account does not exist: " + toAccountId);
        });

        Transfer transfer = fromAccount.transferTo(toAccount, amount);
        transferRepository.save(transfer);
    }

}
