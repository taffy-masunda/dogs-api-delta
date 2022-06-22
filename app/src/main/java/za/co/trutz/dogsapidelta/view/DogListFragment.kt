package za.co.trutz.dogsapidelta.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import za.co.trutz.dogsapidelta.data.service.DogsRepository
import za.co.trutz.dogsapidelta.data.service.RetrofitService
import za.co.trutz.dogsapidelta.databinding.FragmentFirstBinding
import za.co.trutz.dogsapidelta.viewmodel.DogsViewModel
import za.co.trutz.dogsapidelta.viewmodel.DogsViewModelFactory

class DogListFragment : Fragment(), DogRecyclerInterface {

    private var _binding: FragmentFirstBinding? = null
    private lateinit var viewModel: DogsViewModel
    private val retrofitService = RetrofitService.getInstance()
    private val adapter = DogsRecyclerAdapter(this)
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =
            ViewModelProvider(this, DogsViewModelFactory(DogsRepository(retrofitService)))
                .get(DogsViewModel::class.java)

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.loadingDialog.visibility = View.VISIBLE
            } else {
                binding.loadingDialog.visibility = View.GONE
                viewModel.loaded.observe(viewLifecycleOwner) { loadedData ->
                    if (loadedData) {
                        binding.dogsListRecyclerView.visibility = View.VISIBLE
                    } else {
                        binding.errorMessageTextview.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(context, "Error fetching dog data!", Toast.LENGTH_SHORT).show()
        }

        binding.dogsListRecyclerView.adapter = adapter
        viewModel.dogsList.observe(viewLifecycleOwner) {
            adapter.setDogsList(it.message)
        }

        viewModel.getDogs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDogItemImageClick(imageUrl: String) {}
}