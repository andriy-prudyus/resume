package com.andriiprudyus.myresume.ui.company.list.view

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.andriiprudyus.database.company.Company
import com.andriiprudyus.myresume.R
import com.andriiprudyus.myresume.base.viewModel.State
import com.andriiprudyus.myresume.testUtils.RecyclerViewInteraction
import com.andriiprudyus.myresume.testUtils.SwipeRefreshLayoutMatchers.isRefreshing
import com.andriiprudyus.myresume.ui.company.list.adapter.CompaniesAdapter
import com.andriiprudyus.myresume.ui.company.list.viewModel.CompanyListViewModel
import com.andriiprudyus.myresume.ui.company.list.viewModel.CompanyListViewModelFactory
import com.andriiprudyus.utils.formattedDate
import org.hamcrest.Matchers.allOf
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

    @Test
    fun checkCompanyList() {
        val companies = listOf(
            Company(
                "eMagicOne",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Experienced mobile application developer",
                1503014400000,
                1577750400000,
                "Android Developer / Team Lead"
            ),
            Company(
                "Google",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Mobile application developer",
                1503014400000,
                1577750400000,
                "Android Developer"
            ),
            Company(
                "Amazon",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Mob application developer",
                1503014400000,
                1577750400000,
                "Junior Android Developer"
            ),
            Company(
                "HP",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Tested apps",
                1503014400000,
                1577750400000,
                "QA Engineer"
            ),
            Company(
                "Dell",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Tested app",
                1503014400000,
                1577750400000,
                "Middle QA Engineer"
            ),
            Company(
                "Canon",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Tested good apps",
                1503014400000,
                1577750400000,
                "Junior QA Engineer"
            ),
            Company(
                "UPS",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Tested good applications",
                1503014400000,
                1577750400000,
                "Trainee QA Engineer"
            ),
            Company(
                "Canada Post",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Delivered letter",
                1503014400000,
                1577750400000,
                "Courier"
            ),
            Company(
                "Atimi",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Tested good apps",
                1503014400000,
                1577750400000,
                "Senior QA Engineer"
            ),
            Company(
                "UPS",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Tested good applications",
                1503014400000,
                1577750400000,
                "Trainee QA Engineer"
            ),
            Company(
                "Tetra",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Tested good applications",
                1503014400000,
                1577750400000,
                "Trainee QA Engineer"
            ),
            Company(
                "eMagicOne1",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Experienced mobile application developer1",
                1503014400000,
                1577750400000,
                "Android Developer / Team Lead1"
            ),
            Company(
                "Google1",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Mobile application developer1",
                1503014400000,
                1577750400000,
                "Android Developer"
            ),
            Company(
                "Amazon1",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Mob application developer1",
                1503014400000,
                1577750400000,
                "Junior Android Developer1"
            ),
            Company(
                "HP1",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Tested apps1",
                1503014400000,
                1577750400000,
                "QA Engineer1"
            ),
            Company(
                "Dell1",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Tested app1",
                1503014400000,
                1577750400000,
                "Middle QA Engineer1"
            ),
            Company(
                "Canon1",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Tested good apps1",
                1503014400000,
                1577750400000,
                "Junior QA Engineer1"
            ),
            Company(
                "UPS1",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Tested good applications1",
                1503014400000,
                1577750400000,
                "Trainee QA Engineer1"
            ),
            Company(
                "Canada Post1",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Delivered letter1",
                1503014400000,
                1577750400000,
                "Courier1"
            ),
            Company(
                "Atimi1",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Tested good apps1",
                1503014400000,
                1577750400000,
                "Senior QA Engineer1"
            ),
            Company(
                "UPS1",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Tested good applications1",
                1503014400000,
                1577750400000,
                "Trainee QA Engineer1"
            ),
            Company(
                "Tetra1",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                "Tested good applications1",
                1503014400000,
                1577750400000,
                "Trainee QA Engineer1"
            )
        )

        `when`(mockViewModel.companyList())
            .thenReturn(
                MutableLiveData<State<List<Company>>>(State.Success(companies))
            )

        launchFragmentInContainer<CompanyListFragment>(null, R.style.AppTheme, fragmentFactory)

        RecyclerViewInteraction
            .onRecyclerView<Company, CompaniesAdapter.ItemViewHolder>(withId(R.id.recyclerView))
            .withItems(companies)
            .check(object : RecyclerViewInteraction.ItemViewAssertion<Company> {
                override fun check(item: Company, view: View, e: NoMatchingViewException?) {
                    matches(
                        allOf(
                            hasDescendant(
                                allOf(
                                    withId(R.id.companyNameTextView),
                                    withText(item.companyName),
                                    isCompletelyDisplayed()
                                )
                            ),
                            hasDescendant(
                                allOf(
                                    withId(R.id.roleName),
                                    withText(item.roleName),
                                    isCompletelyDisplayed()
                                )
                            ),
                            hasDescendant(
                                allOf(
                                    withId(R.id.durationTextView),
                                    withText(
                                        "%s - %s".format(
                                            formattedDate(item.startedAt!!),
                                            formattedDate(item.endedAt!!)
                                        )
                                    ),
                                    isCompletelyDisplayed()
                                )
                            ),
                            hasDescendant(
                                allOf(
                                    withId(R.id.logoImageView),
                                    isCompletelyDisplayed()
                                )
                            )
                        )
                    )
                        .check(view, e)
                }
            })
    }
}