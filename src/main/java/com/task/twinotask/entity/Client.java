package com.task.twinotask.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;

@SuppressWarnings("unused")
@Entity
@Table(name = "client")
public class Client implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "birth_date", nullable = false)
	private Date birthDate;

	@Column(name = "date_adjusted")
	private Boolean dateAdjusted = false;

	@Column(name = "monthly_salary", nullable = false)
	private Integer salary;

	@Column(name = "debt")
	private Integer liabilities;

	@Enumerated(EnumType.STRING)
	@Column(name = "profile_status", nullable = false)
	private ProfileVisibility visibility;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(
					name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(
					name = "role_id", referencedColumnName = "id"))
	private Collection<Role> roles;

	public Client() {
		super();
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String username) {
		this.email = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Boolean dateIsAdjusted() {
		return dateAdjusted;
	}

	public void setDateAdjusted() {
		this.dateAdjusted = true;
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

	public ProfileVisibility getVisibility() {
		return visibility;
	}

	public void setVisibility(ProfileVisibility visibility) {
		this.visibility = visibility;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	@Override
	public int hashCode() {
		return email.hashCode();
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", firstName=" + firstName +
				", lastName=" + lastName + ", email=" + email +
				", password=" + password + ", phoneNumber=" + phoneNumber +
				", birthDate=" + birthDate + ", dateAdjusted=" + dateAdjusted +
				", salary=" + salary + ", liabilities=" + liabilities +
				", visibility=" + visibility + "]";
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Client other = (Client) obj;

		return email.equals(other.email);
	}

}
