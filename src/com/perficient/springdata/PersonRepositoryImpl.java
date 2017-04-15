package com.perficient.springdata;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class PersonRepositoryImpl implements CustomPersonRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void test() {
		Person person = entityManager.find(Person.class, 11);
		System.out.println("-->" + person);
	}

}
