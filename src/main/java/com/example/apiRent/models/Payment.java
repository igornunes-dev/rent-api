package com.example.apiRent.models;

import com.example.apiRent.enums.EnumContract;
import com.example.apiRent.enums.EnumPayment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "due_date", nullable = false)
    private LocalDate due_date;

    @Column(name = "payment_date", nullable = true)
    private LocalDate payment_date;

    @Column(name = "amount", nullable = false)
    @Positive
    private BigDecimal amount;

    @Column(name = "status", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private EnumPayment status;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;
}
