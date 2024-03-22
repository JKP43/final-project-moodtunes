package edu.oregonstate.cs492.assignment4.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.DatabaseBuilder
import kotlinx.coroutines.launch

class SavedSongsActivity : AppCompatActivity() {
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
        // Assume SavedSongsAdapter is similar to your MusicListAdapter but for displaying saved songs
        val adapter = SavedSongsAdapter(emptyList())
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            val songs = DatabaseBuilder.getInstance(applicationContext).songDao().getAllSongs()
            adapter.updateSongs(songs) // Implement this method in your adapter to update the list and notifyDataSetChanged
        }
    }
}
