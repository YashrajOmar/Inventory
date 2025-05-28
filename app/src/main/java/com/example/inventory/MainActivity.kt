/*
 * Copyright (C) 2023 The Android Open Source Project
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
 */
package com.example.inventory

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.inventory.ui.theme.InventoryTheme
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        enableEdgeToEdge()
//        super.onCreate(savedInstanceState)
//        setContent {
//            InventoryTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                ) {
//                    InventoryApp()
//                }
//            }
//        }
//    }
//}
//
//
//
//val remoteConfig = Firebase.remoteConfig
//
//val configSettings = remoteConfigSettings {
//    minimumFetchIntervalInSeconds = 3600 // 1 hour
//}
//remoteConfig.setConfigSettingsAsync(configSettings)
//
//// Optional: Default values in case fetch fails
//val defaults = mapOf(
//    "firebase_api_key" to "default_or_blank_key"
//)
//remoteConfig.setDefaultsAsync(defaults)



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // ✅ Setup Remote Config
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(
            mapOf("firebase_api_key" to "default_or_blank_key")
        )

        // ✅ Fetch the config values
        lifecycleScope.launch {
            try {
                remoteConfig.fetchAndActivate().await()
                val apiKey = remoteConfig.getString("firebase_api_key")
                Log.d("RemoteConfig", "Fetched Firebase API Key: $apiKey")
                // Pass this key to your login logic as needed
            } catch (e: Exception) {
                Log.e("RemoteConfig", "Failed to fetch remote config", e)
            }
        }

        setContent {
            InventoryTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    InventoryApp()
                }
            }
        }
    }
}
