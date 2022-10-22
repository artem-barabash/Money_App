package com.example.moneyapp.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyapp.R
import com.example.moneyapp.databinding.OperationItemBinding
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.use_cases.UserAccountFactory.Companion.ACCOUNT
import java.text.NumberFormat
import java.util.*


class TransactionAdapter(private val onItemClicked:(Operation) -> Unit):
    ListAdapter<Operation, TransactionAdapter.TransactionViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val viewHolder = TransactionViewHolder(
            OperationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        return viewHolder
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class TransactionViewHolder(private var binding: OperationItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(operation: Operation) {

            if (operation.receive == ACCOUNT.number) {
                binding.imageViewItem.setImageResource(R.drawable.ic_type_recieve)

                binding.nameOperation.text = operation.send
                binding.nameOperation.setTextColor(Color.rgb(35, 135, 0))

                val sum = NumberFormat.getCurrencyInstance(Locale("en", "US")).format(operation.sum)
                binding.textSum.text = sum
                binding.textSum.setTextColor(Color.rgb(35, 135, 0))
            } else {
                binding.imageViewItem.setImageResource(R.drawable.ic_type_sent)

                binding.nameOperation.text = operation.receive
                binding.nameOperation.setTextColor(Color.rgb(72, 41, 168))

                val sum = NumberFormat.getCurrencyInstance(Locale("en", "US")).format(operation.sum)
                binding.textSum.text = "-$sum"
                binding.textSum.setTextColor(Color.rgb(72, 41, 168))
            }
        }

    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Operation>() {
            override fun areItemsTheSame(oldItem: Operation, newItem: Operation): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Operation, newItem: Operation): Boolean {
                return oldItem == newItem
            }

        }
    }

}

