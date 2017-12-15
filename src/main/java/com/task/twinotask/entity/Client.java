package com.task.twinotask.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = -3009157732242241606L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(name = "password", length = 60, nullable = false)
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

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Client [id=").append(id).append(", firstName=").append(firstName)
                .append(", lastName=").append(lastName).append(", email=").append(email)
                .append(", password=").append(password).append(", phoneNumber=").append(phoneNumber)
                .append(", birthDate=").append(birthDate).append(", dateAdjusted").append(dateAdjusted)
                .append(", salary=").append(salary).append(", liabilities").append(liabilities)
                .append(", visibility").append(visibility).append("]");
        return builder.toString();
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
