package com.udacity

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.udacity.MainActivity.Companion.intentArg_fileName
import com.udacity.MainActivity.Companion.intentArg_status
import com.udacity.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*
import kotlin.concurrent.schedule


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.layout.tvFileNmaeValue.apply {
            this.text = intent.extras?.getString(intentArg_fileName) ?: "unKnown"
        }
        binding.layout.tvStatusValue.apply {
            this.text = intent.extras?.getString(intentArg_status) ?: "unKnown"
        }
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancelAll()
        binding.layout.button.setOnClickListener {
            with((binding.layout.button.parent as MotionLayout)) {
                transitionToStart()
                Timer().schedule(transitionTimeMs) {
                    finish()
                }
            }


        }
        setSupportActionBar(toolbar)
    }

}
