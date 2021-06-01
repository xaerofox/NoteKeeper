package com.example.notekeeper

import androidx.lifecycle.ViewModel

class ItemsActivityViewModel : ViewModel()
{
    private val maxRecentlyViewedNotes = 5
    val recentlyViewedNotes = ArrayList<NoteInfo>(maxRecentlyViewedNotes)

    var navDrawerDisplaySelection = R.id.nav_notes

    fun addToRecentlyViewedNotes(note: NoteInfo) {
        // Check if selection is already in the list
        val existingIndex = recentlyViewedNotes.indexOf(note)
        if(existingIndex == -1)
        {
            // is not in list...
            // Add new one to beginning of list and remove any beyond max we want to keep
            recentlyViewedNotes.add(0, note)
            for (index in recentlyViewedNotes.lastIndex downTo maxRecentlyViewedNotes)
                recentlyViewedNotes.removeAt(index)
        }
        else
        {
            // Shift the ones above down the list and make it first member of the list
            for (index in (existingIndex - 1) downTo 0)
                recentlyViewedNotes[index + 1] = recentlyViewedNotes[index]
            recentlyViewedNotes[0] = note
        }
    }

}