package com.Ayathe.wholesalerapp.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.Ayathe.wholesalerapp.data.Car
import com.Ayathe.wholesalerapp.repository.FirebaseRepository

class ProfileViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    val user = repository.getUserData()
    val car = repository.getCarData()
    val favCars = user.switchMap {
        repository.getFavCars(it.favCars)
    }
    fun removeFavCar(car: Car){
        repository.removeFavCar(car)
    }
    fun editProfileData(map: Map<String, String>){
        repository.editProfileData(map)
    }
    fun uploadUserPhoto(bytes: ByteArray){
        repository.uploadUserPhoto(bytes)
    }

}