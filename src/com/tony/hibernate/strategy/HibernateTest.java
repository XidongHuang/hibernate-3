package com.tony.hibernate.strategy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
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
	public void testSetFetch2(){
		
		Customer customer = session.get(Customer.class, 1);
		System.out.println(customer.getOrders().size());
		
		
	}
	
	@Test
	public void testMany2OneStrategy(){
		
//		Order order = session.get(Order.class, 1);
//		System.out.println(order.getCustomer().getCustomerName());
		
		List<Order> orders = session.createQuery("FROM Order o").list();
		for(Order order: orders){
			
			if(order.getCustomer() != null){
				System.out.println(order.getCustomer().getCustomerName());
				
			}
			
		}
		
		
		//1. "lazy" is "proxy" and "false", they are delay query and immediately query.
		//2. fetch is "join", it means it will use immediately "left outer join" to initial the "many" side
		//3. batch-size need to be set in "one" side:
		// <class name="Customer" table="CUSTOMERS" lazy="true" batch-size="5">
		// It means the amount number of initialing agent object of "one" side
	}
	
	
	@Test
	public void testFetch() {
		
		List<Customer> customers = session.createQuery("FROM Customer").list();

		System.out.println(customers.size());

		for (Customer customer : customers) {
			if (customer.getOrders() != null) {

				System.out.println(customer.getOrders().size());
			}

		}
		
		//set collection, fetch attribtue: Decide the way of initialing "order" collection
		//1. Default is "select". 
		//2. Can let it be "subselect". Initial it by "subselect" to query all set collection.
		//"subselect" will be shown as "where ... in.." sub-sentence at all "one" side 
		//3. If it is "join", then:
		//3.1 It will use "left outer join" (join and initialing the collection) to query "many" side when loading "one" side objects
		//3.2 Ignore "lazy" attribute.
		//3.3 "HQL" query will ignore "fetch=join" 
		
	}

	@Test
	public void testSetBatchSize() {

		List<Customer> customers = session.createQuery("FROM Customer").list();

		System.out.println(customers.size());

		for (Customer customer : customers) {
			if (customer.getOrders() != null) {

				System.out.println(customer.getOrders().size());
			}

		}

		// set element's batch-size attribute: Set the amount of initial SQL one
		// time.
	}

	@Test
	public void testOne2ManyLevelStrategy() {

		Customer customer = session.get(Customer.class, 1);
		System.out.println(customer.getCustomerName());

		System.out.println(customer.getOrders().size());

		Order order = new Order();
		order.setOrderId(1);
		System.out.println(customer.getOrders().contains(order));

		Hibernate.initialize(customer.getOrders());

		// 1. one-to-many or many-to-many are use lazy loading in default
		// 2. Can modify set's lazy attribute to change default querying
		// strategy
		// Setting it as false is better.
		// 3. Can set lazy to be "extra". Enhance delay querying

	}

	@Test
	public void testClassLevelStrategy() {

		Customer customer = session.load(Customer.class, 1);
		System.out.println(customer.getClass());

		System.out.println(customer.getCustomerId());
		System.out.println(customer.getCustomerName());

	}

}
