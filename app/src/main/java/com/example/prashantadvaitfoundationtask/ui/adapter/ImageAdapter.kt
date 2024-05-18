package com.example.prashantadvaitfoundationtask.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.prashantadvaitfoundationtask.databinding.GridViewBinding
import com.example.prashantadvaitfoundationtask.databinding.LoadingViewBinding
import com.example.prashantadvaitfoundationtask.utils.ImageLoader
import timber.log.Timber

class ImageAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val imageUrls = ArrayList<String>()
    inner class GridViewHolder(private val binding: GridViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String, position: Int) {
            ImageLoader.loadImage(imageUrl, binding.imageView, position)
        }

        fun onViewRecycled(adapterPosition: Int) {
            Timber.i("On View Recycled: $adapterPosition")
            ImageLoader.cancelPotentialWork(binding.imageView)
            binding.imageView.setImageDrawable(null)
        }
    }

    inner class LoadingViewHolder(private val binding: LoadingViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.progressBar.visibility = View.GONE
        }
    }

    fun addImageUrls(newImageUrls: Collection<String>) {
        val startPosition = imageUrls.size+1
        imageUrls.addAll(newImageUrls)
        notifyItemRangeInserted(startPosition, newImageUrls.size)
    }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
         val binding = GridViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
         return GridViewHolder(binding)
     }

     override fun getItemCount(): Int {
         return imageUrls.size
     }


    override fun onViewRecycled(holder: ViewHolder) {
        when(holder) {
            is GridViewHolder -> holder.onViewRecycled(holder.adapterPosition)
        }
    }

     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         Timber.i("Binding at position: $position")
         when(holder) {
             is GridViewHolder -> { holder.bind(imageUrls[position], position) }
         }
     }


 }