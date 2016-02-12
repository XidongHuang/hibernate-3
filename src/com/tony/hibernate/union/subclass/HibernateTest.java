package com.tony.hibernate.union.subclass;

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
	 * Advantages:
	 * 1. Do not need discriminator
	 * 2. The subclass unique attributes can be constrained by unnull 
	 * 3. No extra attributes
	 * 
	 */
	
	
	/**
	 * 
	 * Query:
	 * 1. Query superclass records , need a left outer join
	 * 2. Query subclass records, need a inner join
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
	 * 1. For inserting subclass objects, it needs to insert to two tables
	 * 
	 * 
	 */
	
	@Test
	public void testSave(){
		Person person = new Person();
		person.setAge(11);
		person.setName("AA");
		try {
			session.save(person);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Student student = new Student();
		student.setAge(22);
		student.setName("BB");
		student.setSchool("Tony");
		
		session.save(student);
		
	}
	
	
	

}
