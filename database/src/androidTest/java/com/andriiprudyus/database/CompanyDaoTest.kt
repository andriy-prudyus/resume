package com.andriiprudyus.database

import com.andriiprudyus.database.company.Company
import org.junit.Test

class CompanyDaoTest : BaseDaoTest() {

    @Test
    fun selectCompanies() {
        val companies = listOf(
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

        db.companyDao().selectCompanies()
            .test()
            .assertValue(companies)
            .assertComplete()
    }

    @Test
    fun selectSummary() {
        val companyName = "Google"
        val summary = "Developed good mobile application, gained experience"

        db.companyDao().selectSummary(companyName)
            .test()
            .assertValue(summary)
            .assertComplete()
    }

    @Test
    fun delete() {
        db.companyDao().delete()
            .toSingleDefault(Any())
            .flatMap {
                db.companyDao().selectCompanies()
            }
            .test()
            .assertValue(emptyList())
            .assertComplete()
    }
}