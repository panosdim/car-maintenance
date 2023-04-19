package com.panosdim.carmaintenance.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Car(
    var id: String? = null,
    var name: String = "",
    var service: Service = Service(),
    var tyres: Tyres = Tyres()
) : Parcelable
