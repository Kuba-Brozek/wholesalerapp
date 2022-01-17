package com.Ayathe.wholesalerapp.activites

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import com.Ayathe.wholesalerapp.R
import com.Ayathe.wholesalerapp.repository.FirebaseRepository
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_modify_item.*
import java.util.*
import kotlin.collections.ArrayList


class ModifyItem : AppCompatActivity() {

    private val mStorageRef = FirebaseStorage.getInstance().reference
    private lateinit var mProgressDialog: ProgressDialog
    private val REQUEST_IMAGE_CAPTURE = 1
    private val repository = FirebaseRepository()
    private val db = Firebase.firestore
    private val PROFILE_DEBUG = "PROFILE_DEBUG"
    private val storage = FirebaseStorage.getInstance()
    private lateinit var database: DatabaseReference
    private var filePath: Uri? = null
    private var storageRef = storage.reference
    private var storageReference: StorageReference? = null
    private val btnSelectImage: AppCompatButton by lazy {
        findViewById(R.id.btn_choose_image) }
    private val imgPost: AppCompatImageView by lazy {
        findViewById(R.id.moditemimg) }
    private val btnUpload: AppCompatButton by lazy {
        findViewById(R.id.btn_upload_image) }

    var data: MutableList<String> = ArrayList()
    private var datadisplay: MutableList<String> = ArrayList()
    private lateinit var option: Spinner


    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_item)

        val carname: String = intent.getStringExtra("carname").toString()
        val cardesc: String = intent.getStringExtra("cardesc").toString()
        val carid: String = intent.getStringExtra("carid").toString()
        val carimg: String = intent.getStringExtra("carimg").toString()

        //val user = auth.currentUser?.uid
        initUI()
        moditemname.setText(carname)
        moditemdesc.setText(cardesc)
        xd.text = carid
        showAlertDialog()
        loadImage()
        arrayOfClients()

        option = findViewById(R.id.spinner)

        option.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, datadisplay)

        option.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val choice: String = parent?.getItemAtPosition(position).toString()
                Toast.makeText(applicationContext, choice, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }




    private fun arrayOfClients(): MutableList<String>{
        db.collection("client")
            .whereEqualTo("client", true)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    data.add( "ID Klienta: ${document.id}, dane klienta: ${document.data}")
                    datadisplay.add( "Dane klienta: ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
        return datadisplay
    }

    private fun showAlertDialog(){
        submitcar.setOnClickListener {
        MaterialAlertDialogBuilder(this).setTitle("Alert").setMessage("Are you sure you want to delete that item?")
            .setNegativeButton("No"){dialog, which -> }
            .setPositiveButton("Delete"){dialog, which->
                deleteItem()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
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
                    try {
                        val carid: String = intent.getStringExtra("carid").toString()
                        val carimgpng = "posts/$carid.png"
                        val message = ""
                        storageRef.child(carimgpng).delete().addOnSuccessListener { }
                    } catch (e: Exception) { }
                    }
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
                sendDataToFB()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

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
            val uri: Uri = data?.data!!
            imgPost.setImageURI(uri)
            btnUpload.tag = uri
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
            .addOnFailureListener {
                Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
            }
    }
    private fun uploadImage(context: Context, imageFileUri: Uri) {

        mProgressDialog = ProgressDialog(context)
        mProgressDialog.setMessage("Please wait, image being upload")
        mProgressDialog.show()

        val date = Date()
        val carid: String = intent.getStringExtra("carid").toString()
        val uploadTask = mStorageRef.child("posts/${carid}.png").putFile(imageFileUri)

        uploadTask.addOnSuccessListener {
            Log.e("Firebase", "Image Upload success")
            mProgressDialog.dismiss()
            val uploadedURL = mStorageRef.child("posts/${date}.png").downloadUrl
            Log.e("Firebase", "Uploaded $uploadedURL")
        }.addOnFailureListener {
            Log.e("Firebase", "Image Upload fail")
            mProgressDialog.dismiss()
        }
    }
}
