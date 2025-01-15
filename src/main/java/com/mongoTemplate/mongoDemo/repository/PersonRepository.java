package com.mongoTemplate.mongoDemo.repository;

import com.mongoTemplate.mongoDemo.collection.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends MongoRepository<Person,String> {
    List<Person> findByName(String name);

    @Query(value = "{'age':{$gt:?0,$lt:?1}}")
    List<Person> findPersonByAgeBetween(Integer minAge, Integer maxAge);
}
