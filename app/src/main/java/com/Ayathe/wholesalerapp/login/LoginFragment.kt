package com.Ayathe.wholesalerapp.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.Ayathe.wholesalerapp.BaseFragment
import com.Ayathe.wholesalerapp.R
import kotlinx.android.synthetic.main.fragment_sign_in.*

class LoginFragment: BaseFragment(){

    private val fbAuth = FirebaseAuth.getInstance()
    private val LOG_DEUBG = "LOG_DEBUG"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLoginClick()
        setupRegistrationClick()
    }

    private fun setupRegistrationClick() {
        signUpButton.setOnClickListener {
            findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment().actionId)
        }
    }

    private fun setupLoginClick() {
        loginButton.setOnClickListener {
            val email = emailLoginInput.text?.trim().toString()
            val pass = passLoginInput.text?.trim().toString()

            fbAuth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener { authRes ->
                    if(authRes.user != null) startApp()
                }
                .addOnFailureListener{ exc ->
                    Snackbar.make(requireView(), "Upss...Something went wrong...", Snackbar.LENGTH_SHORT)
                        .show()
                    Log.d(LOG_DEUBG, exc.message.toString())
                }
        }
    }
}