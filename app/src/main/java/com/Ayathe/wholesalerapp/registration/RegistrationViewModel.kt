package com.Ayathe.wholesalerapp.registration

import androidx.lifecycle.ViewModel
import com.Ayathe.wholesalerapp.data.User
import com.Ayathe.wholesalerapp.repository.FirebaseRepository

class RegistrationViewModel: ViewModel() {
    private val repository = FirebaseRepository()

    fun createNewUser(user: User){
        repository.createNewUser(user)
    }

}