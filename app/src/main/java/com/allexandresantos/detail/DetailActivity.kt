package com.allexandresantos.detail

import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.allexandresantos.R
import com.allexandresantos.cancelNotifications
import com.allexandresantos.databinding.ActivityDetailBinding
import com.allexandresantos.main.MainViewModel
import com.allexandresantos.main.MainViewModelFactory
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

        viewModel.test()

        setSupportActionBar(binding.toolbar)

    }

}
