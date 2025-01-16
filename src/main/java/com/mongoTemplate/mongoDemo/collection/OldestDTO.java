package com.mongoTemplate.mongoDemo.collection;

public class OldestDTO {
    private String city;
    private int oldestAge;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getOldestAge() {
        return oldestAge;
    }

    public void setOldestAge(int oldestAge) {
        this.oldestAge = oldestAge;
    }

    public OldestDTO(String city, int oldestAge) {
        this.city = city;
        this.oldestAge = oldestAge;
    }

    public OldestDTO()
    {

    }
}
