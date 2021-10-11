package com.Ayathe.wholesalerapp.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.Ayathe.wholesalerapp.R
import com.Ayathe.wholesalerapp.data.Car
import com.Ayathe.wholesalerapp.profile.ProfileViewModel
import com.Ayathe.wholesalerapp.repository.FirebaseRepository
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_modify_item.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ModifyItem : AppCompatActivity() {

    val db = Firebase.firestore
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_item)

        val carname:String = intent.getStringExtra("carname").toString()
        val cardesc:String = intent.getStringExtra("cardesc").toString()
        val carid:String = intent.getStringExtra("carid").toString()

        moditemname.setText(carname)
        moditemdesc.setText(cardesc)
        xd.setText(carid)
        setupSubmitcarClick()
        /*Glide.with(this)
            .load(car.image)
            .circleCrop()
            .into(userImage)*/

    }
    private fun setupSubmitcarClick(){
        submitcar.setOnClickListener {
            val name = moditemname.text.trim().toString()
            val desc = moditemdesc.text.trim().toString()
            val id = xd.text.trim().toString()
            database = Firebase.database.reference

            db.collection("cars").document(id).update("name",name, "description", desc)
        }
    }
    }

