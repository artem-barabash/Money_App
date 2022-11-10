package com.example.moneyapp.presentation.ui.fragments

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.moneyapp.R
import com.example.moneyapp.databinding.FragmentCardBinding

import com.example.moneyapp.domain.entities.Card
import com.example.moneyapp.domain.use_cases.UserAccountFactory.Companion.ACCOUNT
import com.example.moneyapp.domain.use_cases.UserDataApplication
import com.example.moneyapp.presentation.adapter.CardViewPagerAdapter
import com.example.moneyapp.presentation.adapter.TabViewPagerAdapter
import com.example.moneyapp.presentation.viewmodel.HomeViewModel
import com.example.moneyapp.presentation.viewmodel.factory.HomeViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class CardFragment : Fragment() {

    private var _binding: FragmentCardBinding? = null

    private val binding get() = _binding

    private val sharedViewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            ACCOUNT,
            (activity?.application as UserDataApplication).database.operationDao()
        )
    }

    private val labels = arrayOf("Settings", "Transactions")


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCardBinding.inflate(inflater, container, false)

        val root:View = binding!!.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            cardFragment = this@CardFragment
        }

        val listCard = ArrayList<Card>()
        listCard.add(Card("master", ACCOUNT.number, "02/30", sharedViewModel.user.value?.user!!.balance))
        listCard.add(Card("master", ACCOUNT.number, "02/30", sharedViewModel.user.value?.user!!.balance))

        val cardViewPager = binding?.cardViewPager
        val tabLayoutCardViewPager = binding?.tabLayout

        cardViewPager?.adapter = CardViewPagerAdapter(listCard, requireContext())


        tabLayoutCardViewPager?.let {
            cardViewPager?.let { it1 ->
                TabLayoutMediator(it, it1){ tab, position ->

                }.attach()
            }
        }


        initTabViewPager()
    }

    private fun initTabViewPager() {

        val tabLayout = binding?.tabLayoutViewPager


        val viewPager = binding?.viewPagerTabs

        val tabViewPagerAdapter = TabViewPagerAdapter(this, labels)

        viewPager?.adapter = tabViewPagerAdapter

        // remove default elevation of actionbar
        //TODO NEED



        tabLayout?.let {
            viewPager?.let { it1 ->
                TabLayoutMediator(it, it1){ tab, position ->
                    tab.text = labels[position]
                }.attach()
            }
        }




        setMarginToTabs(tabLayout)

        //tabLayout?.getChildAt(1)?.isEnabled  = false
        //применить на TransactionListFragment
        //tabLayout?.getTabAt(0)?.setIcon(R.drawable.ic_master)
    }

    private fun setMarginToTabs(tabLayout: TabLayout?) {
        for (i in 0 until tabLayout?.tabCount!!){
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(0, 0, 30, 0)
            tab.requestLayout()
        }

    }

    fun enterToPayment(){
        Toast.makeText(requireContext(), "Make Payment", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    companion object {

    }
}