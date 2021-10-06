package com.Ayathe.wholesalerapp.home

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.Ayathe.wholesalerapp.BaseFragment
import com.Ayathe.wholesalerapp.R
import com.Ayathe.wholesalerapp.activites.AddItem
import com.Ayathe.wholesalerapp.activites.MainActivity
import com.Ayathe.wholesalerapp.activites.ModifyItem
import com.Ayathe.wholesalerapp.data.Car
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), OnCarItemLongClick {

    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()
    private val homeVm by viewModels<HomeViewModel>()
    private val adapter = CarAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView_home.layoutManager = LinearLayoutManager(requireContext())
        recyclerView_home.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeVm.cars.observe(viewLifecycleOwner, {list ->
            adapter.setCars(list)
        })
    }

    override fun onCarLongClick(car: Car, position: Int) {
        homeVm.addFavCar(car)
    }

    override fun onCarClick(car: Car, position: Int) {

        val intent = Intent(activity, ModifyItem::class.java)
        startActivity(intent)
    }

    
}