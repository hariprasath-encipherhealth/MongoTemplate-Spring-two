package com.mongoTemplate.mongoDemo.service;

import com.mongoTemplate.mongoDemo.collection.Names;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
        //input regex combination
        String[] inputArray = input.trim().split("\\s+");
        List<String> regex = new ArrayList<>();
        //this is for combination
        generateRegex(regex, inputArray,0,new StringBuilder());
        //this is for permutation
        //generatePermutations(regex,inputArray,0,new StringBuilder());


        //field combination
        List<String> listOfFields = Arrays.asList("firstName","middleName","lastName");
        List<List<String>> answerFields = new ArrayList<>();
        CriteriaListServiceCombination.giveAllFieldCombination(answerFields,new ArrayList<>(),listOfFields,0);

        System.out.println("fields");
        for(List<String> field:answerFields)
        {
            System.out.println(field);
        }
        //criteria list generation
        String finalRegex = finalSingleRegexGenerator(regex);

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

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(new Criteria().orOperator(criteria.toArray(criteria.toArray(new Criteria[0]))))
        );

        System.out.println("regex");
        for(String fields : regex)
            System.out.println(fields);


        return mongoTemplate.aggregate(aggregation, Names.class,Names.class).getMappedResults();
        //return mongoTemplate.find(query, Names.class);

    }
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
    public static void generatePermutations(List<String> regex, String[] inputArray, int index, StringBuilder sb) {
        if (index == inputArray.length) {
            // When we have a valid permutation, construct the regex pattern and add it
            if (!sb.isEmpty()) {
                String finalRegex = "(?=" + sb.toString() + ")";
                regex.add(finalRegex);
            }
            return;
        }

        // Generate permutations by swapping elements
        for (int i = index; i < inputArray.length; i++) {
            // Swap the elements at index and i
            swap(inputArray, index, i);

            // Append the current element to the StringBuilder
            sb.append(".*").append(inputArray[index]);

            // Recursively generate the rest of the permutations
            generatePermutations(regex, inputArray, index + 1, sb);

            // Backtrack: remove the last added substring and swap back
            sb.setLength(sb.length() - inputArray[index].length() - 2); // Remove ".*" + inputArray[index]
            swap(inputArray, index, i);  // Backtrack (restore the array)
        }
    }

    // Utility function to swap two elements in the array
    private static void swap(String[] array, int i, int j) {
        String temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }


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
