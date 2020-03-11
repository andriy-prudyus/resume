package com.andriiprudyus.myresume.ui.company.details.repository

import com.andriiprudyus.database.DbMediator
import com.andriiprudyus.database.achievement.AchievementDao
import com.andriiprudyus.database.achievement.DbAchievement
import com.andriiprudyus.database.company.CompanyDao
import com.andriiprudyus.database.responsibility.DbResponsibility
import com.andriiprudyus.database.responsibility.ResponsibilityDao
import com.andriiprudyus.database.role.DbRole
import com.andriiprudyus.database.role.RoleDao
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CompanyDetailsRepositoryTest {

    companion object {
        private const val COMPANY_NAME = "eMagicOne"
    }

    private lateinit var repository: CompanyDetailsRepository

    @Mock
    private lateinit var mockDbMediator: DbMediator

    @Mock
    private lateinit var mockCompanyDao: CompanyDao

    @Mock
    private lateinit var mockRoleDao: RoleDao

    @Mock
    private lateinit var mockResponsibilityDao: ResponsibilityDao

    @Mock
    private lateinit var mockAchievementDao: AchievementDao

    @Before
    fun init() {
        repository = CompanyDetailsRepository(mockDbMediator)
    }

    @Test
    fun loadSummary() {
        val expected = "Developed good mobile applications"

        runBlocking {
            `when`(mockDbMediator.companyDao).thenReturn(mockCompanyDao)
            `when`(mockCompanyDao.selectSummary(COMPANY_NAME)).thenReturn(expected)

            assertEquals(expected, repository.loadSummary(COMPANY_NAME))
        }
    }

    @Test
    fun loadRoles() {
        val expected = listOf(
            DbRole("Android Developer / Team Lead", COMPANY_NAME, 1503014400000, 1577750400000),
            DbRole("Android Developer", COMPANY_NAME, 1403014400000, 1503014400000)
        )

        runBlocking {
            `when`(mockDbMediator.roleDao).thenReturn(mockRoleDao)
            `when`(mockRoleDao.select(COMPANY_NAME)).thenReturn(expected)

            assertEquals(expected, repository.loadRoles(COMPANY_NAME))
        }
    }

    @Test
    fun loadResponsibilities() {
        val expected = listOf(
            DbResponsibility(
                COMPANY_NAME,
                "Android Developer",
                "Developing mobile apps for Android"
            ),
            DbResponsibility(COMPANY_NAME, "Android Developer", "Writing tests")
        )

        runBlocking {
            `when`(mockDbMediator.responsibilityDao).thenReturn(mockResponsibilityDao)
            `when`(mockResponsibilityDao.select(COMPANY_NAME)).thenReturn(expected)

            assertEquals(expected, repository.loadResponsibilities(COMPANY_NAME))
        }
    }

    @Test
    fun loadAchievements() {
        val expected = listOf(
            DbAchievement(
                COMPANY_NAME,
                "Android Developer",
                "Developed Mobile Assistant for PrestaShop"
            ),
            DbAchievement(
                COMPANY_NAME,
                "Android Developer",
                "Developed Mobile Assistant for WooCommerce"
            ),
            DbAchievement(
                COMPANY_NAME,
                "Android Developer",
                "Developed Mobile Assistant for Magento"
            )
        )

        runBlocking {
            `when`(mockDbMediator.achievementDao).thenReturn(mockAchievementDao)
            `when`(mockAchievementDao.select(COMPANY_NAME)).thenReturn(expected)

            assertEquals(expected, repository.loadAchievements(COMPANY_NAME))
        }
    }
}