package com.mongoTemplate.mongoDemo.collection;

public class FullName {
    private String firstName;
    private String middleName;
    private String lastName;
    private int age;
    private String fullName;

    public FullName(String firstName, String middleName, String lastName, int age, String fullName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.age = age;
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
