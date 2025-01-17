package com.mongoTemplate.mongoDemo.service;

import com.mongoTemplate.mongoDemo.collection.Names;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class NameService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Names> regexTaskTwo(String firstName, String middleName, String lastName) {
        List<Criteria> criteria = new ArrayList<>();

        //we define the list of criteria
        if(firstName != null && !firstName.isEmpty())
        {
            Pattern pattern = Pattern.compile(firstName,Pattern.CASE_INSENSITIVE);
            criteria.add(Criteria.where("firstName").regex(pattern));
            System.out.println("first name");
        }

        if(middleName != null  && !middleName.isEmpty())
        {
            Pattern pattern = Pattern.compile(middleName,Pattern.CASE_INSENSITIVE);
            criteria.add(Criteria.where("middleName").regex(pattern));
            System.out.println("middle name");
        }

        if(lastName != null  && !lastName.isEmpty())
        {
            Pattern pattern = Pattern.compile(lastName,Pattern.CASE_INSENSITIVE);
            criteria.add(Criteria.where("lastName").regex(pattern));
            System.out.println("last name");
        }

        Query query = new Query();
        if(!criteria.isEmpty()) {
            //create a query and pass it to them
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }

        return mongoTemplate.find(query,Names.class);
    }
}
