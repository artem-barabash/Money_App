package com.example.moneyapp.presentation.ui

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.moneyapp.BuildConfig
import com.example.moneyapp.R
import com.example.moneyapp.data.PDFFileManager
import com.example.moneyapp.data.PDFFileManager.Companion.FILE_NAME
import com.example.moneyapp.data.PDFFileManager.Companion.PATH_FILE
import com.example.moneyapp.data.Validator
import com.example.moneyapp.data.notifications.ReminderBroadcast
import com.example.moneyapp.databinding.ActivityRegistrationBinding
import com.example.moneyapp.domain.entities.User
import com.example.moneyapp.domain.use_cases.UserAccount
import com.example.moneyapp.presentation.viewmodel.RegistrationViewModel
import java.io.File
import java.time.LocalDate
import java.util.*


class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    private  val sharedViewModel: RegistrationViewModel by viewModels()



    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val textObserver = Observer<String>{ text ->
            binding.textViewFailure.text = text
        }

        sharedViewModel.status.observe(this, textObserver)

        binding.registerButton.setOnClickListener{
            registerUser()

        }

        binding.datePickerButton.setOnClickListener{
            changeDate()
        }

        binding.cancelButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        dateBirthday = LocalDate.now()

        binding.textViewDate.text =
            "${dateBirthday.dayOfMonth}.${dateBirthday.monthValue}.${dateBirthday.year}"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerUser() {

        val gender:String =
            if(binding.radioButtonMale.isChecked) "MALE" else "FEMALE"

        val user = User(
            binding.textFirstName.text.toString(),
            binding.textLastName.text.toString(),
            binding.textPhoneNumber.text.toString(),
            binding.textEmailAddress.text.toString(),
            dateBirthday.toString(),
            gender,
            binding.textHomeAddress.text.toString(),
            0.0
        )
        //println(user)



        if(sharedViewModel.checkUserRegistrationData(user, binding.textPassword.text.toString(),
            binding.textPassword.text.toString())){
            if(Validator.PasswordValidator.methodValidPassword(user, binding.textPassword.text.toString())){
                if(sharedViewModel.comparePassword(binding.textPassword.text.toString(), binding.textRepeatPassword.text.toString())){


                    sharedViewModel.createUserInSystem(user, binding.textPassword.text.toString())


                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)



                    FILE_NAME = "${user.firstName}_${user.lastName}.pdf"


                    openPdf(PATH_FILE, FILE_NAME)

                    }


            }else{
                messageAlert(R.string.password_error)
            }
        }

    }


    private fun messageAlert(message: Int){
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        val dialog = builder.create()
        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    fun changeDate(){
        val date: Calendar = Calendar.getInstance()
        var thisAYear = date.get(Calendar.YEAR).toInt()
        var thisAMonth = date.get(Calendar.MONTH).toInt()
        var thisADay = date.get(Calendar.DAY_OF_MONTH).toInt()

        val dpd = DatePickerDialog(this, OnDateSetListener { view2, thisYear, thisMonth, thisDay ->
            thisAMonth = thisMonth + 1
            thisADay = thisDay
            thisAYear = thisYear

            binding.textViewDate.text = "$thisDay.$thisAMonth.$thisYear"
            dateBirthday = LocalDate.of(thisAYear, thisAMonth, thisADay)
            println(dateBirthday.toString())

            val newDate:Calendar = Calendar.getInstance()
            newDate.set(thisYear, thisMonth, thisDay)
            //mh.entryDate = newDate.timeInMillis // setting new date
        }, thisAYear, thisAMonth, thisADay)



        dpd.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun createNotificationChannel() {
        val intent = Intent(this, ReminderBroadcast::class.java)

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else PendingIntent.FLAG_IMMUTABLE

        val pendingIntent =
            PendingIntent.getBroadcast(this, 1, intent, flag)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                0,
                pendingIntent
            )
        }

    }


    private fun openPdf(path: String, nameFile: String) {
        val file = File(path, nameFile)
        val intent = Intent(Intent.ACTION_VIEW)

        //
        val uri: Uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file)

        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No application found for pdf reader", Toast.LENGTH_SHORT).show()
        }
    }

    companion object{
        lateinit var dateBirthday: LocalDate
    }

}

