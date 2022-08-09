package br.com.zup.edu.raceconditions.account.services;

import base.SpringBootIntegrationTest;
import br.com.zup.edu.raceconditions.account.model.Account;
import br.com.zup.edu.raceconditions.account.model.AccountRepository;
import br.com.zup.edu.raceconditions.account.model.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RepeatableReadIsolationLevelTransferServiceTest extends SpringBootIntegrationTest {

    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2);
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100).setScale(2);

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private RepeatableReadIsolationLevelTransferService repeatableReadIsolationLevelTransferService;

    private Account FROM_ACCOUNT;
    private Account TO_ACCOUNT;

    @BeforeEach
    public void setUp() {
        transferRepository.deleteAll();
        accountRepository.deleteAll();
        this.FROM_ACCOUNT = accountRepository.save(new Account("Rafael Ponte", ONE_HUNDRED));
        this.TO_ACCOUNT = accountRepository.save(new Account("Jordi", ZERO));
    }

    @Test
    public void shouldTransferMoneyBetweenAccounts() throws InterruptedException {

        assertEquals(ONE_HUNDRED, accountRepository.getBalance(FROM_ACCOUNT.getId()));
        assertEquals(ZERO, accountRepository.getBalance(TO_ACCOUNT.getId()));

        doSyncAndConcurrently(10, s -> {
            repeatableReadIsolationLevelTransferService
                    .transfer(FROM_ACCOUNT.getId(), TO_ACCOUNT.getId(), new BigDecimal("20"));
        });

        assertAll("transfer between accounts",
            () -> assertEquals(1, transferRepository.count(), "total of transfer"),
            () -> assertEquals(new BigDecimal("80.00"),
                    accountRepository.getBalance(FROM_ACCOUNT.getId()), "from-account balance"),
            () -> assertEquals(new BigDecimal("20.00"),
                    accountRepository.getBalance(TO_ACCOUNT.getId()), "to-account balance")
        );
    }

}