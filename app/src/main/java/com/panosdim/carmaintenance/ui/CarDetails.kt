package com.panosdim.carmaintenance.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.Service
import com.panosdim.carmaintenance.ui.theme.CarMaintenanceTheme

@Composable
fun CarDetails(selectedCar: Car?, updateCarService: (car: Car) -> Unit) {
    selectedCar?.let {
        Column {
            ServiceCard(it, updateCarService = { updateCarService(it) })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarDetailsPreview() {
    val selectedCar = Car(
        name = "Test Car",
        service = Service(date = "03-04-2023", odometer = "150.000km", nextService = "175.000km")
    )
    CarMaintenanceTheme {
        CarDetails(selectedCar) {}
    }
}