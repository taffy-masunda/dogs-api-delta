package za.co.trutz.dogsapidelta.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Response
import za.co.trutz.dogsapidelta.data.service.DogsRepository
import za.co.trutz.dogsapidelta.data.service.DogsResponse

class DogsViewModel constructor(private val repository: DogsRepository): ViewModel() {

    val dogsList = MutableLiveData<DogsResponse>()
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val loaded = MutableLiveData<Boolean>()

    fun getDogs() {
        val response = repository.getDogs()
        response.enqueue(object : retrofit2.Callback<DogsResponse> {
            override fun onResponse(call: Call<DogsResponse>, response: Response<DogsResponse>) {
                dogsList.postValue(response.body())
                loaded.value = true
                loading.value = false
            }

            override fun onFailure(call: Call<DogsResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
                loaded.value = false
                loading.value = false
            }
        })
    }
}