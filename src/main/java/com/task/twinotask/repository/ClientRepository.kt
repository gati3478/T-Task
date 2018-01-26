package com.task.twinotask.repository

import com.task.twinotask.entity.Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : JpaRepository<Client, Long> {
	fun findByEmail(email: String): Client
}
