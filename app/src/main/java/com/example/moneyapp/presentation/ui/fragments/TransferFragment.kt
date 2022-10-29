package com.example.moneyapp.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyapp.R
import com.example.moneyapp.databinding.FragmentTransferBinding
import com.example.moneyapp.domain.entities.Person
import com.example.moneyapp.domain.use_cases.UserAccountFactory.Companion.ACCOUNT
import com.example.moneyapp.domain.use_cases.UserDataApplication
import com.example.moneyapp.presentation.adapter.PersonAdapter
import com.example.moneyapp.presentation.adapter.TransferViewPagerAdapter
import com.example.moneyapp.presentation.viewmodel.HomeViewModel
import com.example.moneyapp.presentation.viewmodel.factory.HomeViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator


/**
 * A simple [Fragment] subclass.
 * Use the [TransferFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransferFragment : Fragment()  {

    private var _binding: FragmentTransferBinding? = null
    private val binding get() = _binding

    private lateinit var personRecyclerView: RecyclerView

    private val sharedViewModel:HomeViewModel by activityViewModels {
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
        _binding = FragmentTransferBinding.inflate(inflater, container, false)

        val root:View = binding!!.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply{
            lifecycleOwner = viewLifecycleOwner
            fragment = this@TransferFragment
            viewModel = sharedViewModel
        }

        val transferTab = binding?.tabLayoutTransfer
        val transferViewPager = binding?.viewPagerTransfer

        val list = ArrayList<String>()
        list.add(ACCOUNT.number)
        list.add(ACCOUNT.number)

        transferViewPager?.adapter = TransferViewPagerAdapter(list)

        transferTab?.let {
            transferViewPager?.let { it1 ->
                TabLayoutMediator(it, it1){ tab, position ->

                }.attach()
            }
        }

        personRecyclerView = binding!!.personRecyclerView

        personRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val persons = ArrayList<Person>()
        persons.add(Person("000", "Ivan", "Ivan"))
        persons.add(Person("000", "Pedro", "Pedro"))

        personRecyclerView.adapter = PersonAdapter(requireContext(), persons)

        val spinner: Spinner = binding!!.spinnerPurpose

        ArrayAdapter.createFromResource(requireContext(),
            R.array.pay_services_array,
            R.layout.spinner_list_selected
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_list_item)
            spinner.adapter = adapter
        }

        //selected item
        spinner.selectedItem.toString()
    }


    override fun onStart() {
        super.onStart()
        sharedViewModel.closeTransaction()
    }

    fun backToHome(){
        val fragmentManager = (requireContext() as AppCompatActivity).supportFragmentManager
        val transactionFragment = fragmentManager.beginTransaction()

        transactionFragment.replace(R.id.fl_layout, HomeFragment())

        transactionFragment.commit()
    }

    fun clickToPayment(){
        val editText = EditText(requireContext())

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.app_name)
            .setMessage(R.string.recipient_number)
            .setIcon(R.drawable.ic_baseline_paid_24)
            .setView(editText)
            .setPositiveButton("Ok"
            ) { p0, p1 -> sharedViewModel.checkNumberInFirebase(editText.text.toString()) }
            .setNegativeButton("Cancel", null)
        dialog.show()
    }

    fun sendMoney(){
        val sum = binding!!.editTextSum.text.toString().toDouble()

        if(sum != 0.0 && sharedViewModel.user.value!!.user.balance >= sum){
            sharedViewModel.sendToRecipient(sum)
        }else{
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle(R.string.app_name)
                .setMessage(R.string.failure_payment)
                .setIcon(R.drawable.ic_baseline_paid_24)
                .setNegativeButton("Cancel", null)
            dialog.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}