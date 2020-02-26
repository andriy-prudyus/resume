package com.andriiprudyus.myresume.ui.company.list.repository

import com.andriiprudyus.database.DbMediator
import com.andriiprudyus.database.company.Company
import com.andriiprudyus.database.company.CompanyDao
import com.andriiprudyus.myresume.sharedPreferences.CompanySharedPreferences
import com.andriiprudyus.network.CompanyApi
import com.andriiprudyus.network.RestClientMediator
import com.andriiprudyus.network.model.FileDto
import com.andriiprudyus.network.model.GistResponse
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class CompanyListRepositoryTest {

    companion object {
        private val content = """
            [
              {
                "company_name": "eMagicOne",
                "logo_url": "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "summary": "Experienced mobile application developer with a lot of years of experience writing efficient, maintainable and reusable code for Android applications.",
                "roles": [
                  {
                    "role_name": "Android Developer / Team Lead",
                    "started_at": 1546300800000,
                    "ended_at": 1577750400000,
                    "responsibilities": [
                      "Developing mobile apps for Android",
                      "Creating sprints and tasks",
                      "Estimating deadlines",
                      "Publishing apps on PlayStore"
                    ],
                    "achievements": [
                      "Published Mobile Assistant for PrestaShop",
                      "Published Mobile Assistant for WooCommerce",
                      "Published Mobile Assistant for Magento"
                    ]
                  },
                  {
                    "role_name": "Android Developer",
                    "started_at": 1503014400000,
                    "ended_at": 1546214400000,
                    "responsibilities": [
                      "Developing mobile apps for Android",
                      "Writing tests"
                    ],
                    "achievements": [
                      "Developed Mobile Assistant for PrestaShop",
                      "Developed Mobile Assistant for WooCommerce",
                      "Developed Mobile Assistant for Magento"
                    ]
                  }
                ]
              },
              {
                "company_name": "Google",
                "logo_url": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                "summary": "Developed good mobile applications, gained experience",
                "roles": [
                  {
                    "role_name": "Android Developer",
                    "started_at": 1464739200000,
                    "ended_at": 1502928000000,
                    "responsibilities": [
                      "Developing mobile apps using Java",
                      "Refactoring",
                      "Learning new technologies"
                    ],
                    "achievements": [
                      "Learned MVVM",
                      "Learned MVC",
                      "Learned RxJava"
                    ]
                  },
                  {
                    "role_name": "Junior Android Developer",
                    "started_at": 1451606400000,
                    "ended_at": 1464393600000,
                    "responsibilities": [
                      "Developing mobile apps using Java",
                      "Refactoring",
                      "Learning new technologies",
                      "Testing"
                    ],
                    "achievements": [
                      "Learned JUnit",
                      "Learned Espresso",
                      "Learned Android"
                    ]
                  }
                ]
              }
            ]
        """.trimIndent()

        private val companies = listOf(
            Company(
                "eMagicOne",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Developed good mobile application",
                1503014400000,
                1577750400000,
                "Android Developer / Team Lead"
            ),
            Company(
                "Google",
                "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                "Developed good mobile application",
                1451606400000,
                1502928000000,
                "Android Developer"
            )
        )
    }

    private lateinit var repository: CompanyListRepository

    @Mock
    private lateinit var mockRestClientMediator: RestClientMediator

    @Mock
    private lateinit var mockDbMediator: DbMediator

    @Mock
    private lateinit var mockSharedPreferences: CompanySharedPreferences

    @Mock
    private lateinit var mockCalendar: Calendar

    @Mock
    private lateinit var mockCompanyDao: CompanyDao

    @Mock
    private lateinit var mockCompanyApi: CompanyApi

    @Before
    fun init() {
        repository = CompanyListRepository(
            mockRestClientMediator,
            mockDbMediator,
            mockSharedPreferences,
            mockCalendar
        )
    }

    @Test
    fun loadCompanies_cache() {
        `when`(mockSharedPreferences.lastLoadDataTimestamp).thenReturn(1582669538000)
        `when`(mockCalendar.timeInMillis).thenReturn(1582669538100)
        `when`(mockDbMediator.companyDao).thenReturn(mockCompanyDao)
        `when`(mockCompanyDao.selectCompanies()).thenReturn(Single.just(companies))

        repository.loadCompanies()
            .test()
            .assertValue(companies)
            .assertComplete()

        verify(mockCompanyDao).selectCompanies()
        verify(mockCompanyDao, never()).delete()
    }

    @Test
    fun loadCompanies_noCache_success() {
        val response = GistResponse(mapOf("Companies.json" to FileDto(content)))

        `when`(mockSharedPreferences.lastLoadDataTimestamp).thenReturn(1582583137000)
        `when`(mockCalendar.timeInMillis).thenReturn(1582669538100)
        `when`(mockRestClientMediator.companyApi).thenReturn(mockCompanyApi)
        `when`(mockCompanyApi.loadCompanies()).thenReturn(Single.just(Response.success(response)))
        `when`(mockDbMediator.companyDao).thenReturn(mockCompanyDao)
        `when`(mockCompanyDao.delete()).thenReturn(Completable.complete())
        `when`(mockCompanyDao.selectCompanies()).thenReturn(Single.just(companies))

        repository.loadCompanies()
            .test()
            .assertValue(companies)
            .assertComplete()

        verify(mockCompanyApi).loadCompanies()
        verify(mockCompanyDao).delete()
        verify(mockCompanyDao).selectCompanies()
    }

    @Test
    fun loadCompanies_noCache_failure() {
        val response = GistResponse(mapOf("File.json" to FileDto(content)))

        `when`(mockSharedPreferences.lastLoadDataTimestamp).thenReturn(1582583137000)
        `when`(mockCalendar.timeInMillis).thenReturn(1582669538100)
        `when`(mockRestClientMediator.companyApi).thenReturn(mockCompanyApi)
        `when`(mockCompanyApi.loadCompanies()).thenReturn(Single.just(Response.success(response)))

        repository.loadCompanies()
            .test()
            .assertNotComplete()

        verify(mockCompanyApi).loadCompanies()
        verify(mockCompanyDao, never()).delete()
        verify(mockCompanyDao, never()).selectCompanies()
    }

    @Test
    fun refreshCompanies() {
        val spy = spy(repository)
        doReturn(Single.just(companies)).`when`(spy).loadCompanies()

        spy.refreshCompanies()
            .test()
            .assertValue(companies)
    }
}