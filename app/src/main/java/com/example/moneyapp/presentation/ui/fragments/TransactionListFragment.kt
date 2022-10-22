package com.example.moneyapp.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.activityViewModels
import com.example.moneyapp.R
import com.example.moneyapp.databinding.FragmentTransactionListBinding
import com.example.moneyapp.domain.use_cases.UserAccountFactory.Companion.ACCOUNT
import com.example.moneyapp.presentation.adapter.TabViewOperationsAdapter
import com.example.moneyapp.presentation.viewmodel.HomeViewModel
import com.example.moneyapp.presentation.viewmodel.factory.HomeViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class TransactionListFragment : Fragment() {

    private var _binding: FragmentTransactionListBinding? = null
    private val binding get() = _binding

    private val labels = arrayOf("All", "Income", "Expense")




    private val sharedViewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(ACCOUNT)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTransactionListBinding.inflate(inflater, container, false)

        val root: View = binding!!.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            //viewModel = sharedViewModel
            fragment = this@TransactionListFragment
        }

        val tabLayoutFragments = binding?.tabLayoutTransactions
        val viewPagerFragments = binding?.viewPagerTabTransaction

        val tabViewOperationsAdapter = TabViewOperationsAdapter(this, labels)
        viewPagerFragments?.adapter = tabViewOperationsAdapter


        tabLayoutFragments?.let {
            viewPagerFragments?.let { it1 ->
                TabLayoutMediator(it, it1){ tab, position ->
                    tab.text = labels[position]
                }.attach()
            }
        }

        setMarginToTabs(tabLayoutFragments)

    }



    private fun setMarginToTabs(tabLayout: TabLayout?) {
        for (i in 0 until tabLayout?.tabCount!!){
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(0, 0, 15, 0)
            tab.requestLayout()
        }

        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_income_arrow)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_expense__arrow)

    }

    fun showRecentlyTransactions(){}

    fun showSelectedTime(){}


}