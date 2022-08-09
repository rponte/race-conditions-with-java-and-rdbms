package br.com.zup.edu.raceconditions.account.model.optimistic;

import javax.persistence.*;
import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

@Entity
public class Account2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String holderName;

    @Column(nullable = false, precision = 9, scale = 2)
    private BigDecimal balance;

    @Version
    private Long version;

    @Deprecated
    public Account2(){}

    public Account2(String holderName, BigDecimal balance) {
        this.holderName = holderName;
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Transfer2 transferTo(Account2 toAccount, BigDecimal amount) {

        this.debit(amount);
        toAccount.credit(amount);

        return new Transfer2(this, toAccount, amount);
    }

    private void debit(BigDecimal amount) {
        if (balance.subtract(amount).compareTo(ZERO) < 0) {
            throw new IllegalStateException("there's not enough balance");
        }
        this.balance = balance.subtract(amount);
    }

    public Long getId() {
        return id;
    }

    private void credit(BigDecimal amount) {
        this.balance = balance.add(amount);
    }

}
