package com.example.moneyapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyapp.R
import com.example.moneyapp.domain.entities.Card
import java.text.NumberFormat

class CardViewPagerAdapter(private val cardList: ArrayList<Card>, private val context: Context): RecyclerView.Adapter<CardViewPagerAdapter.ImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = cardList[position]

        holder.imageView.setImageResource(if(item.type == "visa") R.drawable.ic_visa else R.drawable.ic_master)

        holder.textBalanceCard.text = NumberFormat.getCurrencyInstance().format(item.balance)

        holder.textNumberCard.text = getNumberUser(item.number)

        holder.textDateCard.text = item.date
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    private fun getNumberUser(number: String): String = "**** **** **** ${number.substring(number.length - 4)}"

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageCardViewPager);
        val textBalanceCard: TextView = itemView.findViewById(R.id.textCardViewPagerBalance)
        val textNumberCard: TextView = itemView.findViewById(R.id.textCardViewNumber)
        val textDateCard: TextView = itemView.findViewById(R.id.textDateCardViewPager)
    }
}