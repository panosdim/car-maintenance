package com.panosdim.carmaintenance.model

import kotlinx.serialization.Serializable

@Serializable
data class TimingBelt(var date: String = "", var odometer: String = "")
