package com.allexandresantos.main

import android.app.Application
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.allexandresantos.R
import com.allexandresantos.custombutton.ButtonState
import com.allexandresantos.sendNotification
import com.allexandresantos.util.*

class MainViewModel(val app: Application) : AndroidViewModel(app) {

    private var downloadType: DownloadType? = null

    val _action = MutableLiveData<Event<MainAction>>()

    val action: LiveData<Event<MainAction>>
        get() = _action

    private val _buttonState = MutableLiveData<ButtonState>()

    val buttonState: LiveData<ButtonState>
        get() = _buttonState

    private var downloadName: String = ""

    fun startDownload(){
        if (downloadType != null){
            if (_buttonState.value == ButtonState.Loading) return
            downloadType?.let { download(it) }
        } else{
            _action.value = Event(MainAction.SelectFile)
        }
    }

    private fun download(type: DownloadType) {

        _buttonState.value = ButtonState.Loading

        downloadName = downloadType.toString()

        val request =
                DownloadManager.Request(Uri.parse(type.value))
                        .setTitle(app.applicationContext. getString(R.string.app_name))
                        .setDescription(app.applicationContext.getString(R.string.app_description))
                        .setRequiresCharging(false)
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)

        val downloadManager = app.applicationContext.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

    }

    fun setDownloadComplete(downloadStatus: String){

        _buttonState.value = ButtonState.Completed

        _action.value = Event(MainAction.DownloadHasFinished)

        val notificationManager = ContextCompat.getSystemService(
            app.applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(app.applicationContext, downloadName, downloadStatus)

    }

    fun setType(type: DownloadType){
        downloadType = type
    }


    enum class DownloadType(val value: String){
        UDACITY("https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"),
        RETROFIT("https://github.com/square/retrofit/archive/master.zip"),
        GLIDE("https://github.com/bumptech/glide/archive/master.zip");

        override fun toString(): String {
            return when(this){
                UDACITY -> UDACITY_DESCRIPTION
                RETROFIT -> RETROFIT_DESCRIPTION
                GLIDE -> GLIDE_DESCRIPTION
            }
        }

    }

    sealed class MainAction{
        object SelectFile: MainAction()
        object DownloadHasFinished: MainAction()
    }

}