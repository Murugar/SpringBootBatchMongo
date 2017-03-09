package com.iqmsoft.spring.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.iqmsoft.spring.batch.model.Person;
import com.iqmsoft.spring.batch.repos.PersonRepository;

@Configuration
@EnableMongoRepositories
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class Application implements CommandLineRunner {

	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	@Autowired
	private PersonRepository arepos;

	public static void main(String args[]) {
		logger.debug("Init");
		logger.info("Init");
		
		SpringApplication.run(BatchConfiguration.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {

		logger.debug("Run");
		logger.info("Run");

		if (arepos.findAll().isEmpty()) {
			arepos.save(new Person("admin", "admin"));
			arepos.save(new Person("user", "user"));
		}

	}
}
