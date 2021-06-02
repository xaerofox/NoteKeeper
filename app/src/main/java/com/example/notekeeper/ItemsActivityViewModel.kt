package com.example.notekeeper

import android.os.Bundle
import androidx.lifecycle.ViewModel

class ItemsActivityViewModel : ViewModel()
{
    var isNewlyCreated = true

    private val maxRecentlyViewedNotes = 5
    val recentlyViewedNotes = ArrayList<NoteInfo>(maxRecentlyViewedNotes)
    val navDrawerDisplaySelectionName =
        "com.example.notekeeper.ItemsActivityViewModel.navDrawerDisplaySelection"
    var navDrawerDisplaySelection = R.id.nav_notes
    var recentlyViewedNoteIdsName =
        "com.example.notekeeper.ItemsActivityViewModel.recentlyViewedNoteIds"

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

    fun saveState(outState: Bundle) {
        outState.putInt(navDrawerDisplaySelectionName, navDrawerDisplaySelection)

        val noteIds = DataManager.noteIdsAsIntArray(recentlyViewedNotes)
        outState.putIntArray(recentlyViewedNoteIdsName, noteIds)
    }

    fun restoreState(savedInstanceState: Bundle) {
        navDrawerDisplaySelection = savedInstanceState.getInt(navDrawerDisplaySelectionName)

        val noteIds = savedInstanceState.getIntArray(recentlyViewedNoteIdsName)
        val noteList = noteIds?.let { DataManager.loadNotes(*it) }
        //val noteList = DataManager.loadNotes(*noteIds)
        if (noteList != null) {
            recentlyViewedNotes.addAll(noteList)
        }
    }

}