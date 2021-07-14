package com.marketing.rewards.repository;

import com.marketing.rewards.domain.Purchase;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
}
