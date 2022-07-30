package com.udacity

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*
import com.udacity.MainActivity.Companion.intentArg_fileName
import com.udacity.MainActivity.Companion.intentArg_status


class DetailActivity : AppCompatActivity() {
private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.layout.tvFileNmaeValue.apply {
            this.text=intent.extras?.getString(intentArg_fileName)?:"unKnown"
        }
        binding.layout.tvStatusValue.apply {
            this.text=intent.extras?.getString(intentArg_status)?:"unKnown"
        }
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancelAll()
        binding.layout.button.setOnClickListener {
            this.finish()
        }
        setSupportActionBar(toolbar)
    }

}
