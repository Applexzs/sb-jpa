package com.applexzs.springboot.jpa;

import com.applexzs.springboot.jpa.dto.PersonDto;
import com.applexzs.springboot.jpa.entities.Person;
import com.applexzs.springboot.jpa.repositories.IPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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
        update();
    }

    @Transactional(readOnly = true)
    public void whereIn(){
        System.out.println("================== Consulta where in ==================");
        List<Person> persons = repository.getPersonsByIds(Arrays.asList(1L, 2L, 5L, 7L));
        persons.forEach(System.out::println);
    }


    @Transactional(readOnly = true)
    public void subQueries(){
        System.out.println("================== Consultas por el nombre mas corto y su largo ==================");
        List<Object[]>  registers = repository.getShorterName();
        registers.forEach(reg -> {
            String name = (String) reg[0];
            Integer length = (Integer) reg[1];
            System.out.println("name = " + name + ", length = " + length);
        });

        System.out.println("================== Consulta paara obtener el ultimo registro de persona ==================");
        Optional<Person> optionalPerson = repository.getLastRegistration();
        optionalPerson.ifPresent(System.out::println);
    }

    public void queriesFunctionAggregation() {
        Long count = repository.getTotalPerson();
        Long min = repository.getMinId();
        Long max = repository.getMaxId();

        System.out.println("================== Consultas de la tabla persona ==================");
        System.out.println("Total de registros: " + count);
        System.out.println("Minimo de ID: " + min);
        System.out.println("Maximo de ID: " + max);

        System.out.println("=================== Consulta con el nombre y su largo ================");
        List<Object[]> regs = repository.getPersonNameLength();
        regs.forEach(reg -> {
            String name = (String) reg[0];
            Integer length = (Integer) reg[1];
            System.out.println("name = " + name + ", length = " + length);
        });

        System.out.println("=================== Consulta con el nombre mas largo ================");
        Integer minNameLength = repository.getMinLengthName();
        System.out.println(minNameLength);
        System.out.println("=================== Consulta con el nombre mas corto ================");
        Integer maxNameLength = repository.getMaxLengthName();
        System.out.println(maxNameLength);

        System.out.println("=================== Consultas resumen de funciones de agregacion min, max, sum, avg, count  ================");
        Object[] resumeReg = (Object[]) repository.getResumeAggregationFunction();
        System.out.println("min=" + resumeReg[0] +
                ", max=" + resumeReg[1] +
                ", sum=" + resumeReg[2] +
                ", avg=" + resumeReg[3] +
                ", count=" + resumeReg[4]);
    }

    @Transactional(readOnly = true)
    public void personalizedQueriesBetween() {
        System.out.println("================== Consultas por rangos ==================");
        List<Person> persons = repository.findByIdBetweenOrderByNameDesc(2L, 5L);
        persons.forEach(System.out::println);

        persons = repository.findByNameBetweenOrderByNameAsc("J", "Q");
        persons.forEach(System.out::println);

        persons = repository.findAllByOrderByNameAsc();
        persons.forEach(System.out::println);
    }

    @Transactional(readOnly = true)
    public void personalizedQueriesConcatUpperAndLowerCase() {
        System.out.println("================== Consultas con nombres unicos de personas ==================");
        List<String> names = repository.findAllFullnameConcat();
        names.forEach(System.out::println);

        System.out.println("================== Consultas nombres y apellidos mayuscula ==================");
        names = repository.findAllFullnameUpper();
        names.forEach(System.out::println);
        System.out.println("================== Consultas nombres y apellidos minuscula ==================");
        names = repository.findAllFullnameLower();
        names.forEach(System.out::println);
        System.out.println("================== Consultas personalizada en upper y lower case ==================");
        List<Object[]> regs = repository.obtenerPersonDataListCase();
        regs.forEach(reg -> System.out.println("id=" + reg[0] + ", nombre=" + reg[1] + ", apellido=" + reg[2] + ", lenguaje=" + reg[3]));
    }

    @Transactional(readOnly = true)
    public void personalizedQueriesDistinct() {
        System.out.println("Consultas con nombres de personas");
        List<String> names = repository.findAllNames();
        names.forEach(System.out::println);

        System.out.println("================== Consultas con nombres unicos de personas ==================");
        names = repository.findAllNamesDistinct();
        names.forEach(System.out::println);

        System.out.println("================== Consultas con Lenguajes de programacion unicos ==================");
        List<String> languages = repository.findAllProgrammingLanguagesDistinct();
        languages.forEach(System.out::println);
        System.out.println("================== Consultas con total del lenguajes de programacion unicas ==================");
        Long totalLanguages = repository.findAllProgrammingLanguagesDistinctCount();
        System.out.println("Total de lenguajes de programacion: " + totalLanguages);
    }

    @Transactional(readOnly = true)
    public void personalizedQueries2() {
        System.out.println("================== consulta por objeto persona y lenguaje de programacion ==================");
        List<Object[]> personsRegs = repository.findAllMixPerson();

        personsRegs.forEach(reg -> {
            System.out.println("programmingLanguage=" + reg[1] + ", person=" + reg[0]);
        });

        System.out.println("consulta que puebla y devuelve objeto entity de una instancia personalizada");
        List<Person> persons = repository.findAllObjectPersonPersonalized();
        persons.forEach(System.out::println);

        System.out.println("consulta que puebla y devuelve objeto dto de una clase personalizada");
        List<PersonDto> personsDto = repository.findAllPersonDto();
        personsDto.forEach(System.out::println);
    }

    @Transactional(readOnly = true)
    public void personalizedQueries() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("================== consulta solo el nombre por el id ==================");
        System.out.println("Ingrese el id:");
        Long id = scanner.nextLong();
        scanner.close();

        System.out.println("===== mostrando solo el nombre =====");
        String name = repository.getNameById(id);
        System.out.println(name);

        System.out.println("===== mostrando solo el id =====");
        Long idDb = repository.getIdById(id);
        System.out.println(idDb);

        System.out.println("===== mostrando nombre completo con concat =====");
        String fullName = repository.getFullNameById(id);
        System.out.println(fullName);

        System.out.println("===== consulta por campos personalizados por el id =====");
        Optional<Object> optionalReg = repository.obtenerPersonDataById(id);
        if (optionalReg.isPresent()) {
            Object[] personReg = (Object[]) optionalReg.orElseThrow();
            System.out.println("id=" + personReg[0] + ", nombre=" + personReg[1] + ", apellido=" + personReg[2] + ", lenguaje=" + personReg[3]);
        }

        System.out.println("===== consulta por campos personalizados lista ======");
        List<Object[]> regs = repository.obtenerPersonDataList();
        regs.forEach(reg -> System.out.println("id=" + reg[0] + ", nombre=" + reg[1] + ", apellido=" + reg[2] + ", lenguaje=" + reg[3]));
    }

    @Transactional
    public void delete2() {
        repository.findAll().forEach(System.out::println);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el id a eliminar:");
        Long id = scanner.nextLong();

        Optional<Person> optionalPerson = repository.findById(id);

        optionalPerson.ifPresentOrElse(repository::delete,
                () -> System.out.println("Lo sentimos no existe la persona con ese id!"));

        repository.findAll().forEach(System.out::println);

        scanner.close();
    }

    @Transactional
    public void delete() {
        repository.findAll().forEach(System.out::println);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el id a eliminar:");
        Long id = scanner.nextLong();
        repository.deleteById(id);

        repository.findAll().forEach(System.out::println);

        scanner.close();
    }

    @Transactional
    public void update() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el id de la persona:");
        Long id = scanner.nextLong();

        Optional<Person> optionalPerson = repository.findById(id);

        // optionalPerson.ifPresent(person -> {
        if (optionalPerson.isPresent()) {
            Person personDB = optionalPerson.orElseThrow();

            System.out.println(personDB);
            System.out.println("Ingrese el lenguaje de programacion:");
            String programmingLanguage = scanner.next();
            personDB.setProgrammingLanguage(programmingLanguage);
            Person personUpdated = repository.save(personDB);
            System.out.println(personUpdated);
        } else {
            System.out.println("El usuario no esta presente! no existe!");
        }

        // });

        scanner.close();
    }

    @Transactional
    public void create() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el nombre:");
        String name = scanner.next();
        System.out.println("Ingrese el apellido:");
        String lastname = scanner.next();
        System.out.println("Ingrese el lenguaje de programacion:");
        String programmingLanguage = scanner.next();
        scanner.close();

        Person person = new Person(null, name, lastname, programmingLanguage);

        Person personNew = repository.save(person);
        System.out.println(personNew);

        repository.findById(personNew.getId()).ifPresent(System.out::println);

    }

    @Transactional(readOnly = true)
    public void findOne() {
        // Person person = null;
        // Optional<Person> optionalPerson = repository.findById(1L);
        // // if (!optionalPerson.isEmpty()) {
        // if (optionalPerson.isPresent()) {
        // 	person = optionalPerson.get();
        // }
        // System.out.println(person);
        repository.findOneLikeName("hn").ifPresent(System.out::println);
    }

    @Transactional(readOnly = true)
    public void list() {
        // List<Person> persons = (List<Person>) repository.findAll();
        // List<Person> persons = (List<Person>) repository.buscarByProgrammingLanguage("Python", "Pepe");

        List<Person> persons = (List<Person>) repository.findByProgrammingLanguageAndName("Java", "Andres");

        persons.stream().forEach(person -> {
            System.out.println(person);
        });

        List<Object[]> personsValues = repository.obtenerPersonDataByProgrammingLanguage("Java");
        personsValues.stream().forEach(person -> {
            System.out.println(person[0] + " es experto en " + person[1]);
        });
    }

}
