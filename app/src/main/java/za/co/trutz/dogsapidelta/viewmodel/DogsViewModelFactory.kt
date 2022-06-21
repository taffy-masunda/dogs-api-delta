package za.co.trutz.dogsapidelta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import za.co.trutz.dogsapidelta.data.service.DogsRepository

class DogsViewModelFactory constructor(private val repository: DogsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DogsViewModel::class.java)) {
            DogsViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("DogsViewModel Not Found")
        }
    }
}