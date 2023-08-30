package com.panosdim.carmaintenance

import androidx.lifecycle.ViewModel
import com.panosdim.carmaintenance.model.Car

class MainViewModel : ViewModel() {
    private val repository = Repository()
    val cars = repository.getCars()

    fun addNewCar(car: Car) {
        repository.addNewCar(car)
    }

    fun updateCar(car: Car) {
        repository.updateCar(car)
    }

    fun deleteCar(car: Car) {
        repository.deleteCar(car)
    }

    fun signOut() {
        repository.signOut()
    }
}