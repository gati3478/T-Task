package com.task.twinotask.entity

import java.sql.Date
import javax.persistence.*

@Suppress("unused")
@Entity
@Table(name = "client")
data class Client(

	@Column(name = "first_name", nullable = false)
	val firstName: String = "",

	@Column(name = "last_name", nullable = false)
	val lastName: String = "",

	@Column(unique = true, nullable = false)
	val email: String = "",

	@Column(name = "password", nullable = false)
	val password: String = "",

	@Column(name = "phone_number")
	val phoneNumber: String? = "",

	@Column(name = "birth_date", nullable = false)
	val birthDate: Date? = null,

	@Column(name = "monthly_salary", nullable = false)
	val salary: Int = 0,

	@Column(name = "debt")
	val liabilities: Int = 0,

	@ManyToMany(fetch = FetchType.EAGER, cascade = [(CascadeType.ALL)])
	@JoinTable(
		name = "users_roles",
		joinColumns = [(JoinColumn(name = "user_id", referencedColumnName = "id"))],
		inverseJoinColumns = [(JoinColumn(name = "role_id", referencedColumnName = "id"))]
	)
	val roles: Collection<Role>? = null,

	@Enumerated(EnumType.STRING)
	@Column(name = "profile_status", nullable = false)
	val visibility: ProfileVisibility? = ProfileVisibility.REGISTERED,

	@Column(name = "date_adjusted")
	private var dateAdjusted: Boolean = false,

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	val id: Long? = 0
) {
	fun dateIsAdjusted(): Boolean = dateAdjusted

	fun setDateAdjusted() {
		dateAdjusted = true
	}
}
