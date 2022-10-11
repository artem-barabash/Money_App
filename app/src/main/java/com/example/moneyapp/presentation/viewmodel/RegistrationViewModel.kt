package com.example.moneyapp.presentation.viewmodel



import android.R
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyapp.BuildConfig
import com.example.moneyapp.data.FireBaseManager
import com.example.moneyapp.data.PDFFileManager
import com.example.moneyapp.data.Validator
import com.example.moneyapp.domain.entities.User
import com.example.moneyapp.domain.use_cases.UserAccount
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.Period


class RegistrationViewModel : ViewModel() {

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireBaseManager: FireBaseManager = FireBaseManager()


    @RequiresApi(Build.VERSION_CODES.O)
    fun checkUserRegistrationData(user: User, password: String, repeatPassword: String) : Boolean {
        if(user.firstName.isNotEmpty() && user.lastName.isNotEmpty()
            && user.email.isNotEmpty() && user.phone.isNotEmpty()
            && user.birthday.isNotEmpty() && user.homeAddress.isNotEmpty()
            &&  password.isNotEmpty() && repeatPassword.isNotEmpty()){

                if(Validator.methodValidPhoneNumber(user.phone) && Validator.methodValidEmail(user.email)
                    && Validator.ValidatorForPersonality.methodCheckPersonalityData(user.firstName) &&
                    Validator.ValidatorForPersonality.methodCheckPersonalityData(user.lastName)
                    && Validator.methodCheckHomeAddress(user.homeAddress)){

                    return if( Period.between(
                            LocalDate.parse(user.birthday),
                            LocalDate.now(),
                        ).years >= 16){

                        _status.value = ""

                        //при внесении в базу "${dateBirthday.dayOfMonth}.${dateBirthday.monthValue}.${dateBirthday.year}"

                        true
                    }else{
                        _status.value = "The client's age cannot be less than 16 years!"
                        false
                    }

                }else{
                    _status.value = "The user information is not correct!"
                    return false
                }



        }else{
            _status.value = "Fields can not be empty!"
            return false
        }
    }

    fun comparePassword(password: String, repeatPassword: String): Boolean{
        return if(password == repeatPassword)
            true
        else{
            _status.value = "Passwords do not match!"
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createUserInSystem(user: User, password: String) {
        //TODO

        fireBaseManager.addUser(user, password)
        firebaseAuth.createUserWithEmailAndPassword(user.email, password)
    }

}