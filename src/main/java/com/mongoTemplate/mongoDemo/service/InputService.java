package com.mongoTemplate.mongoDemo.service;

import com.mongoTemplate.mongoDemo.collection.Names;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.StringOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class InputService {
    @Autowired
    MongoTemplate mongoTemplate;
    public List<Names> regex(String input)
    {
        //if the input is empty we return null
        if(input.isEmpty())
            return null;

        //input regex combination
        String[] inputArray = input.trim().split("\\s+");
        List<String> regex = new ArrayList<>();
        //this is for regex combination and generation
        generateRegex(regex, inputArray,0,new StringBuilder());
        //this is for permutation
        //generatePermutations(regex,inputArray,0,new StringBuilder());

        //this is to generate the final single regular expression
        String finalRegex = finalSingleRegexGenerator(regex);

        //generating field combination
        List<String> listOfFields = Arrays.asList("firstName","middleName","lastName");
        List<List<String>> answerFields = new ArrayList<>();
        CriteriaListServiceCombination.giveAllFieldCombination(answerFields,new ArrayList<>(),listOfFields,0);

        System.out.println("fields");
        for(List<String> field:answerFields)
        {
            System.out.println(field);
        }


        //criteria list generation
        List<Criteria> criteria = new ArrayList<>();
        for(List<String> fields : answerFields)
        {
            if(!fields.isEmpty()) {
                criteria.add(generateCriteria(fields, finalRegex));
            }
        }

//        for (Criteria c : criteria) {
//            String temp = c.getCriteriaObject().toString();
//            System.out.println(  " Regex: " + temp);
//        }
//        Query query = new Query();
//        query.addCriteria(new Criteria().orOperator(
//                criteria.toArray(criteria.toArray(new Criteria[0]))
//        ));

        StringBuilder finalRegexes= new StringBuilder();
        for (String s : regex)
        {
            finalRegexes.append("(?=.*").append(s).append(")");
        }

        StringOperators stringOperators = new StringOperators();
        StringOperators.valueOf("firstName").concat(" ").
                concatValueOf("middleName").concat(" ").
                concatValueOf("lastName");

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(new Criteria().orOperator(criteria.toArray(criteria.toArray(new Criteria[0]))))

        );

        System.out.println("regex");
        for(String fields : regex)
            System.out.println(fields);

        System.out.println(finalRegex);
        return mongoTemplate.aggregate(aggregation, Names.class,Names.class).getMappedResults();
        //return mongoTemplate.find(query, Names.class);

    }
    //this method generates the regex combination from given input string
    public static void generateRegex(List<String> regex,String[] inputArray,int index,StringBuilder sb)
    {
        if(index == inputArray.length)
        {
            if(!sb.isEmpty()) {
                String finalRegex = "(?=" + sb.toString() + ")";
                regex.add(finalRegex);
            }
            return;
        }

        String temp = ".*"+inputArray[index];
        sb.append(temp);
        generateRegex(regex,inputArray,index+1,sb);
        sb.setLength(sb.length() - temp.length());
        generateRegex(regex,inputArray,index+1,sb);
    }
//    public static void generatePermutations(List<String> regex, String[] inputArray, int index, StringBuilder sb) {
//        if (index == inputArray.length) {
//            // When we have a valid permutation, construct the regex pattern and add it
//            if (!sb.isEmpty()) {
//                String finalRegex = "(?=" + sb.toString() + ")";
//                regex.add(finalRegex);
//            }
//            return;
//        }
//
//        // Generate permutations by swapping elements
//        for (int i = index; i < inputArray.length; i++) {
//            // Swap the elements at index and i
//            swap(inputArray, index, i);
//
//            // Append the current element to the StringBuilder
//            sb.append(".*").append(inputArray[index]);
//
//            // Recursively generate the rest of the permutations
//            generatePermutations(regex, inputArray, index + 1, sb);
//
//            // Backtrack: remove the last added substring and swap back
//            sb.setLength(sb.length() - inputArray[index].length() - 2); // Remove ".*" + inputArray[index]
//            swap(inputArray, index, i);  // Backtrack (restore the array)
//        }
//    }
//
//    // Utility function to swap two elements in the array
//    private static void swap(String[] array, int i, int j) {
//        String temp = array[i];
//        array[i] = array[j];
//        array[j] = temp;
//    }

    //this method converts the combination of regexes into a single regex
    public String finalSingleRegexGenerator(List<String> regexes)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0;i<regexes.size();i++)
        {
            if(i != regexes.size() - 1)
               sb.append(regexes.get(i)).append('|');
            else
                sb.append(regexes.get(i));
        }
        return sb.toString();
    }

    //this method generates the criteria using the given list of field names
    public static Criteria generateCriteria(List<String> fields,String regex)
    {
        List<Criteria> criteria = new ArrayList<>();

        for(String field : fields)
        {
            criteria.add(Criteria.where(field).regex(regex));
        }

        return new Criteria().andOperator(criteria.toArray(criteria.toArray(new Criteria[0])));
    }
}
