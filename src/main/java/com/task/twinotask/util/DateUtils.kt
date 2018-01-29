package com.task.twinotask.util

import java.sql.Date
import java.util.*

fun Date.yearsSince() : Int {
	val present = Calendar.getInstance()
	val past = Calendar.getInstance()
	past.time = this

	var years = 0

	while (past.before(present)) {
		past.add(Calendar.YEAR, 1)
		if (past.before(present)) ++years
	}
	return years
}
