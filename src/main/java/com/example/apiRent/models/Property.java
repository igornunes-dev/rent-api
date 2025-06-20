package com.example.apiRent.models;

import com.example.apiRent.enums.EnumProperty;
import com.example.apiRent.enums.EnumUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "property")
@Getter
@Setter
@NoArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "price", nullable = false)
    @Positive
    private BigDecimal price;

    @Column(name = "status", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private EnumProperty status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;


}
