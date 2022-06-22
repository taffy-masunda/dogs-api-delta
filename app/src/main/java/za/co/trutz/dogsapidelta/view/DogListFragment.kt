package za.co.trutz.dogsapidelta.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.trutz.dogsapidelta.data.service.DogsRepository
import za.co.trutz.dogsapidelta.data.service.RetrofitService
import za.co.trutz.dogsapidelta.databinding.FragmentDogsListBinding
import za.co.trutz.dogsapidelta.viewmodel.DogsViewModel
import za.co.trutz.dogsapidelta.viewmodel.DogsViewModelFactory

class DogListFragment : Fragment() {

    private var _binding: FragmentDogsListBinding? = null
    private lateinit var viewModel: DogsViewModel
    private val retrofitService = RetrofitService.getInstance()
    private val binding get() = _binding!!
    private lateinit var adapter: DogsRecyclerAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private var isPageLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDogsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =
            ViewModelProvider(this, DogsViewModelFactory(DogsRepository(retrofitService)))
                .get(DogsViewModel::class.java)

        layoutManager = GridLayoutManager(requireContext(), 2)
        binding.dogsListRecyclerView.layoutManager = layoutManager
        
        getMoreDogs()

        binding.dogsListRecyclerView.addOnScrollListener( object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if(dy >= 0){
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItem = (layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition()
                    val totalDogItems = adapter.itemCount

                    if (!isPageLoading){
                        if((visibleItemCount + pastVisibleItem) >= totalDogItems){
                            viewModel.loading
                            getMoreDogs()
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun getMoreDogs (){
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

        viewModel.dogsList.observe(viewLifecycleOwner) {
            adapter = DogsRecyclerAdapter()
            adapter.setDogsList(it.message)
            adapter.notifyDataSetChanged()
            binding.dogsListRecyclerView.adapter = adapter
        }
        viewModel.getDogs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}