package edu.oregonstate.cs492.assignment4.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.DatabaseBuilder
import kotlinx.coroutines.launch

class SavedSongsActivity : AppCompatActivity() {
    private lateinit var adapter: SavedSongsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_songs)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Saved Songs"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView = findViewById<RecyclerView>(R.id.saved_songs_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SavedSongsAdapter(emptyList()) { songToDelete ->
            lifecycleScope.launch {
                DatabaseBuilder.getInstance(applicationContext).songDao().deleteSong(songToDelete.shareUrl)
                refreshSongs()
            }
        }
        recyclerView.adapter = adapter
        refreshSongs()
    }

    private fun refreshSongs() {
        lifecycleScope.launch {
            val songs = DatabaseBuilder.getInstance(applicationContext).songDao().getAllSongs()
            adapter.updateSongs(songs)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.saved_songs_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                navigateToHome()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToHome() {
        val homeIntent = Intent(this, MainActivity::class.java)
        homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(homeIntent)
        finish()
    }


}
