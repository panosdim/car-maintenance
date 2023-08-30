package com.panosdim.carmaintenance.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Injector(var date: String = "", var odometer: String = "") : Parcelable
