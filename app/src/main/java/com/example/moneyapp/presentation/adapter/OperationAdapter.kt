package com.example.moneyapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyapp.R
import com.example.moneyapp.domain.entities.Operation
import java.text.NumberFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class OperationAdapter(
    private val numberUser: String,
    val context: Context,
    val list: List<Operation>
) : RecyclerView.Adapter<OperationAdapter.OperationViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.operation_item, parent,  false)
        return OperationViewHolder(adapterLayout)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val item = list[position]


        if(item.receive == numberUser){
            holder.imageOperation.setImageResource(R.drawable.ic_type_recieve)

            holder.nameOperation.text = item.send
            holder.nameOperation.setTextColor(Color.rgb(35, 135, 0))

            val sum = NumberFormat.getCurrencyInstance(Locale("en", "US")).format(item.sum)
            holder.balanceOperation.text = sum
            holder.balanceOperation.setTextColor(Color.rgb(35, 135, 0))
        }else{
            holder.imageOperation.setImageResource(R.drawable.ic_type_sent)

            holder.nameOperation.text = item.receive
            holder.nameOperation.setTextColor(Color.rgb(72, 41, 168))

            val sum = NumberFormat.getCurrencyInstance(Locale("en", "US")).format(item.sum)
            holder.balanceOperation.text = "-$sum"
            holder.balanceOperation.setTextColor(Color.rgb(72, 41, 168))
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }

    class OperationViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageOperation:ImageView = view.findViewById(R.id.imageViewItem)
        val nameOperation:TextView = view.findViewById(R.id.name_operation)
        val balanceOperation:TextView = view.findViewById(R.id.textSum)
    }

}