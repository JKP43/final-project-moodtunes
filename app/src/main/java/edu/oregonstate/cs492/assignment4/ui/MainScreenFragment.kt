package edu.oregonstate.cs492.assignment4.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import edu.oregonstate.cs492.assignment4.R

class MainScreenFragment : Fragment(R.layout.main_screen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val happyButton = view.findViewById<Button>(R.id.happyButton)
        val sadButton = view.findViewById<Button>(R.id.sadButton)
        val angryButton = view.findViewById<Button>(R.id.angryButton)
        val relaxedButton = view.findViewById<Button>(R.id.relaxedButton)

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
        val intent = Intent(requireContext(), MusicActivity::class.java)
        intent.putExtra("mood", mood)
        startActivity(intent)
    }
}
