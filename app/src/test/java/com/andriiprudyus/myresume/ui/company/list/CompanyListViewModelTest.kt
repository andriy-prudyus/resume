package com.andriiprudyus.myresume.ui.company.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andriiprudyus.database.company.Company
import com.andriiprudyus.myresume.base.viewModel.State
import com.andriiprudyus.myresume.testUtils.RxImmediateSchedulerRule
import com.andriiprudyus.myresume.ui.company.list.repository.CompanyListRepository
import com.andriiprudyus.myresume.ui.company.list.viewModel.CompanyListViewModel
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CompanyListViewModelTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulerRule = RxImmediateSchedulerRule()
    }

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: CompanyListViewModel

    @Mock
    private lateinit var mockRepository: CompanyListRepository

    @Before
    fun init() {
        viewModel = CompanyListViewModel(mockRepository)
    }

    @Test
    fun companyList() {
        val expected = listOf(
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

        `when`(mockRepository.loadCompanies()).thenReturn(Single.just(expected))

        viewModel.companyList().observeForever {
            assertEquals(expected, it.data)
        }
    }

    @Test
    fun refresh() {
        `when`(mockRepository.refreshCompanies()).thenReturn(Single.just(listOf()))

        viewModel.refresh().observeForever {
            assert(it is State.Success)
        }
    }
}