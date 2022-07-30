package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        ceateNotificationChannel()
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            when {
                custom_button.buttonState == ButtonState.Loading -> {
                    Toast.makeText(
                        this,
                        "please wait until the current process is finished",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                radioButton.isChecked -> {
                    URL = getString(R.string.download_url_1)
                    custom_button.buttonState = ButtonState.Loading
                    intentArg_fileNameValue = getString(R.string.download_choice_1)
                    download()

                }
                radioButton2.isChecked -> {
                    URL = getString(R.string.download_url_2)
                    custom_button.buttonState = ButtonState.Loading
                    intentArg_fileNameValue = getString(R.string.download_choice_2)

                    download()
                }
                radioButton3.isChecked -> {
                    URL = getString(R.string.download_url_3)
                    custom_button.buttonState = ButtonState.Loading
                    intentArg_fileNameValue = getString(R.string.download_choice_3)

                    download()
                }
                else -> {
                    Toast.makeText(
                        this,
                        getString(R.string.no_download_option_chosen),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val action = intent?.action
            val query = DownloadManager.Query()
            id?.let { query.setFilterById(it) }
            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.notification_description),
                    Toast.LENGTH_SHORT
                ).show()
                custom_button.buttonState = ButtonState.Completed
                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                when (cursor.getInt(columnIndex)) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        intentArg_statusValue = "Success"
                    }
                    else -> {
                        intentArg_statusValue = "Fail"
                    }
                }
                setNotification()

            }


        }

    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
//                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun ceateNotificationChannel() {
        val name = "notif channel"
        val desc = "Description"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = desc
        channel.enableVibration(true)
        channel.enableLights(true)

        channel.lightColor = Color.RED

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    private fun setNotification() {

        val detailActivityIntent = Intent(applicationContext, DetailActivity::class.java)
        detailActivityIntent.putExtra(intentArg_fileName, intentArg_fileNameValue)
        detailActivityIntent.putExtra(intentArg_status, intentArg_statusValue)

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            detailActivityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_description))
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .addAction(
                R.drawable.ic_assistant_black_24dp,
                applicationContext.getString(R.string.notification_button),
                pendingIntent
            )
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.notify(NOTIFICATION_ID, notification)
    }


    companion object {
        const val intentArg_fileName = "Filename"
        const val intentArg_status = "Status"
        var intentArg_fileNameValue = "Filename"
        var intentArg_statusValue = "Status"


        private var URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"

        private const val CHANNEL_ID = "channelId"
        private const val NOTIFICATION_ID = 1


    }

}
