package com.example.moneyapp.domain.entities

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.moneyapp.R
import com.example.moneyapp.presentation.adapter.TransactionAdapter
import com.example.moneyapp.presentation.adapter.TransactionViewHolder
import kotlinx.android.synthetic.main.layout_transation_header.*
import java.time.LocalDate

data class HeaderItem(val day: String):BaseItem {
    override val layoutId = R.layout.layout_transation_header

    override val uniqueId = day


    @RequiresApi(Build.VERSION_CODES.O)
    override fun bind(
        holder: TransactionViewHolder,
        itemClickCallback: ((Operation) -> Unit)?
    ) {
        holder.text_header.text = getDayFormat(day)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDayFormat(day: String): String? {
        val dayString = day.split("-")

        val today = LocalDate.now()


        return when (day) {
            today.toString() -> {
                "Today"
            }
            today.minusDays(1).toString() -> {
                "Yesterday"
            }
            else -> {
                dayString[2] + "." + dayString[1] + "." + dayString[0]
            }
        }
    }
}