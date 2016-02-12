package com.tony.hibernate.joined.subclass;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HibernateTest {

	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transation;

	@Before
	public void init() {

		Configuration configuration = new Configuration().configure();

		StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml")
				.build();
		Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();

		sessionFactory = metadata.getSessionFactoryBuilder().build();

		session = sessionFactory.openSession();
		transation = session.beginTransaction();
	}

	@After
	public void destory() {

		transation.commit();
		session.close();
		sessionFactory.close();

	}
	
	/**
	 * Disadvantages:
	 * 1. Discriminator
	 * 2. Subclass's unique attribute names cannot be constrained by unnull 
	 * 3. If the layers of inherit are too many, the attribute names in table will be more
	 * 
	 */
	
	
	/**
	 * 
	 * Query:
	 * 1. Query superclass records , just need one data table
	 * 2. Query subclass records, also just need one data table
	 */
	@Test
	public void testQuery(){
		List<Person> persons = session.createQuery("FROM Person").list();
		System.out.println(persons.size());
		
		List<Student> students = session.createQuery("FROM Student").list();
		System.out.println(students.size());
		
	}
	
	
	
	/**
	 * INSERT:
	 * 1. For subclass object, just need to insert records to a data table
	 * 2. Discriminator column is maintenance by Hibernate automatically. 
	 * 
	 * 
	 */
	
	@Test
	public void testSave(){
		Person person = new Person();
		person.setAge(11);
		person.setName("AA");
		session.save(person);
		
		Student student = new Student();
		student.setAge(22);
		student.setName("BB");
		student.setSchool("Tony");
		
		session.save(student);
		
	}
	
	
	

}
