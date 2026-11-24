package com.qfieldcloud.core.domain.repository;

import com.qfieldcloud.core.domain.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByUsername(String username);
    boolean existsByEmail(String email);
}