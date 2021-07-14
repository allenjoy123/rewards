package com.marketing.rewards.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
public class Reward {
    private @Id
    @GeneratedValue Long id;

    private int pointValue;

    @OneToOne
    private Customer customer;

}
