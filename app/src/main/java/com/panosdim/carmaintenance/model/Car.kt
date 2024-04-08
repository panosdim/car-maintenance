package com.panosdim.carmaintenance.model

import kotlinx.serialization.Serializable


@Serializable
data class Car(
    var id: String? = null,
    var name: String = "",
    var purchaseDate: String = "",
    var service: Service = Service(),
    var tires: Tires = Tires(),
    var kteo: KTEO = KTEO(),
    var injector: Injector = Injector(),
    var timingBelt: TimingBelt = TimingBelt(),
)
