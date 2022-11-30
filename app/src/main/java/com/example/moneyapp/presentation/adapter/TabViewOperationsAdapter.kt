package com.example.moneyapp.presentation.adapter


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.moneyapp.domain.constant.CategoryOperations.Companion.ALL_OPERATIONS
import com.example.moneyapp.domain.constant.CategoryOperations.Companion.EXPENSE_OPERATIONS
import com.example.moneyapp.domain.constant.CategoryOperations.Companion.INCOME_OPERATIONS
import com.example.moneyapp.presentation.ui.fragments.operation_fragments.OperationFragment
import com.example.moneyapp.presentation.ui.fragments.operation_fragments.OperationFragment.Companion.CATEGORY_KEY
import com.example.moneyapp.presentation.ui.fragments.tab_fragments.ServiceMenuFragment
import com.example.moneyapp.presentation.ui.fragments.tab_fragments.TransactionsTabListFragment

class TabViewOperationsAdapter(fragment: Fragment, private val fragmentList: Array<String>): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                val bundle = Bundle()
                bundle.putString(CATEGORY_KEY, ALL_OPERATIONS)
                val fragment = OperationFragment()
                fragment.arguments = bundle

                return fragment
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString(CATEGORY_KEY, INCOME_OPERATIONS)
                val fragment = OperationFragment()
                fragment.arguments = bundle

                return fragment
            }
            else -> {
                val bundle = Bundle()
                bundle.putString(CATEGORY_KEY, EXPENSE_OPERATIONS)
                val fragment = OperationFragment()
                fragment.arguments = bundle

                return fragment
            }
        }

    }

}