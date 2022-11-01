package com.example.moneyapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyapp.R
import com.example.moneyapp.databinding.CardItemBinding
import com.example.moneyapp.domain.entities.Card
import java.text.NumberFormat


class CardAdapter(
    private val context: Context,
    private val dataset:List<Card?>): RecyclerView.Adapter<CardAdapter.CardViewHolder> (){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return CardViewHolder(adapterLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = dataset[position]

        holder.imageCard.setImageResource(if(item!!.type == "visa") R.drawable.ic_visa else R.drawable.ic_master)

        holder.textTypeCard.text = context.resources.getString(if(item.type == "visa") R.string.visa_txt else R.string.master_text)

        holder.textNumberCard.text = "**" + item.number.substring(item.number.length - 4)

        //holder.textBalanceCard.text = NumberFormat.getCurrencyInstance().format(item.balance)

        holder.textDateCard.text = item.date

    }

    override fun getItemCount(): Int {
        return dataset.size
    }



    class CardViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageCard:ImageView = view.findViewById(R.id.imageCard)
        val textTypeCard: TextView = view.findViewById(R.id.textCardTypeName)
        val textBalanceCard:TextView = view.findViewById(R.id.textBalanceCard)
        val textNumberCard:TextView = view.findViewById(R.id.textNumberCard)
        val textDateCard:TextView = view.findViewById(R.id.textDateCard)
    }


}