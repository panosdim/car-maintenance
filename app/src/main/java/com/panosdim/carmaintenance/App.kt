package com.panosdim.carmaintenance

import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

val padding = 8.dp

var user = FirebaseAuth.getInstance().currentUser
val database = FirebaseDatabase.getInstance()
const val CHANNEL_ID = "Car-Maintenance-Channel"
const val TAG = "Car-Maintenance-Tag"