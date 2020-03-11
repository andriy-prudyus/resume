package com.andriiprudyus.database

import com.andriiprudyus.database.responsibility.DbResponsibility
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ResponsibilityDaoTest : BaseDaoTest() {

    @Test
    fun select() {

        fun areEqual(expected: List<DbResponsibility>, actual: List<DbResponsibility>): Boolean {
            if (expected.size != actual.size) {
                return false
            }

            actual.forEach { a ->
                if (expected.find { b ->
                        a.companyName == b.companyName
                                && a.roleName == b.roleName
                                && a.responsibilityName == b.responsibilityName
                    } == null
                ) {
                    return false
                }
            }

            return true
        }

        val companyName = "eMagicOne"

        val expected = listOf(
            DbResponsibility(
                companyName,
                "Android Developer / Team Lead",
                "Developing mobile apps for Android"
            ),
            DbResponsibility(
                companyName,
                "Android Developer / Team Lead",
                "Creating sprints and tasks"
            ),
            DbResponsibility(companyName, "Android Developer / Team Lead", "Estimating deadlines"),
            DbResponsibility(
                companyName,
                "Android Developer / Team Lead",
                "Publishing apps on PlayStore"
            ),
            DbResponsibility(
                companyName,
                "Android Developer",
                "Developing mobile apps for Android"
            ),
            DbResponsibility(companyName, "Android Developer", "Writing tests")
        )

        runBlocking {
            assert(areEqual(expected, db.responsibilityDao().select(companyName)))
        }
    }
}