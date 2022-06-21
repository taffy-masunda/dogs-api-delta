package za.co.trutz.dogsapidelta.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import za.co.trutz.dogsapidelta.R
import za.co.trutz.dogsapidelta.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DogBigImageFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonShare.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            //TODO share functionality
        }

        if (arguments != null) {
            val imageUrl = arguments!!.get("argImageUrl")

            Glide.with(requireContext())
                .load(imageUrl)
                .placeholder(R.drawable.paw_placeholder)
                .error(R.drawable.paw_placeholder)
                .centerCrop()
                .into(binding.dogImageView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}