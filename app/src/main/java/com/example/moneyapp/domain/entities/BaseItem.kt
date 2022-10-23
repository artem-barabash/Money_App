package com.example.moneyapp.domain.entities

import com.example.moneyapp.presentation.adapter.TransactionAdapter
import com.example.moneyapp.presentation.adapter.TransactionViewHolder

interface BaseItem {
    val layoutId: Int

    // Used to compare items when diffing so RecyclerView knows how to animate
    val uniqueId: Any

    /**
     * @param itemClickCallback Optional click callback for clicks on the whole item
     * */
    fun bind(holder: TransactionViewHolder, itemClickCallback: ((Operation) -> Unit)?)

    // Make sure implementations implement equals function (data classes do already)
    override fun equals(other: Any?): Boolean
}