package com.example.moneyapp.presentation.adapter

import androidx.fragment.app.Fragment

import androidx.viewpager2.adapter.FragmentStateAdapter

import com.example.moneyapp.presentation.ui.fragments.tab_fragments.ServiceMenuFragment
import com.example.moneyapp.presentation.ui.fragments.tab_fragments.TransactionsTabListFragment


class TabViewPagerAdapter(fragment: Fragment, private val fragmentsList: Array<String>): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return fragmentsList.size
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> {ServiceMenuFragment()}
            1 -> TransactionsTabListFragment()
        }
        return ServiceMenuFragment()
    }
}