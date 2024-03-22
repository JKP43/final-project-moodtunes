package edu.oregonstate.cs492.assignment4.ui

import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.SongEntity

class SavedSongsAdapter(
    private var songs: List<SongEntity>,
    private val isDarkTheme: Boolean,
    private val onDeleteClicked: (SongEntity) -> Unit
    ) : RecyclerView.Adapter<SavedSongsAdapter.ViewHolder>() {

    fun updateSongs(newSongs: List<SongEntity>) {
        songs = newSongs
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song, isDarkTheme)
        holder.itemView.findViewById<ImageView>(R.id.deleteButton).setOnClickListener {
            onDeleteClicked(song)
        }
    }

    override fun getItemCount(): Int = songs.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val artistTextView: TextView = itemView.findViewById(R.id.artistTextView)
        private val songTitleTextView: TextView = itemView.findViewById(R.id.songTitleTextView)
        private val songImageView: ImageView = itemView.findViewById(R.id.songImageView)

        fun bind(song: SongEntity, isDarkTheme: Boolean) {
            val backgroundColor = if (isDarkTheme) R.color.dark_gray else R.color.light_gray
            val textColorResId = if (isDarkTheme) R.color.white else R.color.black
            val textColor = ContextCompat.getColor(itemView.context, textColorResId)

            itemView.setBackgroundResource(backgroundColor)

            artistTextView.text = song.artist
            songTitleTextView.text = song.songTitle

            artistTextView.setTextColor(textColor)
            songTitleTextView.setTextColor(textColor)

            Glide.with(itemView.context)
                .load(song.songImage)
                .into(songImageView)

            if (song.shortUrl.isNotBlank()) {
                songImageView.setOnClickListener {
                    songImageView.setBackgroundResource(R.drawable.image_border)
                    val mediaPlayer = MediaPlayer()
                    mediaPlayer.setDataSource(song.shortUrl)
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


        }
    }
}
