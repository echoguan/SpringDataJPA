package com.perficient.springdata;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//@RepositoryDefinition(domainClass=Person.class,idClass=Integer.class)
public interface PersonRepsotory extends
	JpaRepository<Person, Integer>,
	JpaSpecificationExecutor<Person>, PersonDao{

	//Get Person by lastName
	Person getByLastName(String lastName);

	//WHERE lastName LIKE ?% AND id < ?
	List<Person> getByLastNameStartingWithAndIdLessThan(String lastName, Integer id);

	//WHERE lastName LIKE %? AND id < ?
	List<Person> getByLastNameEndingWithAndIdLessThan(String lastName, Integer id);

	//WHERE email IN (?, ?, ?) OR birth < ?
	List<Person> getByEmailInAndBirthLessThan(List<String> emails, Date birth);

	//WHERE a.id > ?
	List<Person> getByAddress_IdGreaterThan(Integer id);

	@Query("SELECT p FROM Person p WHERE p.id = (SELECT max(p2.id) FROM Person p2)")
	Person getMaxIdPerson();

	@Query("SELECT p FROM Person p WHERE p.lastName = ?1 AND p.email = ?2")
	List<Person> testQueryAnnotationParams1(String lastName, String email);

	@Query("SELECT p FROM Person p WHERE p.lastName = :lastName AND p.email = :email")
	List<Person> testQueryAnnotationParams2(@Param("email") String email, @Param("lastName") String lastName);

	@Query("SELECT p FROM Person p WHERE p.lastName LIKE %?1% OR p.email LIKE %?2%")
	List<Person> testQueryAnnotationLikeParam(String lastName, String email);

	@Query("SELECT p FROM Person p WHERE p.lastName LIKE %:lastName% OR p.email LIKE %:email%")
	List<Person> testQueryAnnotationLikeParam2(@Param("email") String email, @Param("lastName") String lastName);

	@Query(value="SELECT count(id) FROM jpa_persons", nativeQuery=true)
	long getTotalCount();

	@Modifying
	@Query("UPDATE Person p SET p.email = :email WHERE id = :id")
	void updatePersonEmail(@Param("id") Integer id, @Param("email") String email);

	@Query(nativeQuery = true)
    List<Person> findByLastNameIsNative();

    List<Person> findByLastNameIs();
}
 