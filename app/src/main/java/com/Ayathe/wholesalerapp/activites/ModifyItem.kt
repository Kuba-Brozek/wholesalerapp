package com.Ayathe.wholesalerapp.activites

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.icu.number.NumberFormatter.with
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.material.Snackbar
import com.Ayathe.wholesalerapp.R
import com.Ayathe.wholesalerapp.data.Car
import com.Ayathe.wholesalerapp.profile.ProfileViewModel
import com.Ayathe.wholesalerapp.repository.FirebaseRepository
import com.bumptech.glide.GenericTransitionOptions.with
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.with
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.with
import com.google.android.gms.cast.framework.media.ImagePicker
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import io.reactivex.internal.util.NotificationLite.getError
import kotlinx.android.synthetic.main.activity_modify_item.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.list_row.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL
import java.util.*


class ModifyItem : AppCompatActivity() {

    private val REPO_DEBUG = "REPO_DEBUG"
    private val REQUEST_IMAGE_CAPTURE = 1
    private val repository = FirebaseRepository()
    val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()
    private lateinit var database: DatabaseReference
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null

    private val btnSelectImage: AppCompatButton by lazy {
        findViewById(R.id.btn_choose_image)
    }

    private val imgPost: AppCompatImageView by lazy {
        findViewById(R.id.moditemimg)
    }

    private val btnUpload: AppCompatButton by lazy {
        findViewById(R.id.btn_upload_image)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_item)

        val carname: String = intent.getStringExtra("carname").toString()
        val cardesc: String = intent.getStringExtra("cardesc").toString()
        val carid: String = intent.getStringExtra("carid").toString()

        initUI()

        moditemname.setText(carname)
        moditemdesc.setText(cardesc)
        xd.setText(carid)
        showAlertDialog()

    }

    private fun showAlertDialog(){
        submitcar.setOnClickListener {
        MaterialAlertDialogBuilder(this).setTitle("Alert").setMessage("Are you sure you want to delete that item?")
            .setNegativeButton("No"){dialog, which ->

            }
            .setPositiveButton("Delete"){dialog, which->
            deleteItem()
            }.show()
        }
    }



    private fun deleteItem() {
        val id = xd.text.trim().toString()

            db.collection("cars").document(id)
                .delete()
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }


    }
    private fun sendDataToFB(){
        val name = moditemname.text.trim().toString()
        val desc = moditemdesc.text.trim().toString()
        val id = xd.text.trim().toString()
        database = Firebase.database.reference

        db.collection("cars").document(id).update("name", name, "description", desc)
    }
    private fun initUI() {
        btnSelectImage.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }

        btnUpload.setOnClickListener{
            val imgURI = btnUpload.tag as Uri?
            if(imgURI == null){
                Toast.makeText(this,"Please select image first",Toast.LENGTH_SHORT).show()
            }else{
                FirebaseStorageManager().uploadImage(this,imgURI)
                addUploadRecordToDb(imgURI.toString())
                sendDataToFB()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
            imgPost.setImageURI(uri)
            btnUpload.setTag(uri)
        }
    }
    private fun addUploadRecordToDb(url: String){
        val db = FirebaseFirestore.getInstance()
        val id = xd.text.trim().toString()
        val data = HashMap<String, Any>()
        data["image"] = url

        db.collection("cars").document(id)
            .update(data)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Saved to DB", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
            }
    }

}




