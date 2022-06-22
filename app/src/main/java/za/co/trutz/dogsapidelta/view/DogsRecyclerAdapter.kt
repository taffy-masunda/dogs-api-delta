package za.co.trutz.dogsapidelta.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import za.co.trutz.dogsapidelta.R
import za.co.trutz.dogsapidelta.databinding.SingleDogImageItemLayoutBinding

class DogsRecyclerAdapter : RecyclerView.Adapter<DogsRecyclerAdapter.DogViewHolder>() {

    private var dogsList: MutableList<String> = ArrayList()

    fun setDogsList(dogsList: List<String>) {
        this.dogsList = dogsList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SingleDogImageItemLayoutBinding.inflate(inflater, parent, false)

        return DogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val dog = dogsList[position]

        Glide.with(holder.itemView.context)
            .load(dog)
            .error(R.drawable.paw_placeholder)
            .centerCrop()
            .into(holder.binding.dogImageView)

        holder.binding.dogImageView.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                val bundle: Bundle = bundleOf()
                bundle.putString(DOG_KEY, dog)

                findNavController(holder.itemView.findFragment()).navigate(
                    R.id.action_DogListFragment_to_DogBigImageFragment,
                    bundle
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return dogsList.size
    }

    class DogViewHolder(val binding: SingleDogImageItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        const val DOG_KEY = "argImageUrl"
    }
}