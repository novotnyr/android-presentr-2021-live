package com.github.novotnyr.presentr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()

    // 1. [x]  pripravime handler: to je vykonavatel uloh z frontu
    // 2. [x] pripravime si samotnu ulohu - refresh pouzivatelov
    // 3. periodicky budeme volat submitovanie ulohy do handlera

    private val handler = Handler(Looper.getMainLooper())

    private val refreshUserTask = object : Runnable {
        override fun run() {
            userViewModel.refresh()
            handler.postDelayed(this, 3*1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userListView: ListView = findViewById(R.id.userListView)
        val adapter = ArrayAdapter<User>(this, android.R.layout.simple_list_item_1)
        userListView.adapter = adapter

        userViewModel.refresh()
        // synchronizacia medzi VM a adapterom zoznamu
        userViewModel.users.observe(this) {
            adapter.clear()
            adapter.addAll(it)
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(refreshUserTask)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(refreshUserTask, 3 * 1000)
    }

    fun onFabClick(view: View) {
        userViewModel.login(User("novotny"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.refreshMenuItem) {
            userViewModel.refresh()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}