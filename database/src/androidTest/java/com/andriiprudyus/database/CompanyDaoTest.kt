package com.andriiprudyus.database

import com.andriiprudyus.database.company.Company
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CompanyDaoTest : BaseDaoTest() {

    @Test
    fun selectCompanies() {
        val expected = listOf(
            Company(
                "eMagicOne",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Experienced mobile application developer with a lot of years of experience writing efficient, maintainable and reusable code for Android applications.",
                1503014400000,
                1577750400000,
                "Android Developer / Team Lead"
            ),
            Company(
                "Google",
                "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                "Developed good mobile application, gained experience",
                1451606400000,
                1502928000000,
                "Android Developer"
            )
        )

        runBlocking {
            assertEquals(expected, db.companyDao().selectCompanies())
        }
    }

    @Test
    fun selectSummary() {
        val companyName = "Google"
        val expected = "Developed good mobile application, gained experience"

        runBlocking {
            assertEquals(expected, db.companyDao().selectSummary(companyName))
        }
    }

    @Test
    fun delete() {
        runBlocking {
            db.companyDao().delete()
            assert(db.companyDao().selectCompanies().isEmpty())
        }
    }
}