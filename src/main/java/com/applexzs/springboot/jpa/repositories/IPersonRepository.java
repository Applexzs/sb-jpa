package com.applexzs.springboot.jpa.repositories;

import com.applexzs.springboot.jpa.entities.Person;
import org.springframework.data.repository.CrudRepository;

public interface IPersonRepository extends CrudRepository<Person, Long> {
}