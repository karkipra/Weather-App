package com.pratikkarki.weatherapp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.pratikkarki.weatherapp.adapter.TodoAdapter
import com.pratikkarki.weatherapp.data.AppDatabase
import com.pratikkarki.weatherapp.data.Todo
import com.pratikkarki.weatherapp.data.WeatherResult
import com.pratikkarki.weatherapp.network.WeatherAPI
import com.pratikkarki.weatherapp.touch.TodoTouchHelperCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,TodoDialog.TodoHandler {
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fabAdd.setOnClickListener{
            addCity()
        }

        initRecycleView()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

    }

    private fun initRecycleView() {
        Thread {
            val todos = AppDatabase.getInstance(this@MainActivity).todoDao().findAllTodos()

            runOnUiThread {
                todoAdapter = TodoAdapter(this@MainActivity, todos)

                recyclerTodo.adapter = todoAdapter
                val callback = TodoTouchHelperCallback(todoAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerTodo)
            }
        }.start()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            val intentMain = Intent(Intent.ACTION_MAIN)
            intentMain.addCategory(Intent.CATEGORY_HOME)
            intentMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intentMain)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_add -> {
                addCity()
            }
            R.id.nav_about -> {
                Toast.makeText(this@MainActivity,
                        getString(R.string.author),
                        Toast.LENGTH_LONG)
                        .show()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun todoCreated(item: Todo) {
        Thread {
            val id = AppDatabase.getInstance(this).todoDao().insertTodo(item)
            item.todoId = id

            runOnUiThread {
                todoAdapter.addTodo(item)
            }
        }.start()
    }

    private fun addCity() {
        TodoDialog().show(supportFragmentManager, "TAG_CREATE")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_all -> {
                Thread {
                    AppDatabase.getInstance(this@MainActivity).todoDao().deleteAll()
                    runOnUiThread {
                        todoAdapter.removeAll()
                    }
                }.start()
            }
        }
        return true
    }

}
