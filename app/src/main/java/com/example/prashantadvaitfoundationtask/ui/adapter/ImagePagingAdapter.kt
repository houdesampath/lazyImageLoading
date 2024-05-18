package com.example.prashantadvaitfoundationtask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.prashantadvaitfoundationtask.databinding.GridViewBinding


class ImagePagingAdapter : PagingDataAdapter<String, ImagePagingAdapter.ImageViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = GridViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = getItem(position)
        if (image != null) {
            holder.bind(image, position)
        }
    }

    class ImageViewHolder(private val binding: GridViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: String, position: Int) {
            Glide.with(itemView.context).load(image).into(binding.imageView)
            //ImageLoader.loadImage(image, binding.imageView, position)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<String> =
            object : DiffUtil.ItemCallback<String>() {
                override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                    // Assuming URLs are unique
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                    return oldItem == newItem
                }
            }
        }


}