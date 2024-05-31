package com.latihan.storyapp.view.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.latihan.storyapp.data.api.entity.StoryEntity
import com.latihan.storyapp.databinding.StoryItemBinding
import com.latihan.storyapp.view.detail.DetailActivity

class StoryAdapter : PagingDataAdapter<StoryEntity, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    class MyViewHolder(private val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(items: StoryEntity?) {
            binding.tvTitle.text = items?.name
            binding.tvDesc.text = items?.description
            binding.tvImage.loadImage(items!!.photoUrl)

            itemView.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.tvImage, "imageStory"),
                        Pair(binding.tvTitle, "title"),
                        Pair(binding.tvDesc, "description"),
                    )


                Log.d("StoryAdapter", "onBindViewHolder: Clicked $items")
                val intent = Intent(binding.tvItem.context, DetailActivity::class.java)
                intent.putExtra("id", items?.id)
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}

private fun ImageView.loadImage(url: String?) {
    Glide.with(this)
        .load(url)
        .into(this)
}

