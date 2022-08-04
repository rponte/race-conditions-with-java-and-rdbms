package br.com.zup.edu.raceconditions.model.optimistic;

import br.com.zup.edu.raceconditions.model.Event;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Ticket2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Event2 event;

    @Column(nullable = false)
    private String customerName;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Deprecated
    public Ticket2(){}

    public Ticket2(Event2 event, String customerName) {
        this.event = event;
        this.customerName = customerName;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", event=" + event +
                ", customerName='" + customerName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
