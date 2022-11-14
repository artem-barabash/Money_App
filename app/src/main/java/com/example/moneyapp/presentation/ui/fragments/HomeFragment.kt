package com.example.moneyapp.presentation.ui.fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyapp.R
import com.example.moneyapp.databinding.FragmentHomeBinding
import com.example.moneyapp.databinding.MyLoadingLayoutBinding
import com.example.moneyapp.domain.entities.*
import com.example.moneyapp.domain.use_cases.UserAccountFactory.Companion.ACCOUNT
import com.example.moneyapp.domain.use_cases.UserDataApplication
import com.example.moneyapp.presentation.adapter.CardAdapter
import com.example.moneyapp.presentation.adapter.ServiceAdapter
import com.example.moneyapp.presentation.adapter.TransactionAdapter
import com.example.moneyapp.presentation.viewmodel.HomeViewModel
import com.example.moneyapp.presentation.viewmodel.factory.HomeViewModelFactory
import kotlinx.coroutines.*


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

    private lateinit var cardAdapter: CardAdapter

    private lateinit var transactionRecyclerView: RecyclerView


    private val sharedViewModel: HomeViewModel by activityViewModels{
        HomeViewModelFactory(
            ACCOUNT,
            (activity?.application as UserDataApplication).database.operationDao()
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //TODO нужно подключить тему исключительно на данном фрагменте
        //context?.theme!!.applyStyle(R.style.Theme_HomeFragment, false)


       _binding = FragmentHomeBinding.inflate(inflater,container, false)
        val root: View = binding!!.root


        //val cardAdapter = CardAdapter(requireContext(), listCard)
        //recyclerViewCard.adapter = cardAdapter

        recyclerViewCard = binding?.cardRecyclerView!!
        recyclerViewCard.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)




        viewLifecycleOwner.lifecycleScope.launch() {
            sharedViewModel.cardList.observe(viewLifecycleOwner, Observer {
                cardAdapter = CardAdapter(requireContext(), it)
                recyclerViewCard.adapter = cardAdapter
            })
        }


        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            homeFragment = this@HomeFragment
        }




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


        //val listCard = ArrayList<Card>()
        //listCard.add(Card("master", ACCOUNT.number, "02/25", sharedViewModel.user.value?.user!!.balance))


        transactionRecyclerView = binding?.transactionRecyclerView!!
        transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        val transactionAdapter = TransactionAdapter{}



        transactionRecyclerView.adapter = transactionAdapter

        viewLifecycleOwner.lifecycleScope.launch(){
            //binding!!.progressBar.progress = View.VISIBLE
            delay(1000)

            sharedViewModel.getOperationsAll().collect(){ it ->
                transactionAdapter.submitList(createSortedList(it)?.take(2))

            }



        }


    }

    private fun createSortedList(operationList: List<Operation>) : MutableList<BaseItem>?{
        //сразу идет соортировка в порядке убывания sortedByDescending
        val operationsItems = operationList.map { TransactionsItem(it, TransactionAdapter.FIRST_ELEMENT) }.sortedByDescending{ it.operation.time }

        val transactions = mutableListOf<BaseItem>()

        for(element in operationList.indices){
            if(element % 2 != 0){
                transactions.add(TransactionsItem(operationsItems[element].operation, TransactionAdapter.LAST_ELEMENT))
            }

            transactions.add(TransactionsItem(operationsItems[element].operation, TransactionAdapter.FIRST_ELEMENT))

        }

        return  transactions

    }

    override fun onStart() {
        super.onStart()



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}