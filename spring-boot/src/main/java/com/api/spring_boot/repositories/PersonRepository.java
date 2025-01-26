package com.api.spring_boot.repositories;

import com.api.spring_boot.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

    boolean existsPersonByCpf(String cpf);

    Optional<Person> findByCpf(String cpf);
}
