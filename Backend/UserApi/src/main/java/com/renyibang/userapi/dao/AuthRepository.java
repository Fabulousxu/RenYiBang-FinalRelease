package com.renyibang.userapi.dao;

import com.renyibang.userapi.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
  boolean existsByUserIdAndPassword(long userId, String password);
}
