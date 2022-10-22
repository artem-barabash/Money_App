package com.example.moneyapp.presentation.ui.fragments

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyapp.R
import com.example.moneyapp.data.room.AppDatabase
import com.example.moneyapp.databinding.FragmentHomeBinding
import com.example.moneyapp.domain.entities.Card

import com.example.moneyapp.domain.entities.Service
import com.example.moneyapp.domain.use_cases.UserAccount
import com.example.moneyapp.domain.use_cases.UserAccountFactory
import com.example.moneyapp.domain.use_cases.UserAccountFactory.Companion.ACCOUNT
import com.example.moneyapp.domain.use_cases.UserDataApplication
import com.example.moneyapp.presentation.adapter.CardAdapter
import com.example.moneyapp.presentation.adapter.ServiceAdapter

import com.example.moneyapp.presentation.viewmodel.HomeViewModel
import com.example.moneyapp.presentation.viewmodel.factory.HomeViewModelFactory


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding

    private lateinit var recyclerViewService: RecyclerView

    private lateinit var recyclerViewCard: RecyclerView

    private val sharedViewModel: HomeViewModel by activityViewModels{
        HomeViewModelFactory(
            ACCOUNT,
            (activity?.application as UserDataApplication).database.operationDao()
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //TODO нужно подключить тему исключительно на данном фрагменте
        context?.theme!!.applyStyle(R.style.Theme_HomeFragment, false)


       _binding = FragmentHomeBinding.inflate(inflater,container, false)
        val root: View = binding!!.root
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            homeFragment = this@HomeFragment
        }


        sharedViewModel.addOperationFromFireBaseToRoom()


        init()
    }

    private fun init(){
        recyclerViewService = binding?.serviceRecyclerView!!
        recyclerViewService.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val listService = ArrayList<Service>()
        listService.add(Service(R.drawable.ic_transfer,R.string.service_transfer))
        listService.add(Service(R.drawable.ic_bill_payment,R.string.service_bill))
        listService.add(Service(R.drawable.ic_mobile,R.string.service_recharge ))
        listService.add(Service(R.drawable.ic_more, R.string.service_more))

        val serviceAdapter = ServiceAdapter(requireContext(), listService)
        recyclerViewService.adapter = serviceAdapter


        recyclerViewCard = binding?.cardRecyclerView!!
        recyclerViewCard.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val listCard = ArrayList<Card>()
        listCard.add(Card("master", ACCOUNT.number, "02/25", ACCOUNT.user.balance))

        val cardAdapter = CardAdapter(requireContext(), listCard)
        recyclerViewCard.adapter = cardAdapter


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}