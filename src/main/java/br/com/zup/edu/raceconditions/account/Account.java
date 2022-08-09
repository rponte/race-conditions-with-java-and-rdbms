package br.com.zup.edu.raceconditions.account;

import javax.persistence.*;
import java.math.BigDecimal;

import static java.math.BigDecimal.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String holderName;

    @Column(nullable = false)
    private BigDecimal balance;

    @Deprecated
    public Account(){}

    public Account(String holderName, BigDecimal balance) {
        this.holderName = holderName;
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Transfer transferTo(Account toAccount, BigDecimal amount) {

        this.debit(amount);
        toAccount.credit(amount);

        return new Transfer(this, toAccount, amount);
    }

    private void debit(BigDecimal amount) {
        if (balance.subtract(amount).compareTo(ZERO) < 0) {
            throw new IllegalStateException("there's not enough balance");
        }
        this.balance = balance.subtract(amount);
    }

    private void credit(BigDecimal amount) {
        this.balance = balance.add(amount);
    }

}
