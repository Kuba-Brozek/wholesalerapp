package com.Ayathe.wholesalerapp.home

import androidx.lifecycle.ViewModel
import com.Ayathe.wholesalerapp.data.Car
import com.Ayathe.wholesalerapp.repository.FirebaseRepository

class HomeViewModel : ViewModel() {
    private val repository = FirebaseRepository()
    val cars = repository.getCars()

    fun addFavCar(car: Car){
        repository.addFavCar(car)
    }

}