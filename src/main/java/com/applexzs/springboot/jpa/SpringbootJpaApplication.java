package com.applexzs.springboot.jpa;

import com.applexzs.springboot.jpa.entities.Person;
import com.applexzs.springboot.jpa.repositories.IPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class SpringbootJpaApplication implements CommandLineRunner {

    @Autowired
    private IPersonRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootJpaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        findOne();
    }

    public void findOne() {
//        Person person = null;
//        Optional<Person> optionalPerson = repository.findById(8L);
//        if(optionalPerson.isPresent()) {
//            person = optionalPerson.get();
//        }
//        System.out.println(person);
        repository.findById(1L).ifPresent(person -> System.out.println(person));
    }

    public void list() {
        //List<Person> persons = (List<Person>) repository.findAll();
        //List<Person> persons = (List<Person>) repository.buscarPorProgrammingLanguage("Java", "Andres");

        List<Person> persons = (List<Person>) repository.findByProgrammingLanguageAndName("Java", "Andres");
        persons.stream().forEach(person -> System.out.println(person));

        List<Object[]> personValues = repository.obtenerPersonData();
        personValues.stream().forEach(person -> {
            System.out.println(person[0] + " es experto en " + person[1]);
            System.out.println(person);
        });
    }

}
