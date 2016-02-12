package com.tony.hibernate.one2one.foreign;

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
	public void testGet2(){
		//When querying the object without foreign key, 
		//it will use left outer join to query all relative objects.
		//And initial them.
		Manager mgr = session.get(Manager.class, 1);
		
		System.out.println(mgr.getMgrName());
		System.out.println(mgr);
		System.out.println(mgr.getDept().getDeptName());
	}
	
	
	@Test
	public void testGet(){
		
		//1. In default, it will be lazy loading for relative attributes
		//2. It may have LazyInitializationException: session.close()
		Department department = session.get(Department.class, 1);
		System.out.println(department);
		
		
		
		//3. The connection requirement of querying Manager object should be
		// dept.manager_id = mgr.manager_id
		// instead of dept.dept_id = mgr.manager_id
		
		Manager mgr = department.getMgr();
		System.out.println(mgr.getMgrName());
		
	}
	
	
	@Test
	public void testSave(){
		Department department = new Department();
		department.setDeptName("DEPT-BB");
		
		Manager manager = new Manager();
		manager.setMgrName("MGR-BB");
		
		//Setting instance relation
		department.setMgr(manager);
		manager.setDept(department);
		
		
		//Tips:
		//Store the object does not have foreign key
		session.save(department);
		session.save(manager);
		
	}
	
	
	

}
