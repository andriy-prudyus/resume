package com.andriiprudyus.myresume.db

import com.andriiprudyus.myresume.db.role.Role
import org.junit.Test

class RoleDaoTest : BaseDaoTest() {

    @Test
    fun select() {
        val companyName = "eMagicOne"

        val roles = listOf(
            Role("Android Developer / Team Lead", 1546300800000, 1577750400000),
            Role("Android Developer", 1503014400000, 1546214400000)
        )

        db.roleDao().select(companyName)
            .test()
            .assertValue(roles)
            .assertComplete()
    }
}