package com.panosdim.carmaintenance.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Service(var date: String = "", var odometer: String = "", var nextService: String = "") :
    Parcelable
