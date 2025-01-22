package com.mongoTemplate.mongoDemo.service;

import com.mongoTemplate.mongoDemo.collection.FullName;
import com.mongoTemplate.mongoDemo.collection.Names;
import com.mongoTemplate.mongoDemo.collection.Person;
import org.apache.el.lang.ExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.expression.Expression;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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

    public List<Names> fullNameSearch(String input) {


        //this method here converts the input into regex
        //to this pattern
        // ()|()|() - this pattern converts the given input string into an or operator regex according to the array length
        //it checks for any of the conditions
        String [] array = input.split("\\s");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++)
        {
            if(i == array.length - 1)
            {
                sb.append('(').append(array[i]).append(')');
            }
            else
            {
                sb.append('(').append(array[i]).append(')').append('|');
            }
        }

        String sbRegex = sb.toString();
        System.out.println(sbRegex);
        //if the input is rav k uh
        //the output regex will be(rav)|(K)|(uh)
        //now we construct a regex pattern out of this
        Pattern pattern = Pattern.compile(sbRegex,Pattern.CASE_INSENSITIVE);
        //we specify the criteria list for each field
        List<Criteria> criteria = new ArrayList<>();



        //firstName
        if(array.length == 1) {
            criteria.add(Criteria.where("firstName").regex(pattern));
            //middleName
            criteria.add(Criteria.where("middleName").regex(pattern));
            //lastName
            criteria.add(Criteria.where("lastName").regex(pattern));
        }

        if(array.length == 2)
        {
            //firstName and middleName
            criteria.add(new Criteria().andOperator(
                    Criteria.where("firstName").regex(pattern),
            Criteria.where("middleName").regex(pattern)));


            //middleName and lastName
            criteria.add(new Criteria().andOperator(
                    Criteria.where("middleName").regex(pattern),
                    Criteria.where("lastName").regex(pattern)
            ));


            //firstName and lastName
            criteria.add(new Criteria().andOperator(

                    Criteria.where("firstName").regex(pattern),
                    Criteria.where("lastName").regex(pattern)
            ));
        }

        //all fields
        if(array.length == 3) {
            criteria.add(new Criteria().andOperator(
                    Criteria.where("firstName").regex(pattern),
                    Criteria.where("middleName").regex(pattern),
                    Criteria.where("lastName").regex(pattern)
            ));
        }


        //now we specify the query and we specify the and operator for the criteria
        Query query = new Query();

        query.addCriteria(new Criteria().orOperator(criteria.toArray(criteria.toArray(new Criteria[0]))));

        return mongoTemplate.find(query,Names.class);
    }
}
