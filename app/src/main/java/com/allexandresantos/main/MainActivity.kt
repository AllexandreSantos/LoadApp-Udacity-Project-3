package com.allexandresantos.main

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.allexandresantos.R
import com.allexandresantos.databinding.ActivityMainBinding
import com.allexandresantos.receiver.DownloadReceiver


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this.application)).get(MainViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setSupportActionBar(binding.toolbar)

//        registerReceiver(DownloadReceiver(), IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        observeViewModel()

    }

    private fun observeViewModel() {

        viewModel.action.observe(this){
            it?.getContentIfNotHandled()?.let { action ->
                when(action){
                    MainViewModel.MainAction.SelectDownloadToast -> showToast()
                }
            }
        }

    }

    private fun showToast(){
        Toast.makeText(this, getString(R.string.choose_file), Toast.LENGTH_SHORT).show()
    }

}
