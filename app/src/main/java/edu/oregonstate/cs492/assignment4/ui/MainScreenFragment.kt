package edu.oregonstate.cs492.assignment4.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class MainScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val selectedMood = remember { mutableStateOf("") }
                MainScreen(onMoodSelected = { mood ->
                    selectedMood.value = mood
                    navigateToSongs(mood)
                })
            }
        }
    }

    private fun navigateToSongs(mood: String) {
        val intent = Intent(requireContext(), MusicActivity::class.java)
        intent.putExtra("musicList", mood)
        startActivity(intent)
    }
}

@Composable
fun MainScreen(onMoodSelected: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MoodButton(text = "Happy", onClick = { onMoodSelected("Happy") })
        MoodButton(text = "Sad", onClick = { onMoodSelected("Sad") })
        MoodButton(text = "Angry", onClick = { onMoodSelected("Angry") })
        MoodButton(text = "Relaxed", onClick = { onMoodSelected("Relaxed") })
    }
}

@Composable
fun MoodButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = text)
    }
}

//@Preview
//@Composable
//fun PreviewMainScreen() {
//    Assignment4Theme {
//        MainScreen(onMoodSelected = {})
//    }
//}
