package com.example.moneyapp.presentation.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.moneyapp.R
import com.example.moneyapp.databinding.FragmentAccountBinding
import com.example.moneyapp.domain.use_cases.UserAccountFactory.Companion.ACCOUNT
import com.example.moneyapp.domain.use_cases.UserDataApplication
import com.example.moneyapp.presentation.viewmodel.HomeViewModel
import com.example.moneyapp.presentation.viewmodel.factory.HomeViewModelFactory
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding

    lateinit var langContext: Context

    private val sharedViewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            ACCOUNT,
            (activity?.application as UserDataApplication).database.operationDao()
        )
    }

    private var showNumber: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        val root:View = binding!!.root

        return  root
    }

    @SuppressLint("UseRequireInsteadOfGet")
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

    /*override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.locale == Locale.ENGLISH) {
            Toast.makeText(requireContext(), "English", Toast.LENGTH_SHORT).show();
        } else if (newConfig.locale == Locale.getDefault()){
            Toast.makeText(requireContext(), "Ukrainian", Toast.LENGTH_SHORT).show();
        }
    }*/

    private fun restartActivityInLanguage(language: String) {
        val locale = Locale(language)
        val config = Configuration()
        config.locale = locale
        val resources: Resources = resources
        resources.updateConfiguration(config, resources.displayMetrics)
        println("languale=" + config.locale)
        requireActivity().recreate()
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