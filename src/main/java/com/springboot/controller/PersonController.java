package com.springboot.controller;

import com.springboot.model.Person;
import com.springboot.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/persons")
public class PersonController {
    @Autowired
    private PersonService personService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public List<Person> getAllPersons() {
        return personService.findAll();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public Person getPersonById(@PathVariable Long id) {
        return personService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        return personService.save(person);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Person updatePerson(@PathVariable Long id, @RequestBody Person person) {
        person.setId(id);
        return personService.save(person);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id) {
        personService.deleteById(id);
    }
}