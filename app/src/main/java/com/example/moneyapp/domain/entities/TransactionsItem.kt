package com.example.moneyapp.domain.entities

import android.annotation.SuppressLint
import android.graphics.Color
import com.example.moneyapp.R
import com.example.moneyapp.domain.use_cases.UserAccountFactory
import com.example.moneyapp.presentation.adapter.TransactionAdapter
import com.example.moneyapp.presentation.adapter.TransactionViewHolder
import kotlinx.android.synthetic.main.operation_item.*
import org.checkerframework.checker.units.qual.C
import java.text.NumberFormat
import java.util.*

data class TransactionsItem(val operation: Operation): BaseItem {
    override val layoutId = R.layout.transaction_item

    override val uniqueId: Any = operation.time

    @SuppressLint("SetTextI18n")
    override fun bind(
        holder: TransactionViewHolder,
        itemClickCallback: ((Operation) -> Unit)?
    ) {
        if (operation.receive == UserAccountFactory.ACCOUNT.number) {
            holder.imageViewItem.setImageResource(R.drawable.ic_type_recieve)

            holder.name_operation.text = operation.send
            holder.name_operation.setTextColor(Color.rgb(35, 135, 0))

            val sum = NumberFormat.getCurrencyInstance(Locale("en", "US")).format(operation.sum)
            holder.textSum.text = sum
            holder.textSum.setTextColor(Color.rgb(35, 135, 0))
        } else {
            holder.imageViewItem.setImageResource(R.drawable.ic_type_sent)

            holder.name_operation.text = operation.receive
            holder.name_operation.setTextColor(Color.rgb(231, 223,255))


            val sum = NumberFormat.getCurrencyInstance(Locale("en", "US")).format(operation.sum)
            holder.textSum.text = "-$sum"
            holder.textSum.setTextColor(Color.rgb(231, 223,255))
        }
    }
}