package com.allexandresantos.detail

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.allexandresantos.R
import com.allexandresantos.cancelNotifications
import com.allexandresantos.databinding.ActivityDetailBinding
import com.allexandresantos.main.MainActivity
import com.allexandresantos.main.MainViewModel
import com.allexandresantos.main.MainViewModelFactory
import com.allexandresantos.util.DOWNLOAD_NAME
import com.allexandresantos.util.DOWNLOAD_STATUS
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this, DetailViewModelFactory(this.application)).get(DetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_detail)

        viewModel.cancelNotifications()

        initViews()

    }

    private fun initViews() {

        title = getString(R.string.download_details)

        setSupportActionBar(binding.toolbar)

        intent.apply {
            getStringExtra(DOWNLOAD_NAME)?.let {
                Log.d("oi", "onCreate: " + it)
                binding.animatedFileNameTv.text = it
            }

            getStringExtra(DOWNLOAD_STATUS)?.let {
                Log.d("oi", "onCreate: " + it)
                binding.animatedStatusTv.text = it
            }
        }

        binding.okButton.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }

    }

}
