package com.Ayathe.wholesalerapp.registration

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.Ayathe.wholesalerapp.BaseFragment
import com.Ayathe.wholesalerapp.data.User
import com.Ayathe.wholesalerapp.R
import kotlinx.android.synthetic.main.fragment_sign_up.*

class RegistrationFragment: BaseFragment() {


    private val REG_DEBUG = "REG_DEBUG"
    private val fbAuth = FirebaseAuth.getInstance()
    private val regVm by viewModels<RegistrationViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSignUpClick()
    }

    private fun setupSignUpClick() {
        signUpButtonRegistration.setOnClickListener {
            val email = email_registration.text?.trim().toString()
            val pass = pass_registration.text?.trim().toString()
            val repeatPass = repeat_pass_registration.text?.trim().toString()

            if(pass==repeatPass){
                fbAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnSuccessListener {authRes ->
                       if(authRes.user != null){
                           val user = User(
                                   authRes.user!!.uid,
                                   "",
                                   "",
                                   authRes.user!!.email,
                                   listOf(),
                                   "")
                           regVm.createNewUser(user)
                           startApp()
                       }
                    }
                    .addOnFailureListener{exc ->
                        Snackbar.make(requireView(), "Upss...Something went wrong...", Snackbar.LENGTH_SHORT)
                            .show()
                        Log.d(REG_DEBUG, exc.message.toString())
                    }


            }
        }
    }
}