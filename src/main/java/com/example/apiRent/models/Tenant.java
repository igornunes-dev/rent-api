package com.example.apiRent.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tenant")
@Setter
@Getter
@NoArgsConstructor
public class Tenant extends Users{
    @OneToMany(mappedBy = "tenant")
    private Set<Contract> contracts = new HashSet<>();
}
