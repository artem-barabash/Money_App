package com.example.moneyapp.presentation.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.moneyapp.R
import com.example.moneyapp.data.FireBaseManager
import com.example.moneyapp.databinding.ActivityMainBinding
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.entities.User
import com.example.moneyapp.domain.use_cases.UserAccount
import com.example.moneyapp.presentation.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var auth: FirebaseAuth
    lateinit var fireBaseManager: FireBaseManager

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val sharedViewModel: LoginViewModel by viewModels()

    private lateinit var sharedPreferences: SharedPreferences


    @SuppressLint("NewApi", "WrongConstant")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        fireBaseManager = FireBaseManager()

        sharedPreferences = this.getSharedPreferences(TEMP_USER_DATA, MODE_PRIVATE)

        binding.buttonLogin.setOnClickListener{
            if(isOnlineEmulator()){
                loginUserInFireBase()
            }

        }

        binding.buttonRegistration.setOnClickListener{
            if(isOnlineEmulator()){
                val intent = Intent(this, RegistrationActivity::class.java)
                startActivity(intent)
            }

        }


    }

    private fun isOnlineEmulator(): Boolean {
        val online = sharedViewModel.isOnline(this)

        if (!online) Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show()

        return online

    }


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    private fun loginUserInFireBase(){
        if(!binding.editTextEmail.text.toString().isNullOrEmpty() && !binding.editTextPassword.text.toString().isNullOrEmpty()){


            auth.signInWithEmailAndPassword(binding.editTextEmail.text.toString(),
                binding.editTextPassword.text.toString())
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        binding.editTextEmail.backgroundTintList = getColorStateList(R.color.white)
                        binding.editTextPassword.backgroundTintList = getColorStateList(R.color.white)
                        binding.textViewOnError.text = ""


                        getUserDataInFireBase(binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString())



                    }
                }.addOnFailureListener {
                    binding.editTextEmail.backgroundTintList = getColorStateList(R.color.red)
                    binding.editTextPassword.backgroundTintList = getColorStateList(R.color.red)
                    binding.textViewOnError.setText(R.string.failure_auth)
                }

        }else{
            binding.textViewOnError.setText(R.string.message_empty_fields)
        }

    }



    private fun getUserDataInFireBase(email: String, password: String){
        val dataReferenceUser: DatabaseReference = databaseReference.child("User")


        val query: Query = dataReferenceUser.orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("WrongConstant", "ApplySharedPref")
            override  fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {

                    val nBalance: Any? = it.child("balance").value

                    val balance: Double = if((nBalance != null) && (nBalance.toString() != "0")){
                        nBalance as Double
                    }else 0.0


                    val key = it.key.toString()
                    val user = User(
                        it.child("firstName").value.toString(),
                        it.child("lastName").value.toString(),
                        it.child("phone").value.toString(),
                        it.child("email").value.toString(),
                        it.child("birthday").value.toString(),
                        it.child("gender").value.toString(),
                        it.child("homeAddress").value.toString(),
                        balance)

                    lifecycleScope.coroutineContext.let {
                        sharedPreferences.edit().clear().apply()

                        saveDataSharedPreferences(user, key, "", password)
                    }

                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)

                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }

        })
    }


    fun saveDataSharedPreferences(user: User, numberKey: String, imageLink: String, password: String){
        val editor = sharedPreferences.edit()

        editor.putString(FIRST_NAME, user.firstName)
        editor.putString(LAST_NAME, user.lastName)
        editor.putString(PHONE, user.phone)
        editor.putString(EMAIL, user.email)
        editor.putString(BIRTHDAY, user.birthday)
        editor.putString(GENDER, user.gender)
        editor.putString(HOME_ADDRESS, user.homeAddress)

        editor.putString(BALANCE, user.balance.toString())

        editor.putString(PASSWORD, password)
        editor.putString(NUMBER_KEY, numberKey)
        editor.putString(IMAGE_LINK, imageLink)



        editor.commit()
    }

    override fun onStart() {
        super.onStart()

        //при возврате на LoginActivity из HomeActivity
        binding.editTextEmail.text.clear()
        binding.editTextPassword.text.clear()
    }

    companion object{
        const val TEMP_USER_DATA : String = "MySharedPref"

        const val FIRST_NAME: String = "firstNameU"
        const val LAST_NAME: String = "lastNameU"
        const val PHONE: String = "phoneU"
        const val EMAIL: String = "emailU"
        const val BIRTHDAY: String = "birthdayU"
        const val GENDER: String = "genderU"
        const val HOME_ADDRESS: String = "homeAddressU"
        const val BALANCE: String = "balanceU"
        const val PASSWORD: String = "passwordU"
        const val NUMBER_KEY: String = "numberKeyU"
        const val IMAGE_LINK: String = "imageLinkU"
    }
}
