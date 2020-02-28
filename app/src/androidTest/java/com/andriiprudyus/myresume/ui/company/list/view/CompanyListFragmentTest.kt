package com.andriiprudyus.myresume.ui.company.list.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.andriiprudyus.database.company.Company
import com.andriiprudyus.myresume.R
import com.andriiprudyus.myresume.base.viewModel.State
import com.andriiprudyus.myresume.testUtils.SwipeRefreshLayoutMatchers.isRefreshing
import com.andriiprudyus.myresume.ui.company.list.adapter.CompaniesAdapter
import com.andriiprudyus.myresume.ui.company.list.viewModel.CompanyListViewModel
import com.andriiprudyus.myresume.ui.company.list.viewModel.CompanyListViewModelFactory
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@MediumTest
@RunWith(MockitoJUnitRunner::class)
class CompanyListFragmentTest {

    @Mock
    private lateinit var mockViewModel: CompanyListViewModel

    @Mock
    private lateinit var mockViewModelFactory: CompanyListViewModelFactory

    private val fragmentFactory = object : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
            return CompanyListFragment(mockViewModelFactory)
        }
    }

    @Before
    fun setup() {
        `when`(mockViewModelFactory.create(CompanyListViewModel::class.java))
            .thenReturn(mockViewModel)
    }

    @Test
    fun loadCompanies_stateLoading() {
        `when`(mockViewModel.companyList())
            .thenReturn(MutableLiveData<State<List<Company>>>(State.Loading()))

        launchFragmentInContainer<CompanyListFragment>(null, R.style.AppTheme, fragmentFactory)

        onView(withId(R.id.progressBar)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.noDataTextView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.swipeRefreshLayout)).check(matches(not(isRefreshing())))
    }

    @Test
    fun loadCompanies_stateSuccess_emptyList() {
        `when`(mockViewModel.companyList())
            .thenReturn(MutableLiveData<State<List<Company>>>(State.Success(listOf())))

        launchFragmentInContainer<CompanyListFragment>(null, R.style.AppTheme, fragmentFactory)

        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.swipeRefreshLayout)).check(matches(not(isRefreshing())))
    }

    @Test
    fun loadCompanies_stateSuccess_notEmptyList() {
        `when`(mockViewModel.companyList())
            .thenReturn(
                MutableLiveData<State<List<Company>>>(
                    State.Success(
                        listOf(
                            Company(
                                "eMagicOne",
                                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                                "Experienced mobile application developer with a lot of years of experience writing efficient code",
                                1503014400000,
                                1577750400000,
                                "Android Developer / Team Lead"
                            )
                        )
                    )
                )
            )

        launchFragmentInContainer<CompanyListFragment>(null, R.style.AppTheme, fragmentFactory)

        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.noDataTextView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.swipeRefreshLayout)).check(matches(not(isRefreshing())))
    }

    @Test
    fun loadCompanies_stateFailure_contentEmpty() {
        `when`(mockViewModel.companyList())
            .thenReturn(MutableLiveData<State<List<Company>>>(State.Failure(Exception("Test"))))

        launchFragmentInContainer<CompanyListFragment>(null, R.style.AppTheme, fragmentFactory)

        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.swipeRefreshLayout)).check(matches(not(isRefreshing())))
    }

    @Test
    fun loadCompanies_stateFailure_contentNotEmpty() {
        val message = "Test"
        val liveData = MutableLiveData<State<List<Company>>>(
            State.Success(
                listOf(
                    Company(
                        "eMagicOne",
                        "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                        "Experienced mobile application developer",
                        1503014400000,
                        1577750400000,
                        "Android Developer / Team Lead"
                    )
                )
            )
        )

        `when`(mockViewModel.companyList()).thenReturn(liveData)

        launchFragmentInContainer<CompanyListFragment>(null, R.style.AppTheme, fragmentFactory)

        liveData.postValue(State.Failure(Exception(message)))

        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.noDataTextView)).check(matches(not(isDisplayed())))
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(message)))
        onView(withId(R.id.swipeRefreshLayout)).check(matches(not(isRefreshing())))
    }

    @Test
    fun swipeRefresh_stateSuccess_contentEmpty() {
        `when`(mockViewModel.companyList())
            .thenReturn(MutableLiveData<State<List<Company>>>(State.Success(emptyList())))
        `when`(mockViewModel.refresh()).thenReturn(MutableLiveData<State<Any>>(State.Success(Any())))

        launchFragmentInContainer<CompanyListFragment>(null, R.style.AppTheme, fragmentFactory)

        onView(withId(R.id.swipeRefreshLayout)).perform(swipeDown())
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.swipeRefreshLayout)).check(matches(not(isRefreshing())))
    }

    @Test
    fun swipeRefresh_stateSuccess_contentNotEmpty() {
        `when`(mockViewModel.companyList())
            .thenReturn(
                MutableLiveData<State<List<Company>>>(
                    State.Success(
                        listOf(
                            Company(
                                "eMagicOne",
                                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                                "Experienced mobile application developer",
                                1503014400000,
                                1577750400000,
                                "Android Developer / Team Lead"
                            )
                        )
                    )
                )
            )
        `when`(mockViewModel.refresh()).thenReturn(MutableLiveData<State<Any>>(State.Success(Any())))

        launchFragmentInContainer<CompanyListFragment>(null, R.style.AppTheme, fragmentFactory)

        onView(withId(R.id.swipeRefreshLayout)).perform(swipeDown())
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.noDataTextView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.swipeRefreshLayout)).check(matches(not(isRefreshing())))
    }

    @Test
    fun swipeRefresh_stateFailure_contentEmpty() {
        val message = "Test"

        `when`(mockViewModel.companyList())
            .thenReturn(MutableLiveData<State<List<Company>>>(State.Success(emptyList())))
        `when`(mockViewModel.refresh())
            .thenReturn(MutableLiveData<State<Any>>(State.Failure(Exception(message))))

        launchFragmentInContainer<CompanyListFragment>(null, R.style.AppTheme, fragmentFactory)

        onView(withId(R.id.swipeRefreshLayout)).perform(swipeDown())
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.swipeRefreshLayout)).check(matches(not(isRefreshing())))
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(message)))
    }

    @Test
    fun swipeRefresh_stateFailure_contentNotEmpty() {
        val message = "Test"

        `when`(mockViewModel.companyList())
            .thenReturn(
                MutableLiveData<State<List<Company>>>(
                    State.Success(
                        listOf(
                            Company(
                                "eMagicOne",
                                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                                "Experienced mobile application developer",
                                1503014400000,
                                1577750400000,
                                "Android Developer / Team Lead"
                            )
                        )
                    )
                )
            )
        `when`(mockViewModel.refresh())
            .thenReturn(MutableLiveData<State<Any>>(State.Failure(Exception(message))))

        launchFragmentInContainer<CompanyListFragment>(null, R.style.AppTheme, fragmentFactory)

        onView(withId(R.id.swipeRefreshLayout)).perform(swipeDown())
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.noDataTextView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.swipeRefreshLayout)).check(matches(not(isRefreshing())))
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(message)))
    }

    @Test
    fun onCompanyClicked() {
        val navController =
            TestNavHostController(ApplicationProvider.getApplicationContext()).apply {
                setGraph(R.navigation.navigation_graph)
            }

        `when`(mockViewModel.companyList())
            .thenReturn(
                MutableLiveData<State<List<Company>>>(
                    State.Success(
                        listOf(
                            Company(
                                "eMagicOne",
                                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                                "Experienced mobile application developer",
                                1503014400000,
                                1577750400000,
                                "Android Developer / Team Lead"
                            )
                        )
                    )
                )
            )

        launchFragmentInContainer<CompanyListFragment>(
            null,
            R.style.AppTheme,
            fragmentFactory
        ).apply {
            onFragment { fragment ->
                Navigation.setViewNavController(fragment.requireView(), navController)
            }
        }

        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CompaniesAdapter.ItemViewHolder>(0, click())
        )

        assert(navController.currentDestination?.id == R.id.companyDetailsFragment)
    }
}