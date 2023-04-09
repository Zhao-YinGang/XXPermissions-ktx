package com.zhaoyingang.permission.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dylanc.viewbinding.nonreflection.binding
import com.zhaoyingang.permission.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by binding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnLocationPermission.setOnClickListener {
            requestBleCameraLocationPermissions()
        }
    }
}