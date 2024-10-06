package amrk000.skyai.presentation.view.activity

import amrk000.TestRunner
import amrk000.skyai.R
import amrk000.skyai.presentation.di.component.SkyAiApplication
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.base.IdlingResourceRegistry
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.CustomTestApplication
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AboutActivityTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(AboutActivity::class.java)

    @Before
    fun setUp(){
        hiltRule.inject()

    }

    @Test
    fun myAppsButton(){
        Thread.sleep(2000)
        onView(withId(R.id.moreapps))
            .perform(click())
    }

    @Test
    fun myGitRepoButton(){
        Thread.sleep(2000)
        onView(withId(R.id.about_appRepoButton))
            .perform(click())
    }
}