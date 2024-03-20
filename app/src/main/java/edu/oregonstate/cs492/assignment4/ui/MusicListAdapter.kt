package edu.oregonstate.cs492.assignment4.ui

import android.content.Context
import edu.oregonstate.cs492.assignment4.data.MusicFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import edu.oregonstate.cs492.assignment4.R

class MusicListAdapter(context: Context, private val resourceLayout: Int, private val musicList: List<MusicFormat>) :
    ArrayAdapter<MusicFormat>(context, resourceLayout, musicList) {

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

        artistTextView.text = musicItem.artist
        songTitleTextView.text = musicItem.songTitle

        Glide.with(context)
            .load(musicItem.songImage)
            .into(songImageView)

        return rowView
    }
}