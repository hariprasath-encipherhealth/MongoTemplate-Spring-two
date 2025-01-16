package com.mongoTemplate.mongoDemo.collection;

public class CityPopulationDTO {
    private String city;
    private int population;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public CityPopulationDTO(String city, int population) {
        this.city = city;
        this.population = population;
    }

    public CityPopulationDTO()
    {

    }
}
