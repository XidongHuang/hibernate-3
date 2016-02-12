package com.tony.hibernate.n2n;

import java.util.Set;

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

	@Test
	public void testGet(){
		Category category = session.get(Category.class, 1);
		System.out.println(category.getName());
		
		Set<Item> items = category.getItems();
		
		System.out.println(items.size());
		
	}
	
	
	@Test
	public void testSave(){
		Category category1 = new Category();
		category1.setName("C-AA");
		
		Category category2 = new Category();
		category2.setName("C-BB");
		
		Item item1 = new Item();
		item1.setName("I-AA");
		Item item2 = new Item();
		item2.setName("I-BB");
		
		
		//Set up relation
		category1.getItems().add(item1);
		category1.getItems().add(item2);
		
		category2.getItems().add(item1);
		category2.getItems().add(item2);
		
		item1.getCategories().add(category1);
		item1.getCategories().add(category2);
		
		item2.getCategories().add(category1);
		item2.getCategories().add(category2);
		
		//Save
		session.save(category1);
		session.save(category2);
		
		session.save(item1);
		session.save(item2);
		
	}
	
	
	

}
