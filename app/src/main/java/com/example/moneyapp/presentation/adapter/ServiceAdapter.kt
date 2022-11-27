package com.example.moneyapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyapp.R
import com.example.moneyapp.domain.entities.Service
import com.example.moneyapp.presentation.ui.fragments.TransferFragment

class ServiceAdapter(
    private val context: Context,
    private val dataset: List<Service>): RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.service_item, parent, false)
        return ServiceViewHolder(adapterLayout)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val item = dataset[position]
        holder.imageView.setImageResource(item.imageId)
        holder.textView.text = context.resources.getString(item.nameId)

        holder.itemView.setOnClickListener{
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            val transactionFragment = fragmentManager.beginTransaction()


            if(dataset[position].nameId == R.string.service_transfer){
                transactionFragment.setReorderingAllowed(true)
                transactionFragment.addToBackStack(null)
                transactionFragment.replace(R.id.fl_layout, TransferFragment())

                transactionFragment.commit()
            }


        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class ServiceViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textView:TextView = view.findViewById(R.id.textService)
        val imageView: ImageView = view.findViewById(R.id.imageCard)
    }

}