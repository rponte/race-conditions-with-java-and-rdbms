package br.com.zup.edu.raceconditions.account.services;

import base.SpringBootIntegrationTest;
import br.com.zup.edu.raceconditions.account.model.Account;
import br.com.zup.edu.raceconditions.account.model.AccountRepository;
import br.com.zup.edu.raceconditions.account.model.TransferRepository;
import br.com.zup.edu.raceconditions.account.model.optimistic.Account2;
import br.com.zup.edu.raceconditions.account.model.optimistic.Account2Repository;
import br.com.zup.edu.raceconditions.account.model.optimistic.Transfer2Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OptimisticLockingTransferServiceTest extends SpringBootIntegrationTest {

    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2);
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100).setScale(2);

    @Autowired
    private Account2Repository account2Repository;
    @Autowired
    private Transfer2Repository transfer2Repository;

    @Autowired
    private OptimisticLockingTransferService optimisticLockingTransferService;

    private Account2 FROM_ACCOUNT;
    private Account2 TO_ACCOUNT;

    @BeforeEach
    public void setUp() {
        transfer2Repository.deleteAll();
        account2Repository.deleteAll();
        this.FROM_ACCOUNT = account2Repository.save(new Account2("Rafael Ponte", ONE_HUNDRED));
        this.TO_ACCOUNT = account2Repository.save(new Account2("Jordi", ZERO));
    }

    @Test
    public void shouldTransferMoneyBetweenAccounts() throws InterruptedException {

        assertEquals(ONE_HUNDRED, account2Repository.getBalance(FROM_ACCOUNT.getId()));
        assertEquals(ZERO, account2Repository.getBalance(TO_ACCOUNT.getId()));

        doSyncAndConcurrently(10, s -> {
            optimisticLockingTransferService
                    .transfer(FROM_ACCOUNT.getId(), TO_ACCOUNT.getId(), new BigDecimal("20"));
        });

        assertAll("transfer between accounts",
            () -> assertEquals(1, transfer2Repository.count(), "total of transfers"),
            () -> assertEquals(new BigDecimal("80.00"),
                    account2Repository.getBalance(FROM_ACCOUNT.getId()), "from-account balance"),
            () -> assertEquals(new BigDecimal("20.00"),
                    account2Repository.getBalance(TO_ACCOUNT.getId()), "to-account balance")
        );
    }

}