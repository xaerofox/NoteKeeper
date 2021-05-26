package com.example.notekeeper

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import com.jwhh.notekeeper.CourseRecyclerAdapter

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @Rule @JvmField
    val itemsActivity = ActivityScenarioRule(ItemsActivity::class.java)

    @Test
    fun selectNoteAfterNavigationDrawerChange()
    {
        //Open nav drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        //Select courses in drawer
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_courses))

        //Display list of courses (card view)
        val coursePosition = 0
        onView(withId(R.id.listItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CourseRecyclerAdapter.ViewHolder>(coursePosition, click())
        )

        //Switch to display list of notes (grid view)
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_notes))

        val notePosition = 0
        onView(withId(R.id.listItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<NoteRecyclerAdapter.ViewHolder>(notePosition, click())
        )

        //Compare selected note to DataManager (via notePosition)
        val note = DataManager.notes[notePosition]
        onView(withId(R.id.spinnerCourses)).check(matches(withSpinnerText(containsString(note.course?.title))))
        onView(withId(R.id.textNoteTitle)).check(matches(withText(containsString(note.title))))
        onView(withId(R.id.textNoteText)).check(matches(withText(containsString(note.text))))

    }
}