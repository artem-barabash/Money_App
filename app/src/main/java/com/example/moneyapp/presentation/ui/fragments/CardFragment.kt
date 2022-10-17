package com.example.moneyapp.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.moneyapp.R
import com.example.moneyapp.databinding.FragmentCardBinding

import com.example.moneyapp.domain.entities.Card
import com.example.moneyapp.domain.use_cases.UserAccountFactory
import com.example.moneyapp.domain.use_cases.UserAccountFactory.Companion.ACCOUNT
import com.example.moneyapp.presentation.adapter.CardAdapter
import com.example.moneyapp.presentation.adapter.CardViewPagerAdapter
import com.example.moneyapp.presentation.ui.HomeActivity
import com.example.moneyapp.presentation.viewmodel.HomeViewModel
import com.example.moneyapp.presentation.viewmodel.factory.HomeViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator


class CardFragment : Fragment() {

    private var _binding: FragmentCardBinding? = null

    private val binding get() = _binding

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
        listCard.add(Card("master", ACCOUNT.number, "02/25", ACCOUNT.user.balance))
        listCard.add(Card("master", ACCOUNT.number, "02/25", ACCOUNT.user.balance))

        val cardViewPager = binding?.cardViewPager
        val tabLayoutCardViewPager = binding?.tabLayout

        cardViewPager?.adapter = CardViewPagerAdapter(listCard, requireContext())


        tabLayoutCardViewPager?.let {
            cardViewPager?.let { it1 ->
                TabLayoutMediator(it, it1){ tab, position ->

                }.attach()
            }
        }
    }

    companion object {

    }
}