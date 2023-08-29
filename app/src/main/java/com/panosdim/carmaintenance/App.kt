package com.panosdim.carmaintenance

import android.content.BroadcastReceiver
import androidx.compose.ui.unit.dp


val paddingSmall = 4.dp
val paddingLarge = 8.dp
val paddingExtraLarge = 16.dp

lateinit var onComplete: BroadcastReceiver

const val CHANNEL_ID = "Car-Maintenance-Channel"
const val TAG = "Car-Maintenance-Tag"