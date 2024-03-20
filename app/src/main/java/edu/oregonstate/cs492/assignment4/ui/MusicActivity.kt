package edu.oregonstate.cs492.assignment4.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.AppDatabase
import edu.oregonstate.cs492.assignment4.data.DatabaseBuilder
import edu.oregonstate.cs492.assignment4.data.MusicFormat
import edu.oregonstate.cs492.assignment4.data.SongEntity
import kotlinx.coroutines.launch

class MusicActivity : AppCompatActivity() {
    // Assume the music list is passed from the previous page or fetched based on mood search
    private var musicList: List<MusicFormat> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        val listView: ListView = findViewById(R.id.listView)

        // Retrieve the music list passed from the previous activity or fetched based on mood search
        musicList = intent.getParcelableArrayListExtra("musicList") ?: emptyList()

        // Convert music list to an array of song titles
        val musicTitles = musicList.map { it.songTitle }.toTypedArray()

        // Use ArrayAdapter to display song titles in the ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, musicTitles)
        listView.adapter = adapter

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



            // Build Spotify Uri
            val spotifyUri = "spotify:${selectedMusic.shortUrl}"

            // Open Spotify using Intent
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(spotifyUri))
            intent.setPackage("com.spotify.music") // Specify Spotify app to open the link

            // Check if Spotify app is installed on the device
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "Spotify app is not installed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
