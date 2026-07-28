package com.prakash.LogFetch.repo;

import com.prakash.LogFetch.model.UserRegistry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserRegistry, Integer> {
    Optional<UserRegistry> findByUsername(String username);
}
