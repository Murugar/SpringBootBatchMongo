package com.iqmsoft.spring.batch;

import com.iqmsoft.spring.batch.model.Person;
import com.iqmsoft.spring.batch.processor.PersonItemProcessor;
import com.iqmsoft.spring.batch.tokenizer.PersonFixedLengthTokenizer;
import com.mongodb.client.MongoClients;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

	@Bean
	public ItemReader<Person> reader() {
		// we read a flat file that will be used to fill a Person object
		FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
		// we pass as parameter the flat file directory
		reader.setResource(new ClassPathResource("PersonData.txt"));
		// we use a default line mapper to assign the content of each line to
		// the Person object
		reader.setLineMapper(new DefaultLineMapper<Person>() {
			{
				// we use a custom fixed line tokenizer
				setLineTokenizer(new PersonFixedLengthTokenizer());
				// as field mapper we use the name of the fields in the Person
				// class
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {
					{
						// we create an object Person
						setTargetType(Person.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public ItemProcessor<Person, Person> processor() {
		logger.info("PersonItemProcessor");
		return new PersonItemProcessor();
	}

	@Bean
	public ItemWriter<Person> writer() {
		MongoItemWriter<Person> writer = new MongoItemWriter<Person>();
		try {
			logger.info("ItemWriter");
			writer.setTemplate(mongoTemplate());
		} catch (Exception e) {
			logger.debug(e.toString());
			logger.info(e.toString());
		}
		logger.info("Persons");
		writer.setCollection("persons");
		return writer;
	}

	@Bean
	public Job importPerson(JobBuilderFactory jobs, Step s1) {

		return jobs.get("import").incrementer(new RunIdIncrementer()) // because
																		// a
																		// spring
																		// config
																		// bug,
																		// this
																		// incrementer
																		// is
																		// not
																		// really
																		// useful
				.flow(s1).end().build();
	}

	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Person> reader, ItemWriter<Person> writer,
			ItemProcessor<Person, Person> processor) {
		return stepBuilderFactory.get("step1").<Person, Person>chunk(10).reader(reader).processor(processor)
				.writer(writer).build();
	}

	@Bean
	public SimpleMongoClientDatabaseFactory mongoDbFactory() throws Exception {
		return new SimpleMongoClientDatabaseFactory(MongoClients.create(), "batchdb");
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		return mongoTemplate;
	}

	@Bean
	public MongoOperations mongoOperations() throws Exception {
		return new MongoTemplate(mongoDbFactory());
	}

}
