package com.example.moneyapp.data.notifications

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import com.example.moneyapp.BuildConfig
import com.example.moneyapp.R
import com.example.moneyapp.data.PDFFileManager.Companion.FILE_NAME
import com.example.moneyapp.data.PDFFileManager.Companion.PATH_FILE
import com.example.moneyapp.presentation.ui.LoginActivity
import java.io.File

class ReminderBroadcast: BroadcastReceiver() {


    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, p1: Intent?) {

        val file = File(PATH_FILE, FILE_NAME)
        val intent = Intent(Intent.ACTION_VIEW)

        val uri: Uri = context?.let { FileProvider.getUriForFile(it, BuildConfig.APPLICATION_ID + ".provider", file) }!!

        intent.setDataAndType(uri, "application/pdf")
        //intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION


        //intent и pendingIntent прописуем чтобы перейти на MainActivty при клике на уведомление
        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else PendingIntent.FLAG_IMMUTABLE


        //val notificationIntent = Intent(context, LoginActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent,flag)

        val builder: Notification.Builder = Notification.Builder(context, "myCh")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Remind")
            .setContentText("Click to the certificate of bank's client")
            .setAutoCancel(true)
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)


        val notificationManagerCompat = NotificationManagerCompat.from(
            context!!
        )
        notificationManagerCompat.notify(1, builder.build())
    }

    companion object{
        var fileNameUser: String = ""
    }
}