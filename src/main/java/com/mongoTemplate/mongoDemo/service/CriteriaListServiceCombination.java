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

@Service
public class CriteriaListServiceCombination {
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
        String[] inputArray = inputFields.split("\\s");

        //we create list of list String to store the permutation of strings
        List<List<String>> fields = new ArrayList<>();

        //this recursive function uses the fieldName and the length of the input array to generate the
        //permutations
        giveAllFieldsRecursiveCombination(
                fieldName, new ArrayList<>(),fields,0,inputArray.length);

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

    private static void giveAllFieldsRecursiveCombination(List<String> fieldNames, List<String>  temp, List<List<String>> fields, int start, int lengthOfInputArray) {
        if(temp.size() < lengthOfInputArray)
        {
            fields.add(new ArrayList<>(temp));
            return;
        }

        for (int i = start; i < fieldNames.size(); i++) {
            temp.add(fieldNames.get(i));
            giveAllFieldsRecursiveCombination(fieldNames, temp, fields, i + 1, lengthOfInputArray);
            temp.remove(temp.size()-1);
        }
    }


    public static Criteria generateCriteria(List<String> field,String[] inputArray)
    {


        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i < inputArray.length ; i++)
        {
            if(i == inputArray.length -1)
                sb.append('(').append(inputArray[i]).append(')');
            else
                sb.append('(').append(inputArray[i]).append(')').append('|');
        }
        //initially we define a criteria list
        List<Criteria> criteria1 =  new ArrayList<>();

        //now we iterate through the list of fields and generate a criteria to add to the list
        for (int i = 0; i < field.size(); i++) {
            criteria1.add(Criteria.where(field.get(i)).regex(sb.toString(),"i"));
        }

        //next we convert the criteria list into a single criteria and return it
        return new Criteria().andOperator(criteria1.toArray(criteria1.toArray(new Criteria[0])));
    }

    public List<Names> getCriteria(String input)
    {
        List<String> list = Arrays.asList("firstName","middleName","lastName");

        List<List<String>> answer = new ArrayList<>();
        List<Criteria> criteria = new ArrayList<>();
        giveAllFieldCombination(answer,new ArrayList<>(),list,0);

        String[] inputArray = input.split("\\s+");
        for (List<String> list1 : answer)
        {
            if(!list1.isEmpty())
            {
                criteria.add(generateCriteria(list1,inputArray));
            }
        }

        for (Criteria c : criteria) {
            String regex = c.getCriteriaObject().toString();
            System.out.println(  " Regex: " + regex);
        }

        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(criteria.toArray(criteria.toArray(new Criteria[0]))));
        List<Names> returnAnswer = mongoTemplate.find(query,Names.class);
        return returnAnswer;
    }

    public static void giveAllFieldCombination(List<List<String>> answer,List<String> temp, List<String> names,int index)
    {
        if(index == names.size())
        {
            answer.add(new ArrayList<>(temp));
            return;
        }

        temp.add(names.get(index));
        giveAllFieldCombination(answer, temp, names, index+1);
        temp.removeLast();
        giveAllFieldCombination(answer, temp, names, index + 1);
    }
}
