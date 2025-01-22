package com.mongoTemplate.mongoDemo.service;

import com.mongoTemplate.mongoDemo.collection.Names;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CriteriaListServicePermutation {


    @Autowired
    MongoTemplate mongoTemplate;
    //this method calls the criteria list generator which returns the criteria list
    //then runs the query and generates output by running the query using the criteria list generated
    public List<Names> fullNameSearch(List<String> fieldName, String input) {

        //criteria list generator
        List<Criteria> criteria = givefullCriteriaList(fieldName,input);

        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(criteria.toArray(criteria.toArray(new Criteria[0]))));
        return mongoTemplate.find(query,Names.class);
    }

    //this method initially calls the giveAllFields method which returns the permutation of all the
    //fields using the provided fieldNames and input fields as array
    public static List<Criteria> givefullCriteriaList(List<String> fieldName,String inputFields)
    {
        //we split the input field by spaces
        String[] inputArray = inputFields.split("\\s+");

        //we create list of list String to store the permutation of strings
        List<List<String>> fields = new ArrayList<>();

        //this recursive function uses the fieldName and the length of the input array to generate the
        //permutations
        giveAllFieldsRecursive(
                fieldName, new ArrayList<>(),fields,new boolean[fieldName.size()],inputArray.length);

        //now we pass the list of list String to generate criterias and insert the input fields
        List<Criteria> criteria = new ArrayList<>();
        for (List<String> field : fields) {
            criteria.add(generateCriteria(field, inputArray));
        }

        //this is to print the regex in the criteria
        for (Criteria c : criteria) {
            String regex = c.getCriteriaObject().toString();
            System.out.println(  " Regex: " + regex);
        }

        return criteria;
    }

    //this is the recursive function that generates the permutation of the fieldNames
    public static void giveAllFieldsRecursive(
            List<String> fieldNames, List<String> temp, List<List<String>> fields, boolean[] visitedArray, int lengthOfInputArray)
    {
        if(temp.size() == lengthOfInputArray)
        {
            fields.add(new ArrayList<>(temp));
            return;
        }

        for (int i = 0; i < fieldNames.size(); i++) {

            if(!visitedArray[i]) {
                visitedArray[i] = true;
                temp.add(fieldNames.get(i));
                giveAllFieldsRecursive(fieldNames, temp, fields, visitedArray, lengthOfInputArray  );
                temp.remove(temp.size()-1);
                visitedArray[i] = false;

            }
        }
    }

    //this method returns the criteria by using the list of fields and input fields
    public static Criteria generateCriteria(List<String> fieldName,String[] inputFields)
    {
        //initially we define a criteria list
        List<Criteria> criteria1 =  new ArrayList<>();

        //now we iterate through the list of fields and generate a criteria to add to the list
        for (int i = 0; i < fieldName.size(); i++) {
            criteria1.add(Criteria.where(fieldName.get(i)).regex(inputFields[i],"i"));
        }

        //next we convert the criteria list into a single criteria and return it
        return new Criteria().andOperator(criteria1.toArray(criteria1.toArray(new Criteria[0])));
    }


}
