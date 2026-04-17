package com.applexzs.springboot.jpa;

import com.applexzs.springboot.jpa.entities.Person;
import com.applexzs.springboot.jpa.repositories.IPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class SpringbootJpaApplication implements CommandLineRunner {

    @Autowired
    private IPersonRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootJpaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //delete2();
        //create();
        customQueries();

    }

    public void findOne() {
//        Person person = null;
//        Optional<Person> optionalPerson = repository.findById(8L);
//        if(optionalPerson.isPresent()) {
//            person = optionalPerson.get();
//        }
//        System.out.println(person);
        repository.findByNameContaining("hn").ifPresent(person -> System.out.println(person));
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

    @Transactional(readOnly = true)
    public void customQueries(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("=============================== Consulta solo el nombre por id ===============================");
        System.out.print("Ingrese el id para el nombre: ");
        Long id = scanner.nextLong();
        scanner.close();
        System.out.println("===========================  mostrando solo el nombre  ===========================================");
        String name = repository.getNameById(id);
        System.out.println(name);
        System.out.println("===========================  mostrando solo el id  ===========================================");
        String idDb = repository.geIdById(id);
        System.out.println(idDb);
        System.out.println("===========================  mostrando nombre completo  ===========================================");
        String fullname = repository.getFullNameById(id);
        System.out.println(fullname);

        System.out.println("========================== Consulta por campos personalizados por el id =================================");
        Object[] personReg = (Object[]) repository.obtenerPersonDataFullById(id);
        System.out.println("id =" + personReg[0] + ", nombre=" + personReg[1] + ", " + personReg[2] + ", lenguaje " + personReg[3]);
        System.out.println("=========================== Consulta por campor personalizados lista =======================================");
        List<Object[]> regs = repository.obtenerPersonData();
        regs.forEach(reg -> System.out.println("id =" + reg[0] + ", nombre=" + reg[1] + ", " + reg[2] + ", lenguaje " + reg[3]));
    }

    @Transactional
    public void delete2() {

        repository.findAll().forEach(System.out::println);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el id a eliminar:");
        Long id = scanner.nextLong();

        Optional<Person> personOptional = repository.findById(id);
        personOptional.ifPresentOrElse(person -> repository.delete(person), () -> System.out.println("Lo sentimos no existe la persona con ese id"));

        repository.findAll().forEach(System.out::println);
    }

    @Transactional
    public void delete() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el id a eliminar:");
        Long id = scanner.nextLong();
        repository.deleteById(id);
        repository.findAll().forEach(System.out::println);
    }

    @Transactional
    public void update() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el id de la persona:");
        Long id = scanner.nextLong();
        Optional<Person> optionalPerson = repository.findById(id);

        //optionalPerson.ifPresent(person -> {
        if(optionalPerson.isPresent()) {
            Person person = optionalPerson.orElseThrow();
            System.out.print("Ingrese el lenguaje de programacion");
            String programmingLanguage = scanner.next();
            person.setProgrammingLanguage(programmingLanguage);
            Person personDb = repository.save(person);
            System.out.println(personDb);
        } else {
            System.out.println("El usuario no esta presente! No existe!");
        }
        //});

        scanner.close();
    }

    @Transactional
    public void create() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el nombre de la persona:");
        String name = scanner.nextLine();
        System.out.print("Ingrese el apellido de la persona:");
        String lastname = scanner.nextLine();
        System.out.print("Ingrese el lenguaje de programación de la persona:");
        String programmingLanguage = scanner.nextLine();

        Person person = new Person(null, name, lastname, programmingLanguage);
        Person personNew = repository.save(person);
        System.out.println(personNew);
    }

}
