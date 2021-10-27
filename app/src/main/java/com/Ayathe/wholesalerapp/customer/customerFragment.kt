package com.Ayathe.wholesalerapp.customerFragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.Ayathe.wholesalerapp.BaseFragment
import com.Ayathe.wholesalerapp.R
import com.Ayathe.wholesalerapp.activites.AddItem
import com.Ayathe.wholesalerapp.data.Car
import com.Ayathe.wholesalerapp.data.User
import com.Ayathe.wholesalerapp.home.CarAdapter
import com.Ayathe.wholesalerapp.home.OnCarItemLongClick
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream
import java.lang.Exception

class customerFragment : BaseFragment() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.customer_fragment, container, false)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout_action -> {
                auth.signOut()
                requireActivity().finish()
            }
            R.id.dodaj -> {
                val intent = Intent(activity, AddItem::class.java)
                startActivity(intent)
            }
        }
        return false
    }
}
