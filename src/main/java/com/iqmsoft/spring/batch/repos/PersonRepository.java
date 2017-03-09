package com.iqmsoft.spring.batch.repos;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.iqmsoft.spring.batch.model.Person;



public interface PersonRepository extends MongoRepository<Person, String>{

}
