package edu.oregonstate.cs492.assignment4.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import edu.oregonstate.cs492.assignment4.R
import androidx.appcompat.content.res.AppCompatResources

class MainScreenFragment : Fragment(R.layout.main_screen){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.main_screen)

        val happyButton = findViewById<Button>(R.id.happyButton)
        val sadButton = findViewById<Button>(R.id.sadButton)
        val angryButton = findViewById<Button>(R.id.angryButton)
        val relaxedButton = findViewById<Button>(R.id.relaxedButton)

        // Set onClickListeners for each mood button
        happyButton.setOnClickListener {
            // Navigate to SongsActivity and pass the mood
            navigateToSongs("happy")
        }

        sadButton.setOnClickListener {
            navigateToSongs("sad")
        }

        angryButton.setOnClickListener {
            navigateToSongs("angry")
        }

        relaxedButton.setOnClickListener {
            navigateToSongs("relaxed")
        }
    }

    private fun navigateToSongs(mood: String) {
        val intent = intent(this, MusicActivity::class.java)
        intent.putExtra("mood", mood)
        startActivity(intent)
    }
}