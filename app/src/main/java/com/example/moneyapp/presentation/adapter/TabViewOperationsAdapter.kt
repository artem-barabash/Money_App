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

@Suppress("UNREACHABLE_CODE")
class TabViewOperationsAdapter(fragment: Fragment, private val fragmentList: Array<String>): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> return OperationFragment.newInstance(ALL_OPERATIONS)
            1 -> return OperationFragment.newInstance(INCOME_OPERATIONS)
            else -> return OperationFragment.newInstance(EXPENSE_OPERATIONS)
        }
    }

}