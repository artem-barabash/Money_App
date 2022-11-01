package com.example.moneyapp.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyapp.R
import com.example.moneyapp.databinding.OperationItemBinding
import com.example.moneyapp.domain.entities.BaseItem
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.use_cases.UserAccountFactory.Companion.ACCOUNT
import java.text.NumberFormat
import java.util.*


class TransactionAdapter(private val onItemClicked:(Operation) -> Unit):
    ListAdapter<BaseItem, TransactionViewHolder>(AsyncDifferConfig.Builder<BaseItem>(object : DiffUtil.ItemCallback<BaseItem>() {
        override fun areItemsTheSame(oldItem: BaseItem, newItem: BaseItem): Boolean {
            return oldItem.uniqueId == newItem.uniqueId
        }

        override fun areContentsTheSame(oldItem: BaseItem, newItem: BaseItem): Boolean {
            return oldItem == newItem
        }
    }).build()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
       getItem(position).bind(holder, onItemClicked)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).layoutId
    }


    companion object{
        const val FIRST_ELEMENT = "FIRST_ELEMENT"

        const val SIMPLE_ELEMENT = "SIMPLE_ELEMENT"

        const val LAST_ELEMENT = "LAST_ELEMENT"

        const val SINGLE_ELEMENT = "SINGLE_ELEMENT"
    }

}

