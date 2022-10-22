package com.example.moneyapp.domain.entities

import java.sql.Timestamp

data class Operation(
    val send: String,
    val receive: String,
    val time: String,
    val sum: Double
    ):Comparable<Operation> {
    override fun compareTo(other: Operation): Int {
        val tm1 = Timestamp.valueOf(correctDateAndTime(other.time))
        val tm2 = Timestamp.valueOf(correctDateAndTime(this.time))

        return tm1.compareTo(tm2)
    }

    private fun correctDateAndTime(time: String): String? {
        return if (time.substring(time.indexOf('.') + 1).length != 3) time + "0" else time
    }

}