package com.api.spring_boot.services;

import com.api.spring_boot.dtos.PersonDto;
import com.api.spring_boot.models.Person;
import com.api.spring_boot.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonService {

    final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public Person save(Person person) {
        return personRepository.save(person);
    }

    public boolean existsPersonByCpf(String cpf) {
        return personRepository.existsPersonByCpf(cpf);
    }

    public Page<Person> findAll(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    public Optional<Person> findByCpf(String cpf) {
        return personRepository.findByCpf(cpf);
    }

    @Transactional
    public void delete(Person person) {
        personRepository.delete(person);
    }
}
