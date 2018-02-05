package com.task.twinotask.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Role(
	val name: String,

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	val id: Long = 0
)
