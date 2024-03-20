package edu.oregonstate.cs492.assignment4.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.AppDatabase
import edu.oregonstate.cs492.assignment4.data.DatabaseBuilder
import edu.oregonstate.cs492.assignment4.data.MusicForecast
import edu.oregonstate.cs492.assignment4.data.MusicFormat
import edu.oregonstate.cs492.assignment4.data.MusicRepository
import edu.oregonstate.cs492.assignment4.data.MusicService
import edu.oregonstate.cs492.assignment4.data.SongEntity
import kotlinx.coroutines.launch

class MusicActivity () : AppCompatActivity() {
    // Assume the music list is passed from the previous page or fetched based on mood search
    private var musicList: List<MusicFormat> = emptyList()
    private val viewModel: MusicViewModel by viewModels()
    private lateinit var mood: String
    private val musicTitles: MutableList<MusicFormat> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        val listView: ListView = findViewById(R.id.listView)

        // Use ArrayAdapter to display song titles in the ListView
        val adapter = MusicListAdapter(this, R.layout.music_list_item, musicTitles)
        listView.adapter = adapter



        viewModel.music.observe(this) { m ->
            m?.let {
                musicTitles.clear()
                val shuffledSongs = it.songs.shuffled() //Randomize order
                val halfLength = shuffledSongs.size / 2
                //
                for (i in 0 until halfLength) {
                    musicTitles.add(shuffledSongs[i])
                }
                adapter.notifyDataSetChanged() // Notify adapter of changes
            }
        }


        // Set click listener for the ListView
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // Get the clicked song
            val selectedMusic = musicList[position]

            // Inside your ListView's click listener, after the selectedMusic is defined
            val db = DatabaseBuilder.getInstance(applicationContext)
            val songDao = db.songDao()

            lifecycleScope.launch {
                songDao.insertSong(
                    SongEntity(
                        artist = selectedMusic.artist,
                        songTitle = selectedMusic.songTitle,
                        shortUrl = selectedMusic.shortUrl,
                        shareUrl = selectedMusic.shareUrl,
                        songImage = selectedMusic.songImage,
                        duration = selectedMusic.duration
                    )
                )
            }
            Toast.makeText(applicationContext, "Song saved to favorites!", Toast.LENGTH_SHORT).show()
            // Open the share URL of the selected music in browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(selectedMusic.shareUrl))
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        mood = intent.getStringExtra("musicList") ?: ""

        viewModel.loadMusic("3c361abf", mood, 30)
    }
}
