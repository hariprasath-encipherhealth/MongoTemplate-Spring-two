package com.mongoTemplate.mongoDemo.controller;


import com.mongoTemplate.mongoDemo.collection.*;
import com.mongoTemplate.mongoDemo.service.CriteriaListServiceCombination;
import com.mongoTemplate.mongoDemo.service.CriteriaListServicePermutation;
import com.mongoTemplate.mongoDemo.service.NameService;
import com.mongoTemplate.mongoDemo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;
    @Autowired
    private NameService nameService;
    @Autowired
    private CriteriaListServicePermutation criteriaListServicePermutation;
    @Autowired
    private CriteriaListServiceCombination criteriaListServiceCombination;

    @PostMapping("/save")
    public String savePerson(@RequestBody Person person)
    {
        return personService.savePerson(person);
    }

    @GetMapping("/find/{name}")
    public List<Person> findPersonByName(@PathVariable("name") String name)
    {
        return personService.findPersonByName(name);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") String id)
    {
        personService.deleteById(id);
    }

    @GetMapping("/{minAge}/{maxAge}")
    public List<Person> findByMinAndMaxAge(@PathVariable("minAge") Integer minAge,@PathVariable("maxAge") Integer maxAge)
    {
        return personService.findByAgeBetween(minAge,maxAge);
    }

    @GetMapping("/search")
    public Page<Person> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return personService.search(name,minAge,maxAge,city,pageable);
    }

    @GetMapping("/oldestPerson")
    public List<OldestDTO> findOldest()
    {
        return personService.findOldest();
    }

    @GetMapping("/getPopulationCount")
    public List<CityPopulationDTO> getPopulation()
    {
        return personService.getPopulationCount();

    }

    @GetMapping("/multiLayerSort")
    public List<Person> sort()
    {
        return personService.multiSort();
    }

    @GetMapping("/regexFilter")
    public List<Person> regex()
    {
        return personService.findByRegex();
    }
    @GetMapping("/regexFilterTwo")
    public List<Person> regexTwo()
    {
        return personService.regexSearch();
    }
    @GetMapping("/regexFilterTaskOne")
    public List<Person> regexTaskOne(@RequestParam String name)
    {
        return personService.regexTaskOne(name);
    }

    @GetMapping("/regexFilterTaskTwo")
    public List<Names> regexTaskTwo(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String middleName,
            @RequestParam(required = false) String lastName)

    {
        return nameService.regexTaskTwo(firstName,middleName,lastName);
    }

    @GetMapping("/fullNameSearch")
    public List<Names> fullNameSearch(@RequestParam String input)
    {
        return nameService.fullNameSearch(input);
    }

    @GetMapping("/get-full-criteria-permutation")
    public List<Names> fullCriteriaService(@RequestBody List<String> fieldName, @RequestParam String input)
    {
        return criteriaListServicePermutation.fullNameSearch(fieldName,input);
    }

    @GetMapping("/get-full-criteria-combination")
    public List<Names> fullCriteriaServiceCombination(@RequestBody List<String> fieldName, @RequestParam String input)
    {
        return criteriaListServiceCombination.fullNameSearch(fieldName,input);
    }

    @GetMapping("/get-criteria")
    public List<Names> getCriteria(@RequestParam String input)
    {
        return criteriaListServiceCombination.getCriteria(input);
    }

}
