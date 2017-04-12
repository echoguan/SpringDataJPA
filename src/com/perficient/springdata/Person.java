package com.perficient.springdata;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="JPA_PERSONS")
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "lastName", nullable = false)
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "birth")
	private Date birth;

	@Column(name="ADDR_ID")
	private Integer addressId;

	@JoinColumn(name="ADDRESS_ID")
	@ManyToOne
	private Address address;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}
	
	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", lastName=" + lastName + ", email="
				+ email + ", brith=" + birth + "]";
	}
}
