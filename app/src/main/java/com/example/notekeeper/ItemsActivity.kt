package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notekeeper.databinding.ActivityItemsBinding
import com.example.notekeeper.databinding.ContentItemsBinding
import com.jwhh.notekeeper.CourseRecyclerAdapter

class ItemsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityItemsBinding

    private val noteLayoutManager by lazy { LinearLayoutManager(this) }
    private val noteRecyclerAdapter by lazy { NoteRecyclerAdapter(this, DataManager.notes) }

    private val courseLayoutManager by lazy { GridLayoutManager (this, 2) }
    private val courseRecyclerAdapter by lazy { CourseRecyclerAdapter(this, DataManager.courses.values.toList()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val toolbar = binding.layoutAppBar.toolbar
        setSupportActionBar(toolbar)

        binding.layoutAppBar.fab
            .setOnClickListener { view ->
                startActivity(Intent(this, MainActivity::class.java))
            }

        displayNotes()

        val drawerLayout = binding.drawerLayout
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.openNav, R.string.closeNav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

//        val navController = findNavController(R.id.nav_host_fragment)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
//            ), drawerLayout
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
    }

    private fun displayNotes() {
        var contentBinding = binding.layoutAppBar.layoutContentItems

        contentBinding.listItems.layoutManager = noteLayoutManager
        contentBinding.listItems.adapter = noteRecyclerAdapter

        binding.navView.menu.findItem(R.id.nav_notes).isChecked = true
    }

    private fun displayCourses() {
        var contentBinding = binding.layoutAppBar.layoutContentItems

        contentBinding.listItems.layoutManager = courseLayoutManager
        contentBinding.listItems.adapter = courseRecyclerAdapter

        binding.navView.menu.findItem(R.id.nav_courses).isChecked = true
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.items, menu)
//        return true
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }

    override fun onResume() {
        super.onResume()
        var contentBinding = binding.layoutAppBar.layoutContentItems
        contentBinding.listItems.adapter?.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_notes -> {
                displayNotes()
            }
            R.id.nav_courses -> {
                displayCourses()
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

    private fun handleSelection(message: String) {
        var listItems = binding.layoutAppBar.layoutContentItems.listItems

        Snackbar.make(listItems, message, Snackbar.LENGTH_LONG).show()
    }
}