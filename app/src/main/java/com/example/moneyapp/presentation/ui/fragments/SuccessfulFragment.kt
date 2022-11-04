package com.example.moneyapp.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.moneyapp.R
import com.example.moneyapp.databinding.FragmentSuccessfulBinding
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.use_cases.SingleTransactionFactory
import com.example.moneyapp.domain.use_cases.UserAccountFactory
import com.example.moneyapp.domain.use_cases.UserDataApplication
import com.example.moneyapp.presentation.viewmodel.HomeViewModel
import com.example.moneyapp.presentation.viewmodel.factory.HomeViewModelFactory
import com.google.firebase.database.*
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [SuccessfulFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SuccessfulFragment : Fragment() {

    private var _binding: FragmentSuccessfulBinding? = null
    private val binding get() = _binding

    lateinit var operation: Operation

    private val sharedViewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            UserAccountFactory.ACCOUNT,
            (activity?.application as UserDataApplication).database.operationDao()
        )
    }

    private val databaseReference: DatabaseReference= FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSuccessfulBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            fragment = this@SuccessfulFragment
        }

        operation = SingleTransactionFactory.OPERATION

        binding!!.texDetailNumberCard.text = sharedViewModel.showCardNumber(operation.receive)
        binding!!.textDetailSum.text = NumberFormat.getCurrencyInstance().format(operation.sum)

        val pattern = "yyyy-MM-dd HH:mm:ss.SSS"
        val formatter = DateTimeFormatter.ofPattern(pattern)

        val localDateTime = LocalDateTime.parse(operation.correctDateAndTime(this.operation.time), formatter)

        val dayOfWeek = String.format(
            "%s%s",
            localDateTime.dayOfWeek.toString().substring(0, 1),
            localDateTime.dayOfWeek.toString().substring(1).lowercase(
                Locale.getDefault()
            )
        )
        val month = String.format(
            "%s%s",
            localDateTime.month.toString().substring(0, 1),
            localDateTime.month.toString().substring(1).lowercase(
                Locale.getDefault()
            )
        )

        binding!!.textDetailDate.text = dayOfWeek + ", " + localDateTime.dayOfMonth + " " + month + " " + localDateTime.year

        binding!!.textDetailTime.text = "Time\n${localDateTime.hour}.${localDateTime.minute}"
        
        
        getFullNameFromFireBase()
    }

    private fun getFullNameFromFireBase() {
        val usersRef: DatabaseReference = databaseReference.child("User")

        val query: Query = usersRef.orderByKey().equalTo(SingleTransactionFactory.OPERATION.receive)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children){
                    val firstNameRecipient =
                        itemSnapshot.child("firstName").getValue(String::class.java)!!
                    val lastNameRecipient =
                        itemSnapshot.child("lastName").getValue(String::class.java)!!
                    binding!!.textDetailName.text = "$firstNameRecipient $lastNameRecipient"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException()
            }
        })
    }


    fun backToHome(){
        val fragmentManager = (context as AppCompatActivity).supportFragmentManager
        val transactionFragment = fragmentManager.beginTransaction()

        transactionFragment.replace(R.id.fl_layout, HomeFragment())

        transactionFragment.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}