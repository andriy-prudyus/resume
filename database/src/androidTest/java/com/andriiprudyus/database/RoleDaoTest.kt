package com.andriiprudyus.database

import com.andriiprudyus.database.role.DbRole
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RoleDaoTest : BaseDaoTest() {

    @Test
    fun select() {

        fun areEqual(expected: List<DbRole>, actual: List<DbRole>): Boolean {
            if (expected.size != actual.size) {
                return false
            }

            actual.forEachIndexed { i, a ->
                expected[i].let {
                    if (a.companyName != it.companyName
                        || a.roleName != it.roleName
                        || a.startedAt != it.startedAt
                        || a.endedAt != it.endedAt
                    ) {
                        return false
                    }
                }
            }

            return true
        }

        val companyName = "eMagicOne"

        val expected = listOf(
            DbRole("Android Developer / Team Lead", companyName, 1546300800000, 1577750400000),
            DbRole("Android Developer", companyName, 1503014400000, 1546214400000)
        )

        runBlocking {
            areEqual(expected, db.roleDao().select(companyName))
        }
    }
}