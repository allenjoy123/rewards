package com.marketing.rewards.repository;

import com.marketing.rewards.domain.Reward;
import org.springframework.data.repository.CrudRepository;

public interface RewardRepository extends CrudRepository<Reward, Long> {
}
