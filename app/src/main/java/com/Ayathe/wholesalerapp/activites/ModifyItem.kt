package com.Ayathe.wholesalerapp.activites

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import com.Ayathe.wholesalerapp.R
import com.Ayathe.wholesalerapp.repository.FirebaseRepository
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_modify_item.*
import java.util.*


class ModifyItem : AppCompatActivity() {

    private val REPO_DEBUG = "REPO_DEBUG"
    private val mStorageRef = FirebaseStorage.getInstance().reference
    private lateinit var mProgressDialog: ProgressDialog
    private val REQUEST_IMAGE_CAPTURE = 1
    private val repository = FirebaseRepository()
    val db = Firebase.firestore
    private val PROFILE_DEBUG = "PROFILE_DEBUG"
    private val storage = FirebaseStorage.getInstance()
    private lateinit var database: DatabaseReference
    private var filePath: Uri? = null
    var storageRef = storage.reference
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
        val carimg: String = intent.getStringExtra("carimg").toString()

        initUI()

        moditemname.setText(carname)
        moditemdesc.setText(cardesc)
        xd.setText(carid)

        showAlertDialog()
        loadImage()

    }

    private fun showAlertDialog(){
        submitcar.setOnClickListener {
        MaterialAlertDialogBuilder(this).setTitle("Alert").setMessage("Are you sure you want to delete that item?")
            .setNegativeButton("No"){dialog, which ->

            }
            .setPositiveButton("Delete"){dialog, which->
                deleteItem()
                //DeleteImageFromStorage()
            }.show()
        }
    }

   private fun loadImage(){

       val carid: String = intent.getStringExtra("carid").toString()
       val carimgpng = "posts/$carid.png"
       val car = storageRef.child(carimgpng)
       car.downloadUrl.addOnSuccessListener { Uri ->
           val imageURL = Uri.toString()
       Glide.with(this).load(imageURL).into(imgPost)
   }

   }


    private fun deleteItem() {
        val id = xd.text.trim().toString()


            db.collection("cars").document(id)
                .delete()
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!")


                }
                .addOnFailureListener {  }
    }

    /*private fun DeleteImageFromStorage(){
        val carid: String = intent.getStringExtra("carid").toString()
        val carimgpng = "posts/$carid.png"
        storageRef.child(carimgpng).delete().addOnSuccessListener {
        }.addOnFailureListener{
           MyFailureListener()
       }
    } */

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
                uploadImage(this,imgURI)
                addUploadRecordToDb(imgURI.toString())
                sendDataToFB()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
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
    fun uploadImage(context: Context, imageFileUri: Uri) {
        mProgressDialog = ProgressDialog(context)
        mProgressDialog.setMessage("Please wait, image being upload")
        mProgressDialog.show()
        val date = Date()
        val carid: String = intent.getStringExtra("carid").toString()

        val uploadTask = mStorageRef.child("posts/${carid}.png").putFile(imageFileUri)
        uploadTask.addOnSuccessListener {
            Log.e("Frebase", "Image Upload success")
            mProgressDialog.dismiss()
            val uploadedURL = mStorageRef.child("posts/${date}.png").downloadUrl
            Log.e("Firebase", "Uploaded $uploadedURL")
        }.addOnFailureListener {
            Log.e("Frebase", "Image Upload fail")
            mProgressDialog.dismiss()
        }
    }
    internal inner class MyFailureListener : OnFailureListener {
        override fun onFailure(exception: Exception) {
            val errorCode = (exception as StorageException).errorCode
            val errorMessage = exception.message
            // test the errorCode and errorMessage, and handle accordingly
        }
    }

}




