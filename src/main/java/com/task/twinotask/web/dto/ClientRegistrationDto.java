package com.task.twinotask.web.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.sql.Date;

@SuppressWarnings("unused")
public final class ClientRegistrationDto {

	@NotEmpty
	private String firstName;

	@NotEmpty
	private String lastName;

	@Email
	@NotEmpty
	private String email;

	@NotEmpty
	private String password;

	private String phoneNumber;

	@NotNull
	private Date birthDate;

	@NotNull
	private Integer salary;

	@NotNull
	private Integer liabilities;

	public ClientRegistrationDto() {
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public Integer getLiabilities() {
		return liabilities;
	}

	public void setLiabilities(Integer liabilities) {
		this.liabilities = liabilities;
	}
}