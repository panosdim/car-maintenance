package com.panosdim.carmaintenance.model

import kotlinx.serialization.Serializable

@Serializable
data class Service(var date: String = "", var odometer: String = "", var nextService: String = "")
