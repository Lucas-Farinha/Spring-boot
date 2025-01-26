package com.api.spring_boot.controllers;

import com.api.spring_boot.dtos.PersonDto;
import com.api.spring_boot.models.Person;
import com.api.spring_boot.services.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/person")
public class PersonController {
    final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<Object> savePerson(@RequestBody @Valid PersonDto personDto) {

        if (personService.existsPersonByCpf(personDto.getCpf())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CONFLICT: CPF is already related to a person");
        }

        var person = new Person();
        BeanUtils.copyProperties(personDto, person);
        person.setRegistrationDate(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.save(person));
    }

    @GetMapping
    public ResponseEntity<Page<Person>> getAllPeople(@PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(personService.findAll(pageable));
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "cpf") String cpf){
        Optional<Person> personFound = personService.findByCpf(cpf);

        if (!personFound.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(personFound.get());
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Object> deletePerson(@PathVariable(value = "cpf") String cpf){
        Optional<Person> personFound = personService.findByCpf(cpf);
        if (!personFound.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }
        personService.delete(personFound.get());
        return ResponseEntity.status(HttpStatus.OK).body("Person deleted successfully.");
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<Object> savePerson(@RequestBody @Valid PersonDto personDto, @PathVariable(value = "cpf") String cpf) {

        Optional<Person> personFound = personService.findByCpf(cpf);
        if (!personFound.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }

        var person = personFound.get();
        person.setName(personDto.getName());
        person.setBirthDate(personDto.getBirthDate());
        person.setSalary(personDto.getSalary());
        person.setActive(personDto.getActive());

        return ResponseEntity.status(HttpStatus.OK).body(personService.save(person));
    }
}
