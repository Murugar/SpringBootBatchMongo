package com.iqmsoft.spring.batch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="persons")
public class Person {

	@Id
    private String id;

    private String firstName;
    private String familyName;
    private Integer year;
    
    public Person()
    {
    	
    }

    public Person(String first, String last)
    {
    	this.firstName = first;
    	this.familyName = last;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
