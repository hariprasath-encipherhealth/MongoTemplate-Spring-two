package com.mongoTemplate.mongoDemo.collection;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "person")
public class Person {

    @Id
    private String personId;
    private String name;
    private Integer age;
    private List<String> hobbies;
    private List<Address> addresses;

    public Person()
    {

    }
    public Person(String personId, String name, Integer age, List<String> hobbies, List<Address> addresses) {
        this.personId = personId;
        this.name = name;
        this.age = age;
        this.hobbies = hobbies;
        this.addresses = addresses;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
