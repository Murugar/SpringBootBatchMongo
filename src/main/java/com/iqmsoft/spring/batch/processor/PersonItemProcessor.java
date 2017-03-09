package com.iqmsoft.spring.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import com.iqmsoft.spring.batch.model.Person;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {
	
    @Override
    public Person process(final Person person) throws Exception {
        return person;
    }
}