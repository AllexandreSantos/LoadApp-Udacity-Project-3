package com.allexandresantos.main

import android.app.Application
import android.app.DownloadManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.allexandresantos.R
import com.allexandresantos.receiver.DownloadReceiver
import com.allexandresantos.util.Event

class MainViewModel(val app: Application) : AndroidViewModel(app) {

    private var downloadType: DownloadType? = null

    val _action = MutableLiveData<Event<MainAction>>()

    val action: LiveData<Event<MainAction>>
        get() = _action


    fun setType(type: DownloadType){
        downloadType = type
        Log.d("oi", "setType: " + downloadType)
    }

    private fun download(type: DownloadType) {
        val request =
            DownloadManager.Request(Uri.parse(type.value))
                .setTitle(app.applicationContext. getString(R.string.app_name))
                .setDescription(app.applicationContext.getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = app.applicationContext.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    fun startDownload(){
        if (downloadType != null){
            downloadType?.let { download(it) }
        } else{
            _action.value = Event(MainAction.SelectDownloadToast)
        }
    }

    enum class DownloadType(val value: String){
        UDACITY("https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"),
        RETROFIT("https://github.com/square/retrofit/archive/master.zip"),
        GLIDE("https://github.com/bumptech/glide/archive/master.zip")
    }

    sealed class MainAction{
        object SelectDownloadToast: MainAction()
    }

}