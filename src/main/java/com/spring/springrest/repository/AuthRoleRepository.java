package com.spring.springrest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.springrest.entity.AuthRole;

public interface AuthRoleRepository extends JpaRepository<AuthRole, Long> {
    Optional<AuthRole> findByName(String name);
    Boolean existsByName(String username);
}
