package com.allexandresantos.main

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.allexandresantos.R


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this.application)).get(MainViewModel::class.java)
    }

    private lateinit var binding: com.allexandresantos.databinding.ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setSupportActionBar(binding.toolbar)

        observeViewModel()

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            viewModel.setDownloadComplete(intent)
        }
    }

    private fun observeViewModel() {

        viewModel.action.observe(this){
            it?.getContentIfNotHandled()?.let { action ->
                when(action){
                    MainViewModel.MainAction.SelectFile -> showChooseFileToast()
                    MainViewModel.MainAction.DownloadHasFinished -> showFinishedDownloadToast()
                }
            }
        }

        viewModel.buttonState.observe(this) {
            it?.getContentIfNotHandled()?.let { binding.customButton.setState(it) }
        }

    }

    private fun showFinishedDownloadToast() {
        Toast.makeText(this, getString(R.string.your_download_has_finished), Toast.LENGTH_SHORT).show()
    }

    private fun showChooseFileToast(){
        Toast.makeText(this, getString(R.string.choose_file), Toast.LENGTH_SHORT).show()
    }

}