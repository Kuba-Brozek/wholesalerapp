package com.Ayathe.wholesalerapp.activites

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.Ayathe.wholesalerapp.R
import com.Ayathe.wholesalerapp.data.Car
import com.Ayathe.wholesalerapp.repository.FirebaseRepository
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.IOException

class AddItem : AppCompatActivity() {

    val db = Firebase.firestore
    val ref = db.collection("cars").document()
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    val Any.TAG: String
        get() {
            val tag = javaClass.simpleName
            return if (tag.length <= 23) tag else tag.substring(0, 23)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        submititem.setOnClickListener(View.OnClickListener {
            setupSubmitDataClick()
        })
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference



    }

    private fun setupSubmitDataClick(){

        val name = name123.text.trim().toString()
        val desc = opis.text.trim().toString()
        val car = hashMapOf(
            "name" to name,
            "id" to ref.id,
            "description" to desc,
            "image" to "",
        )


        db.collection("cars")
            .add(car)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }


        }


    }



