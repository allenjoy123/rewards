package com.marketing.rewards.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Customer {
    private @Id @GeneratedValue Long id;
    private String firstName;
    private String lastName;
    @OneToMany
    private List<Purchase> purchaseList;
    @OneToOne
    private Reward reward;

    public Customer(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer() {

    }
}
