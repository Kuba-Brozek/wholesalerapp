package com.Ayathe.wholesalerapp.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.Ayathe.wholesalerapp.R
import com.Ayathe.wholesalerapp.data.Car

class CarAdapter(private val listener: OnCarItemLongClick) :
    RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    private val carsList = ArrayList<Car>()

    fun setCars(list: List<Car>){
        carsList.clear()
        carsList.addAll(list)
        notifyDataSetChanged()
    }

    fun removeCar(car: Car, position: Int){
        carsList.remove(car)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_row, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        bindData(holder)
    }

    private fun bindData(holder: CarViewHolder) {
        val name = holder.itemView.findViewById<TextView>(R.id.carName)
        val description = holder.itemView.findViewById<TextView>(R.id.carProductionYear)
        val image = holder.itemView.findViewById<ImageView>(R.id.carImage)

        name.text = carsList[holder.adapterPosition].name

            description.text = carsList[holder.adapterPosition].description
        Glide.with(holder.itemView)
                .load(carsList[holder.adapterPosition].image)
                .into(image)
    }

    override fun getItemCount(): Int {
       return carsList.size
    }

    inner class CarViewHolder(view: View) : RecyclerView.ViewHolder(view){
        init {
            view.setOnLongClickListener() {
                listener.onCarLongClick(carsList[adapterPosition], adapterPosition)
                true
            }
            view.setOnClickListener() {
                listener.onCarClick(carsList[adapterPosition], adapterPosition)

            }
        }
    }
}
interface OnCarItemLongClick{
    fun onCarLongClick(car: Car, position: Int)
    fun onCarClick(car: Car, position: Int)
}
