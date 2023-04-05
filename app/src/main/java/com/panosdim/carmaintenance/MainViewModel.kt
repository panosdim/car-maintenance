package com.panosdim.carmaintenance

import androidx.lifecycle.ViewModel
import com.panosdim.carmaintenance.model.Car

class MainViewModel : ViewModel() {
    private val repository = Repository()
    val cars = repository.getCars()
    var selectedCar: Car? = null

    fun addNewCar(car: Car) {
        repository.addNewCar(car)
    }
}