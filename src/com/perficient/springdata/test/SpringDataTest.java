package com.perficient.springdata.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import com.perficient.springdata.Person;
import com.perficient.springdata.PersonService;
import com.perficient.springdata.commonrepositorymethod.AddressRepository;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

import com.perficient.springdata.PersonRepository;

public class SpringDataTest {

	private ApplicationContext ctx = null;
	private PersonRepository personRepository = null;
	private PersonService personService;
	
	{
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		personRepository = ctx.getBean(PersonRepository.class);
		personService = ctx.getBean(PersonService.class);
	}

	@Test
	public void testNamedQueries(){
		List<Person> persons1 = personRepository.findByLastNameIs();
		List<Person> persons2 = personRepository.findByLastNameIsNative();
		System.out.println(persons1);
		System.out.println(persons2);
	}
	
	@Test
	public void testCommonCustomRepositoryMethod(){
		ApplicationContext ctx2 = new ClassPathXmlApplicationContext("classpath:com/perficient/springdata/commonrepositorymethod/applicationContext2.xml");
		AddressRepository addressRepository = ctx2.getBean(AddressRepository.class);
		addressRepository.method();
	}
	
	@Test
	public void testCustomRepositoryMethod(){
		personRepository.test();
	}
		
	/**
	 * Invoke JpaSpecificationExecutor's Page<T> findAll(Specification<T> spec, Pageable pageable);
	 * Specification: Encapsulates query conditions for JPA Criteria queries.
	 */
	@Test
	public void testJpaSpecificationExecutor(){
		int pageNo = 3 - 1;
		int pageSize = 5;
		PageRequest pageable = new PageRequest(pageNo, pageSize);
		
		//Use Specification's Anonymous internal classes usually.
		Specification<Person> specification = new Specification<Person>() {
			/**
			 * @param *root: entity class.
			 * @param query:
			 * @param *cb: CriteriaBuilder Object; Get Predicate Object from here.
			 * @return: *Predicate. Represents a query condition.
			 */
			@Override
			public Predicate toPredicate(Root<Person> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path path = root.get("id");
				Predicate predicate = cb.gt(path, 5);//Where id > 5
				return predicate;
			}
		};
		
		Page<Person> page = personRepository.findAll(specification, pageable);
		
		System.out.println("Total number of records: " + page.getTotalElements());
		System.out.println("Current page: " + (page.getNumber() + 1));
		System.out.println("Total number of pages: " + page.getTotalPages());
		System.out.println("Current page's data: " + page.getContent());
		System.out.println("Total number of current page: " + page.getNumberOfElements());
	}
	
	@Test
	public void testJpaRepository(){
		Person person = new Person();
		person.setBirth(new Date());
		person.setEmail("xy@163.com");
		person.setLastName("xyz");
		person.setId(28);
		
		Person person2 = personRepository.saveAndFlush(person);

		System.out.println(person == person2);
	}
	
	@Test
	public void testPagingAndSortingRepository(){
		//pageNo is start with 0
		int pageNo = 6 - 1;
		int pageSize = 5;

		Order order1 = new Order(Direction.DESC, "id");
		Order order2 = new Order(Direction.ASC, "email");
		Sort sort = new Sort(order1, order2);

		//Using PageRequest class in Pageable interface usually.
		PageRequest pageable = new PageRequest(pageNo, pageSize, sort);
		Page<Person> page = personRepository.findAll(pageable);
		
		System.out.println("Total number of records: " + page.getTotalElements());
		System.out.println("Current page: " + (page.getNumber() + 1));
		System.out.println("Total number of pages: " + page.getTotalPages());
		System.out.println("Current page's data: " + page.getContent());
		System.out.println("Total number of current page: " + page.getNumberOfElements());
	}
	
	@Test
	public void testCrudReposiory(){
		List<Person> persons = new ArrayList<>();
		
		for(int i = 'a'; i <= 'z'; i++){
			Person person = new Person();
			person.setAddressId(i + 1);
			person.setBirth(new Date());
			person.setEmail((char)i + "" + (char)i + "@163.com");
			person.setLastName((char)i + "" + (char)i);
			
			persons.add(person);
		}
		
		personService.savePersons(persons);
	}
	
	@Test
	public void testModifying(){
//		personRepository.updatePersonEmail(1, "mmmm@163.com");
		personService.updatePersonEmail("mmmm@163.com", 1);
	}
	
	@Test
	public void testNativeQuery(){
		long count = personRepository.getTotalCount();
		System.out.println(count);
	}
	
	@Test
	public void testQueryAnnotationLikeParam(){
//		List<Person> persons = personRepository.testQueryAnnotationLikeParam("%A%", "%bb%");
//		System.out.println(persons.size());
		
//		List<Person> persons = personRepository.testQueryAnnotationLikeParam("A", "bb");
//		System.out.println(persons.size());
		
		List<Person> persons = personRepository.testQueryAnnotationLikeParam2("bb", "A");
		System.out.println(persons.size());
	}
	
	@Test
	public void testQueryAnnotationParams2(){
		List<Person> persons = personRepository.testQueryAnnotationParams2("aa@163.com", "AA");
		System.out.println(persons);
	}
	
	@Test
	public void testQueryAnnotationParams1(){
		List<Person> persons = personRepository.testQueryAnnotationParams1("AA", "aa@163.com");
		System.out.println(persons);
	}
	
	@Test
	public void testQueryAnnotation(){
		Person person = personRepository.getMaxIdPerson();
		System.out.println(person);
	}
	
	@Test
	public void testKeyWords2(){
		List<Person> persons = personRepository.getByAddress_IdGreaterThan(1);
		System.out.println(persons);
	}
	
	@Test
	public void testKeyWords(){
		List<Person> persons = personRepository.getByLastNameStartingWithAndIdLessThan("X", 10);
		System.out.println(persons);
		
		persons = personRepository.getByLastNameEndingWithAndIdLessThan("X", 10);
		System.out.println(persons);
		
		persons = personRepository.getByEmailInAndBirthLessThan(Arrays.asList("AA@163.com", "FF@163.com",
				"SS@163.com"), new Date());
		System.out.println(persons.size());
	}
	
	@Test
	public void testHelloWorldSpringData() throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException{
		System.out.println(personRepository.getClass().getName());
		
		Person person = personRepository.getByLastName("AA");
		System.out.println(person);
	}
	
	@Test
	public void testJpa(){
		
	}
	
	@Test
	public void testDataSource() throws SQLException {
		DataSource dataSource = ctx.getBean(DataSource.class);
		System.out.println(dataSource.getConnection());
	}

}
