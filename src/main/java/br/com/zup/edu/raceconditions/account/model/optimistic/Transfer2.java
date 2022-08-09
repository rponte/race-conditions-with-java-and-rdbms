package br.com.zup.edu.raceconditions.account.model.optimistic;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Transfer2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
        name = "from_account_id",
        nullable = false, updatable = false
    )
    private Account2 fromAccount;

    @ManyToOne
    @JoinColumn(
        name = "to_account_id",
        nullable = false, updatable = false
    )
    private Account2 toAccount;

    @Column(nullable = false, updatable = false)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @Deprecated
    public Transfer2(){}

    public Transfer2(Account2 fromAccount, Account2 toAccount, BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                '}';
    }
}
