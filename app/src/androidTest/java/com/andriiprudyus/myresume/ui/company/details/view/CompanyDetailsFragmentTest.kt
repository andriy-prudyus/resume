package com.andriiprudyus.myresume.ui.company.details.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.andriiprudyus.database.role.DbRole
import com.andriiprudyus.myresume.R
import com.andriiprudyus.myresume.base.viewModel.State
import com.andriiprudyus.myresume.ui.company.details.adapter.RolesAdapter
import com.andriiprudyus.myresume.ui.company.details.viewModel.CompanyDetailsViewModel
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@MediumTest
@RunWith(MockitoJUnitRunner::class)
class CompanyDetailsFragmentTest {

    companion object {
        private const val COMPANY_NAME = "eMagicOne"
    }

    @Mock
    private lateinit var mockViewModel: CompanyDetailsViewModel

    @Mock
    private lateinit var mockViewModelFactory: ViewModelProvider.Factory

    private val fragmentFactory = object : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
            return CompanyDetailsFragment(mockViewModelFactory)
        }
    }

    private val args = Bundle().apply {
        putString("companyName", COMPANY_NAME)
    }

    @Before
    fun setup() {
        `when`(mockViewModelFactory.create(CompanyDetailsViewModel::class.java))
            .thenReturn(mockViewModel)
    }

    @Test
    fun loadData_stateLoading() {
        `when`(mockViewModel.getItems()).thenReturn(MutableLiveData(State.Loading()))

        launchFragmentInContainer<CompanyDetailsFragment>(args, R.style.AppTheme, fragmentFactory)

        onView(withId(R.id.progressBar)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.noDataTextView)).check(matches(not(isDisplayed())))
    }

    @Test
    fun loadData_stateSuccess_emptyList() {
        `when`(mockViewModel.getItems()).thenReturn(MutableLiveData(State.Success(emptyList())))

        launchFragmentInContainer<CompanyDetailsFragment>(args, R.style.AppTheme, fragmentFactory)

        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun loadData_stateSuccess_notEmptyList() {
        `when`(mockViewModel.getItems())
            .thenReturn(
                MutableLiveData(
                    State.Success(
                        listOf(
                            RolesAdapter.Item.Summary("Developed good mobile applications"),
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
                    )
                )
            )

        launchFragmentInContainer<CompanyDetailsFragment>(args, R.style.AppTheme, fragmentFactory)

        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.noDataTextView)).check(matches(not(isDisplayed())))
    }

    @Test
    fun loadData_stateFailure() {
        val message = "Test"

        `when`(mockViewModel.getItems()).thenReturn(MutableLiveData(State.Failure(Exception(message))))

        launchFragmentInContainer<CompanyDetailsFragment>(args, R.style.AppTheme, fragmentFactory)

        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(message)))
    }
}