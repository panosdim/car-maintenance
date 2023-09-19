package com.panosdim.carmaintenance.model

import kotlinx.serialization.Serializable

@Serializable
data class Injector(var date: String = "", var odometer: String = "")
