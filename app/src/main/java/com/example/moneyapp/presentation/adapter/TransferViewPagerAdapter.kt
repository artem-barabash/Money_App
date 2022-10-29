package com.example.moneyapp.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyapp.R

class TransferViewPagerAdapter(private val listTransfers: List<String>): RecyclerView.Adapter<TransferViewPagerAdapter.TransferViewHolder>() {

    class TransferViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textTitle:TextView = view.findViewById(R.id.textNumberTransfer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransferViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transfer_item, parent, false)
        return TransferViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TransferViewHolder, position: Int) {
        val n = listTransfers[position]
        holder.textTitle.text = "**${n.substring(n.length-4)}"
    }

    override fun getItemCount(): Int {
        return listTransfers.size
    }
}