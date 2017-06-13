package engineertest.android.touche.com.paytouch.ui.activities

import android.support.test.rule.ActivityTestRule
import org.junit.*

import org.junit.Assert.*
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.action.ViewActions.typeText
import engineertest.android.touche.com.paytouch.R

/**
 * Created by archie on 1/6/17.
 */

class LandingActivityTest {

    @get:Rule
    open val activityRule = ActivityTestRule(LandingActivity::class.java)

    private lateinit var activity: LandingActivity

    @Before
    fun setUp() {
        activity = activityRule.activity
    }

    @After
    fun tearDown() {

    }


    @Test
    fun basicLandingActivityTest() {
        testLoaded();
        testSortedByName();
        testSortedByPopularity();
        testSearch();
    }



    fun testLoaded() {
        Thread.sleep(8000)
        val loaded = !activity.viewModel.loadingState
        assertTrue(loaded)
    }

    fun testSortedByName() {
        activity.runOnUiThread {
            activity.menuViewModel.sortType = 0
        }
        Thread.sleep(1000)
        assertNotNull(activity.viewModel.adapter)
        val actor = activity.viewModel.adapter?.getActor(0)
        assertNotNull(actor)
        assertEquals(actor?.name, "Ben Affleck")
    }

    fun testSortedByPopularity() {
        activity.runOnUiThread {
            activity.menuViewModel.sortType = 1
        }
        Thread.sleep(1000)
        assertNotNull(activity.viewModel.adapter)
        val actor = activity.viewModel.adapter?.getActor(1)
        assertNotNull(actor)
        assertEquals(actor?.popularity, 18.6651732095211)
    }

    fun testSearch() {
        activity.runOnUiThread {
            activity.menuViewModel.toggleSearch()
        }
        Thread.sleep(2000)
        assertNotNull(activity.searchDialog)
        assertTrue(activity.searchDialog!!.isInit)
        onView(withId(R.id.name)).perform(typeText("bra"))
        Thread.sleep(2000)
        assertNotNull(activity.searchDialog!!.viewModel.nameAdapter.autoSuggestions)
        val suggestion =  activity.searchDialog!!.viewModel.nameAdapter.autoSuggestions!![0]
        assertEquals(suggestion.value, "Brad Pitt")
    }


}