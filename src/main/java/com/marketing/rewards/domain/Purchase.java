package com.marketing.rewards.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Purchase {
    private @Id
    @GeneratedValue Long id;

    @OneToMany
    private List<Product> products;

    @ManyToOne
    private Customer customer;

    private LocalDate purchaseDate;

}
