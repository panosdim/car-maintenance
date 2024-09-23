package com.panosdim.carmaintenance.model

import kotlinx.serialization.Serializable

@Serializable
data class Tires(
    var date: String = "",
    var odometer: String = "",
    var nextChange: String = "",
    var nextSwap: String = "",
    var swapPerformed: Boolean = false
)
