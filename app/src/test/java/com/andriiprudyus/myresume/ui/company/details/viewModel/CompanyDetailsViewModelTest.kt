package com.andriiprudyus.myresume.ui.company.details.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andriiprudyus.database.achievement.DbAchievement
import com.andriiprudyus.database.responsibility.DbResponsibility
import com.andriiprudyus.database.role.DbRole
import com.andriiprudyus.myresume.testUtils.RxImmediateSchedulerRule
import com.andriiprudyus.myresume.ui.company.details.adapter.RolesAdapter
import com.andriiprudyus.myresume.ui.company.details.repository.CompanyDetailsRepository
import com.google.common.truth.Truth.assertThat
import io.reactivex.Single
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CompanyDetailsViewModelTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulerRule = RxImmediateSchedulerRule()

        private const val COMPANY_NAME = "eMagicOne"
    }

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: CompanyDetailsViewModel

    @Mock
    private lateinit var mockRepository: CompanyDetailsRepository

    @Before
    fun init() {
        viewModel = CompanyDetailsViewModel(mockRepository).apply {
            companyName = COMPANY_NAME
        }
    }

    @Test
    fun getItems() {
        val summary = "Developed good mobile applications"

        val roles = listOf(
            DbRole("Android Developer / Team Lead", COMPANY_NAME, 1503014400000, 1577750400000),
            DbRole("Android Developer", COMPANY_NAME, 1503014400000, 1546214400000)
        )

        val responsibilities = listOf(
            DbResponsibility(
                COMPANY_NAME,
                "Android Developer / Team Lead",
                "Developing mobile apps for Android"
            ),
            DbResponsibility(
                COMPANY_NAME,
                "Android Developer / Team Lead",
                "Creating sprints and tasks"
            ),
            DbResponsibility(
                COMPANY_NAME,
                "Android Developer",
                "Developing mobile apps for Android"
            ),
            DbResponsibility(
                COMPANY_NAME,
                "Android Developer",
                "Writing tests"
            )
        )

        val achievements = listOf(
            DbAchievement(
                COMPANY_NAME,
                "Android Developer / Team Lead",
                "Published Mobile Assistant for PrestaShop"
            ),
            DbAchievement(
                COMPANY_NAME,
                "Android Developer / Team Lead",
                "Published Mobile Assistant for Magento"
            ),
            DbAchievement(
                COMPANY_NAME,
                "Android Developer",
                "Developed Mobile Assistant for PrestaShop"
            ),
            DbAchievement(
                COMPANY_NAME,
                "Android Developer",
                "Developed Mobile Assistant for Magento"
            )
        )

        val expected = listOf(
            RolesAdapter.Item.Summary(summary),
            RolesAdapter.Item.Role(
                DbRole(
                    "Android Developer / Team Lead",
                    COMPANY_NAME,
                    1503014400000,
                    1577750400000
                )
            ),
            RolesAdapter.Item.Responsibility("Developing mobile apps for Android"),
            RolesAdapter.Item.Responsibility("Creating sprints and tasks"),
            RolesAdapter.Item.Achievement("Published Mobile Assistant for PrestaShop"),
            RolesAdapter.Item.Achievement("Published Mobile Assistant for Magento"),
            RolesAdapter.Item.Role(
                DbRole(
                    "Android Developer",
                    COMPANY_NAME,
                    1503014400000,
                    1546214400000
                )
            ),
            RolesAdapter.Item.Responsibility("Developing mobile apps for Android"),
            RolesAdapter.Item.Responsibility("Writing tests"),
            RolesAdapter.Item.Achievement("Developed Mobile Assistant for PrestaShop"),
            RolesAdapter.Item.Achievement("Developed Mobile Assistant for Magento")
        )

        `when`(mockRepository.loadSummary(COMPANY_NAME)).thenReturn(Single.just(summary))
        `when`(mockRepository.loadRoles(COMPANY_NAME)).thenReturn(Single.just(roles))
        `when`(mockRepository.loadResponsibilities(COMPANY_NAME)).thenReturn(
            Single.just(responsibilities)
        )
        `when`(mockRepository.loadAchievements(COMPANY_NAME)).thenReturn(Single.just(achievements))

        viewModel.getItems().observeForever { state ->
            assertThat(state.data).containsExactlyElementsIn(expected).inOrder()
        }
    }
}