package com.andriiprudyus.myresume

import android.view.Gravity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun onNavItemCompaniesClicked() {
        onView(withId(R.id.drawerLayout))
            .check(matches(isClosed(Gravity.START)))
            .perform(DrawerActions.open())

        onView(withId(R.id.navigationView)).perform(NavigationViewActions.navigateTo(R.id.companies))
    }

    @Test
    fun onNavItemAboutClicked() {
        onView(withId(R.id.drawerLayout))
            .check(matches(isClosed(Gravity.START)))
            .perform(DrawerActions.open())

        onView(withId(R.id.navigationView)).perform(NavigationViewActions.navigateTo(R.id.about))
    }
}