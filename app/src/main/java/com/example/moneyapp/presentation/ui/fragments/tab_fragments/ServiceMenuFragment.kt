package com.example.moneyapp.presentation.ui.fragments.tab_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.moneyapp.R
import com.example.moneyapp.databinding.FragmentServiceMenuBinding
import com.example.moneyapp.domain.use_cases.UserAccountFactory.Companion.ACCOUNT
import com.example.moneyapp.presentation.viewmodel.HomeViewModel
import com.example.moneyapp.presentation.viewmodel.factory.HomeViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [ServiceMenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ServiceMenuFragment : Fragment() {

    private var _binding: FragmentServiceMenuBinding? = null
    private val binding get() = _binding

    private val sharedModelView: HomeViewModel by activityViewModels{
        HomeViewModelFactory(ACCOUNT)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentServiceMenuBinding.inflate(inflater, container, false)
        val root = binding!!.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@ServiceMenuFragment
        }
    }

    fun enterToShowStatement(){}

    fun enterToChangePinCode(){}

    fun enterToRemoveCard(){}

}