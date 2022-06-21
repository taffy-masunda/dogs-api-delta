package za.co.trutz.dogsapidelta.data.service

class DogsRepository constructor(private val retrofitDogService: RetrofitService) {
    fun getDogs() = retrofitDogService.getDogs()
}