package com.example.pokedexapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun loginButtonClicked_navigatesToMainActivity() {
        onView(withId(R.id.editTextUsername)).perform(typeText("rhodie"))
        onView(withId(R.id.editTextPassword)).perform(typeText("123456"))
        onView(withId(R.id.buttonLogin)).perform(click())

        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }
}
