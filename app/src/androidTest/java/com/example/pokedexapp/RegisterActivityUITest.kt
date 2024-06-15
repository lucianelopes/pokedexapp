package com.example.pokedexapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed

@RunWith(AndroidJUnit4::class)
class RegisterActivityUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun registerButtonClicked_createsNewUser() {
        onView(withId(R.id.editTextUsername)).perform(typeText("newuser"))
        onView(withId(R.id.editTextPassword)).perform(typeText("password"))
        onView(withId(R.id.buttonRegister)).perform(click())

        // Check if the RegisterActivity is closed and we are back to LoginActivity
        onView(withId(R.id.buttonLogin)).check(matches(isDisplayed()))
    }
}
