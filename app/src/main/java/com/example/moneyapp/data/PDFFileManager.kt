package com.example.moneyapp.data

import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build


import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.entities.User
import com.example.moneyapp.domain.use_cases.UserAccount
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class PDFFileManager {

     @RequiresApi(Build.VERSION_CODES.O)
     fun  createUserPdf(userAccount: UserAccount){
        val document = PdfDocument()
        // crate a page description
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        // start a page
        val page = document.startPage(pageInfo)

        val canvas = page.canvas

        val paint = Paint()

        paint.color = Color.BLACK

        canvas.drawText("The certificate of bank's client", 50f, 20f, paint)


        canvas.drawText("First name: ${userAccount.user.firstName}", 10f, 50f, paint)
        canvas.drawText("Last name: ${userAccount.user.lastName}", 10f, 70f, paint)
        canvas.drawText("Gender: ${userAccount.user.gender}", 10f, 90f, paint)
        canvas.drawText("Birthday: ${userAccount.user.birthday}", 10f, 110f, paint)

        canvas.drawText("Home Address: ${userAccount.user.homeAddress}", 10f, 130f, paint)

        canvas.drawText("Email: ${userAccount.user.email}", 10f, 150f, paint)
        canvas.drawText("Phone: ${userAccount.user.phone}", 10f, 170f, paint)
        canvas.drawText("Password: ${userAccount.password}", 10f, 190f, paint)

        canvas.drawText("Client is got number: ${userAccount.number}.", 10f, 210f, paint)

        canvas.drawText("Date: ${LocalDate.now()}.", 10f, 230f, paint)



        document.finishPage(page)


        val file = File(PATH_FILE)
        if (!file.exists()) {
            file.mkdirs()
        }

        FILE_NAME = userAccount.user.firstName + "_" + userAccount.user.lastName + ".pdf"

        val filePath = File(PATH_FILE, FILE_NAME)

        try {

            document.writeTo(FileOutputStream(filePath))
            //Toast.makeText(this, "Done", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Log.e("main", "error $e")
            //Toast.makeText(this, "Something wrong: $e", Toast.LENGTH_LONG).show()
        }
        // close the document
        document.close()
    }



   @RequiresApi(Build.VERSION_CODES.O)
   fun createOperationPDF(operation: Operation, user: User, firstNameRecipient: String, lastNameRecipient: String){
      val document = PdfDocument()
      // crate a page description
      val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
      // start a page
      val page = document.startPage(pageInfo)

      val canvas = page.canvas

      val paint = Paint()

      paint.color = Color.BLACK



      canvas.drawText("The operation", 50f, 20f, paint)


      canvas.drawText("Send: " + showCardNumber(operation.send) + " ${user.firstName} ${user.lastName} .", 10f, 50f, paint)
      canvas.drawText("Receive: " + showCardNumber(operation.send) + " $firstNameRecipient $lastNameRecipient .", 10f, 70f, paint)
      canvas.drawText("Sum: $${operation.sum}", 10f, 90f, paint)
      canvas.drawText("Time: ${operation.time}", 10f, 120f, paint)

      document.finishPage(page)

      val file = File(PATH_FILE)

      if(!file.exists()){
         file.mkdirs()
      }

      val pattern = "yyyy-MM-dd HH:mm:ss.SSS"
      val formatter = DateTimeFormatter.ofPattern(pattern)

      val localDateTime = LocalDateTime.parse(operation.correctDateAndTime(operation.time), formatter)

      FILE_NAME_OPERATION = "${operation.send}_${localDateTime.year}_${localDateTime.monthValue}_${localDateTime.dayOfMonth}_${localDateTime.hour}_${localDateTime.minute}.pdf"

      val filePath = File(PATH_FILE, FILE_NAME_OPERATION)

      try {
          document.writeTo(FileOutputStream(filePath))
      }catch (e: IOException){
         Log.e("main", "error $e")
      }
      document.close()
   }

   private fun showCardNumber(number: String?): String? {
      val arrNumber = number?.split("".toRegex())?.toTypedArray()
      val sb = StringBuilder()
      if (arrNumber != null) {
         for (i in arrNumber.indices) {
            sb.append(arrNumber[i])
            if (i % 4 == 0 && i != arrNumber.size - 1) sb.append(" ")
         }
      }
      return sb.toString()
   }

   companion object{
      public val PATH_FILE: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
      public lateinit var FILE_NAME: String

      public lateinit var FILE_NAME_OPERATION: String
   }

}