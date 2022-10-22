package com.example.moneyapp.presentation.ui.fragments.operation_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyapp.databinding.FragmentOperationBinding
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.use_cases.UserAccountFactory.Companion.ACCOUNT
import com.example.moneyapp.presentation.adapter.OperationAdapter
import com.example.moneyapp.presentation.viewmodel.HomeViewModel
import com.example.moneyapp.presentation.viewmodel.factory.HomeViewModelFactory
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [OperationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OperationFragment(val selectedCategory: String) : Fragment() {

    private var _binding: FragmentOperationBinding? = null

    private val binding get() = _binding

    private val sharedViewModel:HomeViewModel by activityViewModels {
        HomeViewModelFactory(ACCOUNT)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@OperationFragment
        }

        recyclerView = binding?.recyclerViewOperations!!

        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        retrievedOperationsFromFireBase("receive")
        retrievedOperationsFromFireBase("send")

    }


    private fun retrievedOperationsFromFireBase(q: String) {

        val number:String = sharedViewModel.user.value?.number ?: ""

        val databaseReference:DatabaseReference = FirebaseDatabase.getInstance().reference
        val operationRef = databaseReference.child("Operation")


        operationRef.orderByChild(q).equalTo(number)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    if(snapshot!!.exists()){
                        for(h in snapshot.children){

                            val sum:Any? = h.child("sum").value

                            val nSum : Double = if(sum != null && sum.toString() != "0"){
                                sum.toString().toDouble()
                            }else 0.0


                            val operation = Operation(
                                h.child("send").getValue(String::class.java)!!,
                                h.child("receive").getValue(String::class.java)!!,
                                h.child("time").getValue(String::class.java)!!,
                                nSum
                            )

                            list.add(operation)
                        }

                        list.sort()
                        val operationAdapter = OperationAdapter(number, requireContext(), list)
                        recyclerView.adapter = operationAdapter

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    error.toException()
                }
            })
    }

}