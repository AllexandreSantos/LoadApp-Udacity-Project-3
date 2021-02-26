package com.allexandresantos.main

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.allexandresantos.R
import com.allexandresantos.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this.application)).get(MainViewModel::class.java)
    }

    private lateinit var downloadManager: DownloadManager

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setSupportActionBar(binding.toolbar)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        observeViewModel()

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            var downloadStatus = ""

            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            val cursor: Cursor = downloadManager.query(DownloadManager.Query().setFilterById(id!!))

            if (cursor.moveToFirst()) {
                when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {

                    DownloadManager.STATUS_FAILED -> {
                        downloadStatus = "Failed"
                    }
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        downloadStatus = "Success"
                    }
                }
            }

            viewModel.setDownloadComplete(downloadStatus)
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
            it?.let { binding.customButton.setState(it) }
        }

    }

    private fun showFinishedDownloadToast() {
        Toast.makeText(this, getString(R.string.your_download_has_finished), Toast.LENGTH_SHORT).show()
    }

    private fun showChooseFileToast(){
        Toast.makeText(this, getString(R.string.choose_file), Toast.LENGTH_SHORT).show()
    }

}