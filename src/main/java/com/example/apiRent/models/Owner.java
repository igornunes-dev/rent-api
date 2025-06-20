package com.example.apiRent.models;

import jakarta.persistence.*;
import lombok.*;
import com.example.apiRent.models.Users;
import com.example.apiRent.models.Property;
import com.example.apiRent.models.Contract;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "owner")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Owner extends Users {
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Property> properties = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    private Set<Contract> contracts = new HashSet<>();
}
