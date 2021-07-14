package com.marketing.rewards.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Product {
    private @Id
    @GeneratedValue Long id;

    private double price;

    private String name;

    private String description;

}
