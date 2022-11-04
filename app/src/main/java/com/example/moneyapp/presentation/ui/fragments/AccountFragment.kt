package com.example.moneyapp.presentation.ui.fragments

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.moneyapp.R
import com.example.moneyapp.databinding.FragmentAccountBinding
import com.example.moneyapp.domain.use_cases.UserAccountFactory.Companion.ACCOUNT
import com.example.moneyapp.domain.use_cases.UserDataApplication
import com.example.moneyapp.presentation.viewmodel.HomeViewModel
import com.example.moneyapp.presentation.viewmodel.factory.HomeViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding

    private val sharedViewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            ACCOUNT,
            (activity?.application as UserDataApplication).database.operationDao()
        )
    }

    private var showNumber: Boolean = false


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
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        val root:View = binding!!.root

        return  root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            fragment = this@AccountFragment
        }


    }
    override fun onStart() {
        super.onStart()

        binding!!.imageShowUserNumber.setOnClickListener{
            if(!showNumber){
                binding!!.imageShowUserNumber.setImageResource(R.drawable.ic_eye_lookup)
                binding!!.textUserNumber.text = sharedViewModel.showCardNumber(ACCOUNT.number)
                showNumber = true
            } else{
                binding!!.imageShowUserNumber.setImageResource(R.drawable.ic_eye_lookup_cross_out)
                binding!!.textUserNumber.text = getUserNumber()
                showNumber = false
            }
        }
    }

    fun getUserNumber():String{
        val number = ACCOUNT.number


        return "•••• •••• •••• ${number.substring(number.length - 4)}"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

}