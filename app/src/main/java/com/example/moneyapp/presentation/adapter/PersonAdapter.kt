package com.example.moneyapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyapp.R
import com.example.moneyapp.domain.entities.Person

class PersonAdapter(val context: Context, private val personList: ArrayList<Person>): RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    class PersonViewHolder(view: View): RecyclerView.ViewHolder(view){
        val namePerson:TextView = view.findViewById(R.id.textPersonName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.person_item, parent, false)
        return PersonViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val item = personList[position]

        holder.namePerson.text = item.firstName
    }

    override fun getItemCount(): Int {
        return personList.size
    }
}