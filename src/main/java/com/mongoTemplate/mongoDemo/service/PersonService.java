package com.mongoTemplate.mongoDemo.service;


import com.mongoTemplate.mongoDemo.collection.Person;
import com.mongoTemplate.mongoDemo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    public String savePerson(Person person) {
        return personRepository.save(person).getName();
    }

    public List<Person> findPersonByName(String name) {

        return personRepository.findByName(name);
    }

    public void deleteById(String id) {
        personRepository.deleteById(id);
    }

    public List<Person> findByAgeBetween(Integer minAge, Integer maxAge) {
        return personRepository.findPersonByAgeBetween(minAge,maxAge);
    }

    public Page<Person> search(String name, Integer minAge, Integer maxAge, String city, Pageable pageable) {

        Query query = new Query().with(pageable);
        List<Criteria> criteria = new ArrayList<>();

        if (name != null  && !name.isEmpty())
        {
            criteria.add(Criteria.where("name").regex(name,"i"));
        }

        if(minAge != null && maxAge != null )
        {
            criteria.add(Criteria.where("age").gte(minAge).lte(maxAge));
        }

        if(city != null && !city.isEmpty())
        {
            criteria.add(Criteria.where("address.city").is(city));
        }

        //this and operator is used to combine multiple criterias into one single criteria and
        //checks for all matching or not
        //the toArray is used to convert the list of criteria to an array the new Criteria[0]
        //is passed to say that this the list need to be converted into an array of this type
        //because java by default does not know that
        if(!criteria.isEmpty() ) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }

        Page<Person> people = PageableExecutionUtils.getPage(
                mongoTemplate.find(query,Person.class
                ), pageable, ()->mongoTemplate.count(query.skip(0).limit(0),Person.class));

                return people;
    }

    public List<Person> findOldest() {

        UnwindOperation unwindOperation = Aggregation.unwind("address");
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC,"age");

        GroupOperation groupOperation = Aggregation.group("addresses.city");

        Aggregation aggregation = Aggregation.newAggregation(
                unwindOperation,
                sortOperation,
                groupOperation
        );

        AggregationResults<Person> answer = mongoTemplate.aggregate(aggregation,Person.class,Person.class);
        System.out.println(answer.getRawResults());
        return  answer.getMappedResults();
    }

    public List<Person> getPopulationCount() {

        UnwindOperation unwindOperation = Aggregation.unwind("addresses");

        GroupOperation groupOperation = Aggregation.group("addresses.city")
                .count().as("PopulationCount");

        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC,"PopulationCount");

        ProjectionOperation projectionOperation = Aggregation.project("addresses.city","PopulationCount");
        Aggregation aggregation = Aggregation.newAggregation(
                unwindOperation,
                sortOperation,
                groupOperation
        );

        return mongoTemplate.aggregate(aggregation,Person.class,Person.class).getMappedResults();
    }
}
