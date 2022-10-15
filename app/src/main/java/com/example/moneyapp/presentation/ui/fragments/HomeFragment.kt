package com.example.moneyapp.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyapp.R
import com.example.moneyapp.databinding.FragmentHomeBinding

import com.example.moneyapp.domain.entities.Service
import com.example.moneyapp.presentation.adapter.ServiceAdapter
import com.example.moneyapp.presentation.ui.HomeActivity
import com.example.moneyapp.presentation.ui.HomeActivity.Companion.userAccount
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

    private lateinit var recyclerView: RecyclerView

    private val sharedViewModel: HomeViewModel by activityViewModels{
        HomeViewModelFactory(userAccount)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context?.theme?.applyStyle(R.style.Theme_MoneyApp_HomeFragment, true)
        // Inflate the layout for this fragment
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

        recyclerView = binding?.serviceRecyclerView!!

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val listService = ArrayList<Service>()

        listService.add(Service(R.drawable.ic_transfer,R.string.service_transfer))
        listService.add(Service( R.drawable.ic_bill_payment,R.string.service_bill))
        listService.add(Service(R.drawable.ic_mobile,R.string.service_recharge ))
        listService.add(Service(R.drawable.ic_more, R.string.service_more))

        val serviceAdapter = ServiceAdapter(requireContext(), listService)
        recyclerView.adapter = serviceAdapter

    }

    companion object {

    }
}