/*
 * Copyright 2023 Zhao-YinGang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.zhaoyingang.permission.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dylanc.viewbinding.nonreflection.binding
import com.zhaoyingang.permission.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by binding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnKtBasic.setOnClickListener {
            requestBasicDemo()
        }

        binding.btnKt.setOnClickListener {
            requestDemo()
        }

        binding.btnJava.setOnClickListener {
            RequestDemo4j.run(this)
        }
    }
}