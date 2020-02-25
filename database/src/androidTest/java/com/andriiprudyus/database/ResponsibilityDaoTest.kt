package com.andriiprudyus.database

import com.andriiprudyus.database.responsibility.Responsibility
import org.junit.Test

class ResponsibilityDaoTest : BaseDaoTest() {

    @Test
    fun select() {

        val companyName = "eMagicOne"

        val expected = listOf(
            Responsibility("Android Developer / Team Lead", "Developing mobile apps for Android"),
            Responsibility("Android Developer / Team Lead", "Creating sprints and tasks"),
            Responsibility("Android Developer / Team Lead", "Estimating deadlines"),
            Responsibility("Android Developer / Team Lead", "Publishing apps on PlayStore"),
            Responsibility("Android Developer", "Developing mobile apps for Android"),
            Responsibility("Android Developer", "Writing tests")
        )

        db.responsibilityDao().select(companyName)
            .test()
            .assertValue { actual ->
                expected.size == actual.size && expected.containsAll(actual)
            }
            .assertComplete()
    }
}