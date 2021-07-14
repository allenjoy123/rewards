package com.marketing.rewards.repository;

import com.marketing.rewards.domain.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
