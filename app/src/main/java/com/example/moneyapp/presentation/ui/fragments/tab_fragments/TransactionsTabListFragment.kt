package com.example.moneyapp.presentation.ui.fragments.tab_fragments

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyapp.R
import com.example.moneyapp.databinding.FragmentOperationBinding
import com.example.moneyapp.databinding.FragmentTransactionTabListBinding
import com.example.moneyapp.domain.constant.CategoryOperations
import com.example.moneyapp.domain.entities.BaseItem
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.entities.TransactionsItem
import com.example.moneyapp.domain.use_cases.UserAccountFactory
import com.example.moneyapp.domain.use_cases.UserDataApplication
import com.example.moneyapp.presentation.adapter.TransactionAdapter
import com.example.moneyapp.presentation.viewmodel.HomeViewModel
import com.example.moneyapp.presentation.viewmodel.factory.HomeViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class TransactionsTabListFragment : Fragment() {


    private var _binding: FragmentTransactionTabListBinding? = null

    private val binding get() = _binding

    private val sharedViewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            UserAccountFactory.ACCOUNT,
            (activity?.application as UserDataApplication).database.operationDao()
        )
    }

    lateinit var recyclerViewOperations: RecyclerView


    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTransactionTabListBinding.inflate(inflater, container, false)

        return  binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            fragment = this@TransactionsTabListFragment
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val number:String = sharedViewModel.user.value?.number ?: ""
        recyclerViewOperations = binding!!.operationRecyclerView
        recyclerViewOperations.layoutManager = LinearLayoutManager(requireContext())

        val transactionAdapter = TransactionAdapter{}

        recyclerViewOperations.adapter = transactionAdapter

        lifecycleScope.launch {
            sharedViewModel.getOperationsAll().collect(){it ->
                transactionAdapter.submitList(createListWithThreeItems(it)?.take(3))
            }
        }

    }

    private fun createListWithThreeItems(list: List<Operation>): MutableList<BaseItem>? {
        val operationsItems = list.map { TransactionsItem(it, TransactionAdapter.FIRST_ELEMENT) }.sortedByDescending{ it.operation.time }

        val transactions = mutableListOf<BaseItem>()
        if(operationsItems.size >=3){
            transactions.add(TransactionsItem(operationsItems[0].operation, TransactionAdapter.FIRST_ELEMENT))
            transactions.add(TransactionsItem(operationsItems[1].operation, TransactionAdapter.SIMPLE_ELEMENT))
            transactions.add(TransactionsItem(operationsItems[2].operation, TransactionAdapter.LAST_ELEMENT))
        }else if(operationsItems.size == 2){
            transactions.add(TransactionsItem(operationsItems[0].operation, TransactionAdapter.FIRST_ELEMENT))
            transactions.add(TransactionsItem(operationsItems[1].operation, TransactionAdapter.LAST_ELEMENT))
        }else if(operationsItems.size == 1){
            transactions.add(TransactionsItem(operationsItems[0].operation, TransactionAdapter.SINGLE_ELEMENT))
        }


        return transactions
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}