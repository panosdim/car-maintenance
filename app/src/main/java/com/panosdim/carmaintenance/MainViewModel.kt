package com.panosdim.carmaintenance

import androidx.lifecycle.ViewModel
import com.panosdim.carmaintenance.model.Car
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val repository = Repository()
    val cars = repository.getCars()

    private val _selectedCar = MutableStateFlow<Car?>(null)
    val selectedCar: StateFlow<Car?> = _selectedCar.asStateFlow()


    fun addNewCar(car: Car) {
        repository.addNewCar(car)
    }

    fun changeSelectedCar(selectedCar: Car) {
        _selectedCar.value = selectedCar
    }
}