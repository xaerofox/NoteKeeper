package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notekeeper.databinding.ActivityItemsBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.jwhh.notekeeper.CourseRecyclerAdapter

class ItemsActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    NoteRecyclerAdapter.OnNoteSelectedListener {

    private lateinit var binding: ActivityItemsBinding


    private val noteLayoutManager by lazy { LinearLayoutManager(this) }
    private val noteRecyclerAdapter by lazy {
        val adapter = NoteRecyclerAdapter(this, DataManager.notes)
        adapter.setOnSelectedListener(this)
        adapter
    }

    private val courseLayoutManager by lazy { GridLayoutManager(this, 2) }
    private val courseRecyclerAdapter by lazy {
        CourseRecyclerAdapter(
            this,
            DataManager.courses.values.toList()
        )
    }

    private val recentlyViewedNoteRecyclerAdapter by lazy {
        val adapter = NoteRecyclerAdapter(this, viewModel.recentlyViewedNotes)
        adapter.setOnSelectedListener(this)
        adapter
    }

    private val viewModel by lazy { ViewModelProvider(this)[ItemsActivityViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val toolbar = binding.layoutAppBar.toolbar
        setSupportActionBar(toolbar)

        binding.layoutAppBar.fab
            .setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java))
            }

        if(viewModel.isNewlyCreated && savedInstanceState != null)
        {
            viewModel.restoreState(savedInstanceState)
        }
        viewModel.isNewlyCreated = false

        handleDisplayFunction(viewModel.navDrawerDisplaySelection)

        val drawerLayout = binding.drawerLayout
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.openNav, R.string.closeNav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (outState != null)
            viewModel.saveState(outState)
    }

    private fun displayNotes() {
        val contentBinding = binding.layoutAppBar.layoutContentItems

        contentBinding.listItems.layoutManager = noteLayoutManager
        contentBinding.listItems.adapter = noteRecyclerAdapter

        binding.navView.menu.findItem(R.id.nav_notes).isChecked = true
    }

    private fun displayCourses() {
        val contentBinding = binding.layoutAppBar.layoutContentItems

        contentBinding.listItems.layoutManager = courseLayoutManager
        contentBinding.listItems.adapter = courseRecyclerAdapter

        binding.navView.menu.findItem(R.id.nav_courses).isChecked = true
    }

    private fun displayRecentlyViewedNotes() {
        val contentBinding = binding.layoutAppBar.layoutContentItems
        val listItems = contentBinding.listItems

        listItems.layoutManager = noteLayoutManager
        listItems.adapter = recentlyViewedNoteRecyclerAdapter

        binding.navView.menu.findItem(R.id.nav_recent_notes).isChecked = true
    }

    override fun onResume() {
        super.onResume()
        val contentBinding = binding.layoutAppBar.layoutContentItems
        contentBinding.listItems.adapter?.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here
        when (item.itemId) {
            R.id.nav_notes,
            R.id.nav_courses,
            R.id.nav_recent_notes -> {
                handleDisplayFunction(item.itemId)
                viewModel.navDrawerDisplaySelection = item.itemId
            }
            R.id.nav_share -> {
                handleSelection("Don't you think you've SHARED enough")
            }
            R.id.nav_send -> {
                handleSelection("Send")
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleDisplayFunction(itemId: Int) {
        when (itemId) {
            R.id.nav_notes -> {
                displayNotes()
            }
            R.id.nav_courses -> {
                displayCourses()
            }
            R.id.nav_recent_notes -> {
                displayRecentlyViewedNotes()
            }
        }
    }

    private fun handleSelection(message: String) {
        val listItems = binding.layoutAppBar.layoutContentItems.listItems

        Snackbar.make(listItems, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onNoteSelected(note: NoteInfo) {
        viewModel.addToRecentlyViewedNotes(note)
    }


}