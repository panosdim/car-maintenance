package com.panosdim.carmaintenance.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tires(var date: String = "", var odometer: String = "", var nextChange: String = "") :
    Parcelable
