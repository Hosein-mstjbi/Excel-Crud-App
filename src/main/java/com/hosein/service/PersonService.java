package com.hosein.service;

import com.hosein.model.Person;
import com.hosein.repository.ExcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * لایه سرویس شامل تمام قوانین کسب و کار است.
 * صحت سنجی فرمت داده ها در خود مدل انجام میشود.
 * این لایه قوانین اضافه تری مثل (عدم تکرار کد ملی) را بررسی میکند.
 */
@Service
public class PersonService {

    private final ExcelRepository repository;

    @Autowired
    public PersonService(ExcelRepository repository) {
        this.repository = repository;
    }

    public List<Person> findAll() {
        return repository.findAll();
    }

    public Optional<Person> findById(int id) {
        return repository.findById(id);
    }

    public void save(Person person) {
        validateNationalCodeIsUnique(person);
        repository.save(person);
    }


    public void deleteById(int id) {
        repository.deleteById(id);
    }

    /**
     * کد ملی نباید تکراری باشد بجز خو رکورد در حال پردازش.
     */
    private void validateNationalCodeIsUnique(Person person) {
        boolean duplicateExists = repository.findAll().stream().filter(p -> p.getId() != person.getId())
                .anyMatch(p -> p.getNationalCode().equals(person.getNationalCode()));

        if (duplicateExists) {
            throw new IllegalArgumentException("رکوردی با این کد ملی قبلا ثبت شده است.");
        }
    }
}
