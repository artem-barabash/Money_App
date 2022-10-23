package com.example.moneyapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView


import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyapp.R
import com.example.moneyapp.databinding.OperationItemBinding
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.use_cases.UserAccountFactory.Companion.ACCOUNT

import java.text.NumberFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class OperationAdapter(private val onItemClicked: (Operation) -> Unit) :
    ListAdapter<Operation, OperationAdapter.OperationViewHolder>(DiffCallback) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        val viewHolder = OperationViewHolder(
            OperationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        return viewHolder
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {

        holder.bind(getItem(position))
    }



    class OperationViewHolder(private var binding: OperationItemBinding): RecyclerView.ViewHolder(binding.root) {
        /*val imageOperation:ImageView = view.findViewById(R.id.imageViewItem)
        val nameOperation:TextView = view.findViewById(R.id.name_operation)
        val balanceOperation:TextView = view.findViewById(R.id.textSum)*/
        fun bind(operation: Operation){
            if(operation.receive == ACCOUNT.number){
                binding.imageViewItem.setImageResource(R.drawable.ic_type_recieve)

                binding.nameOperation.text = operation.send
                binding.nameOperation.setTextColor(Color.rgb(35, 135, 0))

                val sum = NumberFormat.getCurrencyInstance(Locale("en", "US")).format(operation.sum)
                binding.textSum.text = sum
                binding.textSum.setTextColor(Color.rgb(35, 135, 0))
            }else{
                binding.imageViewItem.setImageResource(R.drawable.ic_type_sent)

                binding.nameOperation.text = operation.receive
                binding.nameOperation.setTextColor(Color.rgb(231, 223,255))

                val sum = NumberFormat.getCurrencyInstance(Locale("en", "US")).format(operation.sum)
                binding.textSum.text = "-$sum"
                binding.textSum.setTextColor(Color.rgb(231, 223,255))
            }
        }
    }

    companion object{
        private val DiffCallback = object: DiffUtil.ItemCallback<Operation>(){
            override fun areItemsTheSame(oldItem: Operation, newItem: Operation): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Operation, newItem: Operation): Boolean {
                return oldItem == newItem
            }

        }
    }

}