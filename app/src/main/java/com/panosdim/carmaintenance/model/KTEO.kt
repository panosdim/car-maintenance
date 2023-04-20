package com.panosdim.carmaintenance.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KTEO(var date: String = "", var exhaustCard: String = "") : Parcelable
