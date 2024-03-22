package edu.oregonstate.cs492.assignment4.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
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
    private var musicList: List<MusicFormat> = emptyList()
    private val viewModel: MusicViewModel by viewModels()
    private lateinit var mood: String
    private val musicTitles: MutableList<MusicFormat> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {

        // Retrieve the theme choice from SharedPreferences
        val sharedPreferences = getSharedPreferences(
            "theme_prefs",
            Context.MODE_PRIVATE
        )


        super.onCreate(savedInstanceState)

        // Retrieve the theme choice from intent extras
        val isDarkTheme = sharedPreferences.getBoolean("is_dark_theme", false)


        setContentView(R.layout.activity_music)
        val listView: ListView = findViewById(R.id.listView)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        val adapter = MusicListAdapter(
            this,
            R.layout.music_list_item,
            musicTitles,
            onSaveAndViewClicked = { musicItem ->
                lifecycleScope.launch {
                    val insertResult = DatabaseBuilder.getInstance(applicationContext).songDao().insertSong(
                        SongEntity(
                            artist = musicItem.artist,
                            songTitle = musicItem.songTitle,
                            shortUrl = musicItem.shortUrl,
                            shareUrl = musicItem.shareUrl,
                            songImage = musicItem.songImage,
                            duration = musicItem.duration
                        )
                    )
                    if (insertResult == -1L) {
                        Toast.makeText(applicationContext, "Song has already been added!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "Song saved to favorites!", Toast.LENGTH_SHORT).show()
                        navigateToSavedSongs()
                    }
                }
            },
            isDarkTheme
        )


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
            val songDao = DatabaseBuilder.getInstance(applicationContext).songDao()

            lifecycleScope.launch {
                // Try to insert the song and get the result of the insertion
                val insertResult = songDao.insertSong(
                    SongEntity(
                        artist = selectedMusic.artist,
                        songTitle = selectedMusic.songTitle,
                        shortUrl = selectedMusic.shortUrl,
                        shareUrl = selectedMusic.shareUrl,
                        songImage = selectedMusic.songImage,
                        duration = selectedMusic.duration
                    )
                )
                // Check if the insertResult is -1, which means the song wasn't inserted because it already exists
                if (insertResult == -1L) {
                    Toast.makeText(applicationContext, "Song has already been added!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Song saved to favorites!", Toast.LENGTH_SHORT).show()
                    // Open the share URL of the selected music in browser
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(selectedMusic.shareUrl))
                    startActivity(intent)
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.music_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_view_saved_songs -> {
                navigateToSavedSongs()
                true
            }
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToSavedSongs() {
        val intent = Intent(this, SavedSongsActivity::class.java)
        startActivity(intent)
    }


    override fun onStart() {
        super.onStart()

        mood = intent.getStringExtra("musicList") ?: ""

        viewModel.loadMusic("3c361abf", mood, 30)
    }
}
