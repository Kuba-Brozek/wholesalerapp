package com.Ayathe.wholesalerapp.activites

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import com.Ayathe.wholesalerapp.R
import com.Ayathe.wholesalerapp.repository.FirebaseRepository
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_item.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class AddItem : AppCompatActivity() {

    val db = Firebase.firestore
    private val cloud = FirebaseFirestore.getInstance()
    val ref = db.collection("cars").document()
    private val storage = FirebaseStorage.getInstance()
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private val REPO_DEBUG = "REPO_DEBUG"
    private val mStorageRef = FirebaseStorage.getInstance().reference
    private lateinit var mProgressDialog: ProgressDialog
    private val REQUEST_IMAGE_CAPTURE = 1

    private val repository = FirebaseRepository()
    private var storageRef = storage.reference
    private val PROFILE_DEBUG = "PROFILE_DEBUG"
    private lateinit var database: DatabaseReference
    private var filePath: Uri? = null
    private val btnUpload: AppCompatButton by lazy {
        findViewById(R.id.submititem) }

    val Any.TAG: String
        get() {
            val tag = javaClass.simpleName
            return if (tag.length <= 23) tag else tag.substring(0, 23)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        submititem.setOnClickListener{
            setupSubmitDataClick()
        }
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference



    }

    private fun setupSubmitDataClick(){

        val name = name123.text.trim().toString()
        val desc = opis.text.trim().toString()
        val car = hashMapOf(
            "name" to name,
            "description" to desc,
            "image" to "",)

        db.collection("cars")
            .add(car)
            .addOnSuccessListener { documentReference ->

                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                val idBefore = db.collection("cars").document().id;
                val idcar = documentReference.id;
                Log.d(TAG, "DocumentSnapshot added with ID: "+idBefore)

                db.collection("cars")
                    .document(documentReference.id)
                    .update("id",idcar)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        }
    }



