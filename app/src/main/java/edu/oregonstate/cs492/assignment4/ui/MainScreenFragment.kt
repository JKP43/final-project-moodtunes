package edu.oregonstate.cs492.assignment4.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment


class MainScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val selectedMood = remember { mutableStateOf("") }
                val isDarkTheme = remember { mutableStateOf(getSavedThemeChoice()) }

                MainScreen(
                    onMoodSelected = { mood ->
                        selectedMood.value = mood
                        navigateToSongs(mood, isDarkTheme.value)
                    },
                    onThemeToggled = { isDark ->
                        isDarkTheme.value = isDark
                        saveThemeChoice(isDark)
                    },
                    isDarkTheme.value
                )
            }
        }
    }

    private fun navigateToSongs(mood: String, isDarkTheme: Boolean) {
        val intent = Intent(requireContext(), MusicActivity::class.java)
        intent.putExtra("musicList", mood)
        intent.putExtra("isDarkTheme", isDarkTheme)
        startActivity(intent)
    }

    private fun saveThemeChoice(isDark: Boolean) {
        Log.d("MainScreenFragment", "Dark theme toggled: $isDark")
        val sharedPreferences = requireContext().getSharedPreferences(
            "theme_prefs",
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_dark_theme", isDark)
        editor.apply()
    }
    private fun getSavedThemeChoice(): Boolean {
        val sharedPreferences = requireContext().getSharedPreferences(
            "theme_prefs",
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getBoolean("is_dark_theme", false)
    }
}


@Composable
fun MainScreen(onMoodSelected: (String) -> Unit, onThemeToggled: (Boolean) -> Unit, isDarkTheme: Boolean) {
    var isDarkTheme by remember { mutableStateOf(isDarkTheme) }

    val backgroundColor = if (isDarkTheme) Color.DarkGray else Color.LightGray
    val textColor = if (isDarkTheme) Color.LightGray else Color.DarkGray

    MaterialTheme(
        colors = if (isDarkTheme) darkColors() else lightColors()
    ) {
        Surface(color = backgroundColor) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Text(
                        text = "Light Theme",
                        color = textColor,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp)
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                    )
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { isChecked ->
                            isDarkTheme = isChecked
                            onThemeToggled(isChecked)
                        },
                        modifier = Modifier
                            .padding(end = 16.dp)
                    )
                    Text(
                        text = "Dark Theme",
                        color = textColor,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                    )
                }

                LazyVerticalGrid(GridCells.Fixed(2)) {
                    item {
                        MoodButton("😊", onClick = { onMoodSelected("Happy") }, isDarkTheme = isDarkTheme)
                    }
                    item {
                        MoodButton("😢", onClick = { onMoodSelected("Sad") }, isDarkTheme = isDarkTheme)
                    }
                    item {
                        MoodButton("😠", onClick = { onMoodSelected("Angry") }, isDarkTheme = isDarkTheme)
                    }
                    item {
                        MoodButton("😌", onClick = { onMoodSelected("Relaxed") }, isDarkTheme = isDarkTheme)
                    }
                    item {
                        MoodButton("😎", onClick = { onMoodSelected("Cool") }, isDarkTheme = isDarkTheme)
                    }
                    item {
                        MoodButton("🤩", onClick = { onMoodSelected("Party") }, isDarkTheme = isDarkTheme)
                    }
                    item {
                        MoodButton("😑", onClick = { onMoodSelected("Neutral") }, isDarkTheme = isDarkTheme)
                    }
                    item {
                        MoodButton("❓", onClick = { onMoodSelected("Random") }, isDarkTheme = isDarkTheme)
                    }
                }
            }
        }
    }
}


@Composable
fun MoodButton(emoji: String, onClick: () -> Unit, isDarkTheme:Boolean) {
    Button(onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .height(100.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isDarkTheme) Color.LightGray else Color.Gray,
            contentColor = if (isDarkTheme) Color.Gray else Color.LightGray
        )
    ) {
        Text(
            emoji,
            fontSize = (48.dp).value.sp
        )
    }
}

