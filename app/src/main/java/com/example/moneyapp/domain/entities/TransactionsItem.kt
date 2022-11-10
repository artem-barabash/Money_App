package com.example.moneyapp.domain.entities

import android.annotation.SuppressLint
import android.graphics.Color
import com.example.moneyapp.R
import com.example.moneyapp.domain.use_cases.UserAccountFactory
import com.example.moneyapp.presentation.adapter.TransactionAdapter
import com.example.moneyapp.presentation.adapter.TransactionAdapter.Companion.FIRST_ELEMENT
import com.example.moneyapp.presentation.adapter.TransactionAdapter.Companion.LAST_ELEMENT
import com.example.moneyapp.presentation.adapter.TransactionAdapter.Companion.SIMPLE_ELEMENT
import com.example.moneyapp.presentation.adapter.TransactionViewHolder
import kotlinx.android.synthetic.main.operation_item.*
import kotlinx.android.synthetic.main.operation_item.imageViewItem
import kotlinx.android.synthetic.main.operation_item.name_operation
import kotlinx.android.synthetic.main.operation_item.textSum
import kotlinx.android.synthetic.main.transaction_item.*
import org.checkerframework.checker.units.qual.C
import java.text.NumberFormat
import java.util.*

data class TransactionsItem(val operation: Operation, val orderAtList: String): BaseItem {
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
        } else if (operation.send == UserAccountFactory.ACCOUNT.number){
            holder.imageViewItem.setImageResource(R.drawable.ic_type_sent)

            holder.name_operation.text = operation.receive
            holder.name_operation.setTextColor(Color.rgb(231, 223,255))


            val sum = NumberFormat.getCurrencyInstance(Locale("en", "US")).format(operation.sum)
            holder.textSum.text = "-$sum"
            holder.textSum.setTextColor(Color.rgb(231, 223,255))
        }else{
            holder.imageViewItem.setImageResource(R.drawable.ic_salary)

            holder.name_operation.text = operation.receive
            holder.name_operation.setTextColor(Color.rgb(211, 223,255))


            val sum = NumberFormat.getCurrencyInstance(Locale("en", "US")).format(operation.sum)
            holder.textSum.text = sum
            holder.textSum.setTextColor(Color.rgb(211, 223,255))
        }

        when(orderAtList){
            FIRST_ELEMENT -> holder.item_background.setBackgroundResource(R.drawable.operation_item_up)
            SIMPLE_ELEMENT -> holder.item_background.setBackgroundResource(R.drawable.operation_item_simple)
            LAST_ELEMENT -> holder.item_background.setBackgroundResource(R.drawable.operation_item_down)
            else -> holder.item_background.setBackgroundResource(R.drawable.operation_single_item)
        }

    }
}