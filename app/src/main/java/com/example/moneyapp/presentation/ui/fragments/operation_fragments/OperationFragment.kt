@file:OptIn(DelicateCoroutinesApi::class)

package com.example.moneyapp.presentation.ui.fragments.operation_fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyapp.databinding.FragmentOperationBinding
import com.example.moneyapp.domain.constant.CategoryOperations
import com.example.moneyapp.domain.entities.BaseItem
import com.example.moneyapp.domain.entities.HeaderItem
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.entities.TransactionsItem
import com.example.moneyapp.domain.use_cases.UserAccountFactory.Companion.ACCOUNT
import com.example.moneyapp.domain.use_cases.UserDataApplication
import com.example.moneyapp.presentation.adapter.OperationAdapter
import com.example.moneyapp.presentation.adapter.TransactionAdapter
import com.example.moneyapp.presentation.viewmodel.HomeViewModel
import com.example.moneyapp.presentation.viewmodel.factory.HomeViewModelFactory
import com.google.firebase.database.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [OperationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OperationFragment(private val selectedCategory: String) : Fragment() {

    private var _binding: FragmentOperationBinding? = null

    private val binding get() = _binding

    private val sharedViewModel:HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            ACCOUNT,
            (activity?.application as UserDataApplication).database.operationDao()
        )
    }

    lateinit var recyclerView: RecyclerView

    val list = ArrayList<Operation>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       _binding = FragmentOperationBinding.inflate(inflater, container, false)


        val root = binding!!.root
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@OperationFragment
        }



        initRecyclerView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRecyclerView() {
        val number:String = sharedViewModel.user.value?.number ?: ""

        recyclerView = binding?.recyclerViewOperations!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val transactionAdapter = TransactionAdapter {}

        recyclerView.adapter = transactionAdapter

        lifecycleScope.launch{

            when(selectedCategory){
                CategoryOperations.INCOME_OPERATIONS ->
                    sharedViewModel.getOperationsIncome(number).collect() { it ->
                        transactionAdapter.submitList(createListGroupByDay(it))
                    }
                CategoryOperations.EXPENSE_OPERATIONS ->
                    sharedViewModel.getOperationsExpense(number).collect() { it ->
                    transactionAdapter.submitList(createListGroupByDay(it))
                }
                else ->
                    sharedViewModel.getOperationsAll(number).collect() { it ->
                    transactionAdapter.submitList(createListGroupByDay(it))
                }

            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createListGroupByDay(operationList: List<Operation>): MutableList<BaseItem>? {
        //сразу идет соортировка в порядке убывания sortedByDescending
        val operationsItems = operationList.map { TransactionsItem(it) }.sortedByDescending{ it.operation.time }

        val transactionsWithHeaders = mutableListOf<BaseItem>()

        var currentHeader:String? = null

        operationsItems.forEach { element ->
            element.operation.time.let { it ->
                if(it.split(" ")[0] != currentHeader){
                    transactionsWithHeaders.add(HeaderItem(it.split(" ")[0]))
                    currentHeader = it.split(" ")[0]
                }
            }


            transactionsWithHeaders.add(element)
        }

        return  transactionsWithHeaders

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}