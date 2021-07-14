package com.marketing.rewards.repository;

import com.marketing.rewards.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
