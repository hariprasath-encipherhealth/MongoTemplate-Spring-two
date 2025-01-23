package com.mongoTemplate.mongoDemo.service;

import com.mongoTemplate.mongoDemo.collection.Names;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class NewLogicService {

    @Autowired
    MongoTemplate mongoTemplate;
    public List<Names> newLogic(String input) {

        if(input.isEmpty())
        {
            return null;
        }
        List<String> fieldNames = Arrays.asList("firstName","middleName","lastName");

        Criteria criteria = newLogicCriteria(input,fieldNames);

        Query query = new Query();
        query.addCriteria(criteria);



        return mongoTemplate.find(query,Names.class);
    }

//    public static Criteria newLogicCriteria(String input, List<String> fieldNames)
//    {
//        String[] inputArray = input.split("\\s+");
//
//        List<Criteria> criteria = new ArrayList<>();
//
//        //this outer loop runs the size of the input array
//        for (String string : inputArray) {
//            List<Criteria> tempCriteria = new ArrayList<>();
//
//            //this inner loop runs the size of the number of fields
//            for (String fieldName : fieldNames) {
//                tempCriteria.add(Criteria.where(fieldName).regex(string));
//                System.out.print(fieldName + " " + string);
//            }
//
//            criteria.add(new Criteria().orOperator(tempCriteria.toArray(criteria.toArray(new Criteria[0]))));
//        }
//
//        return new Criteria().andOperator(criteria.toArray(criteria.toArray(new Criteria[0])));
//    }


    public static Criteria newLogicCriteria(String input, List<String> fieldNames) {
        String[] inputArray = input.split("\\s+");

        List<Criteria> criteria = new ArrayList<>();

        // This outer loop runs the size of the input array
        for (String string : inputArray) {
            List<Criteria> tempCriteria = new ArrayList<>();

            String regex = Pattern.quote(string);
            Pattern pattern = Pattern.compile(regex);
            // This inner loop runs the size of the number of fields
            for (String fieldName : fieldNames) {
                Criteria fieldCriteria = Criteria.where(fieldName).regex(pattern);
                if (fieldCriteria != null) {
                    tempCriteria.add(fieldCriteria);
                    System.out.print(fieldName + " " + string);
                } else {
                    System.out.println("Null Criteria for field: " + fieldName + " with string: " + string);
                }
            }

            // Only add non-null tempCriteria to the main criteria list
            if (!tempCriteria.isEmpty()) {
                criteria.add(new Criteria().orOperator(tempCriteria.toArray(new Criteria[0])));
            } else {
                System.out.println("No valid criteria for input string: " + string);
            }
        }

        // Ensure you return valid criteria only
        return new Criteria().andOperator(criteria.toArray(new Criteria[0]));
    }


}
