package com.example.project5.dao;

import com.example.project5.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fakeDao")
//data base
public class FakePersonDataAccessService implements PersonDao{

    private static List<Person> DB;

    public FakePersonDataAccessService() {
        this.DB = new ArrayList<>();
    }

    @Override
    public int insertPerson(UUID id, Person person) {
        DB.add(new Person(id, person.getName()));
        return 0;
    }

    public List<Person> selectAllPeople() {
        return DB;
    }

    @Override
    public Optional<Person> selectPersonById(UUID id) {
        return DB.stream().filter(person -> person.getId().equals(id)).findFirst();
    }

    @Override
    public int deletePersonById(UUID id) {
        Optional<Person> personOptional = selectPersonById(id);
        if(personOptional.isEmpty()) {
            return 0;
        }
        DB.remove(personOptional.get());
        return 1;
    }

    @Override
    public int updatePersonById(UUID id, Person newPerson) {
        return selectPersonById(id)
                .map(person -> {
                    int index = DB.indexOf(person);
                    if(index >= 0) {
                        DB.set(index, new Person(id, newPerson.getName()));
                        return  1;
                    }
                    return 0;
                })
                .orElse(0);
    }
}
