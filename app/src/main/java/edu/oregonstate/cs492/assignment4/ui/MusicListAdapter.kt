package edu.oregonstate.cs492.assignment4.ui

import android.content.Context
import edu.oregonstate.cs492.assignment4.data.MusicFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import edu.oregonstate.cs492.assignment4.R
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.core.content.ContextCompat

class MusicListAdapter(
    context: Context,
    private val resourceLayout: Int,
    private val musicList: List<MusicFormat>,
    private val onSaveAndViewClicked: (MusicFormat) -> Unit,
    private val isDarkTheme: Boolean
) : ArrayAdapter<MusicFormat>(context, resourceLayout, musicList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView = convertView
        if (rowView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rowView = inflater.inflate(resourceLayout, parent, false)
        }

        val musicItem = musicList[position]

        val artistTextView = rowView!!.findViewById<TextView>(R.id.artistTextView)
        val songTitleTextView = rowView.findViewById<TextView>(R.id.songTitleTextView)
        val songImageView = rowView.findViewById<ImageView>(R.id.songImageView)
        val saveAndViewButton = rowView.findViewById<Button>(R.id.saveAndViewButton)
        val shareButton = rowView.findViewById<Button>(R.id.shareButton)

        artistTextView.text = musicItem.artist
        songTitleTextView.text = musicItem.songTitle

        val textColorResId = if (isDarkTheme) R.color.white else R.color.black
        artistTextView.setTextColor(ContextCompat.getColor(context, textColorResId))
        songTitleTextView.setTextColor(ContextCompat.getColor(context, textColorResId))

        // Adjust background color based on theme
        val backgroundColorResId = if (isDarkTheme) R.color.dark_gray else R.color.light_gray
        rowView.setBackgroundResource(backgroundColorResId)

        Glide.with(context)
            .load(musicItem.songImage)
            .into(songImageView)

        saveAndViewButton.setOnClickListener {
            onSaveAndViewClicked(musicItem) // Invoke the callback with the current music item
        }
        shareButton.setOnClickListener {
            // Handle share action here
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, musicItem.shareUrl)
            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        if (musicItem.shortUrl.isNotBlank()) {
            songImageView.setOnClickListener {
                songImageView.setBackgroundResource(R.drawable.image_border)
                val mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(musicItem.shortUrl)
                mediaPlayer.prepareAsync()

                mediaPlayer.setOnPreparedListener { mediaPlayer ->
                    mediaPlayer.start()

                    songImageView.postDelayed({
                        if (mediaPlayer.isPlaying) {
                            mediaPlayer.stop()
                            mediaPlayer.release()
                            songImageView.setBackgroundResource(0)
                        }
                    }, 15000)
                }

            }
        } else {
            songImageView.setImageAlpha(30)
        }

        return rowView
    }
}