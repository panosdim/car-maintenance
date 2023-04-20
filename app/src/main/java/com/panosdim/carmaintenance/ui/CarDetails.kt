package com.panosdim.carmaintenance.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.KTEO
import com.panosdim.carmaintenance.model.Service
import com.panosdim.carmaintenance.model.Tyres
import com.panosdim.carmaintenance.padding
import com.panosdim.carmaintenance.ui.theme.CarMaintenanceTheme

@Composable
fun CarDetails(selectedCar: Car?, updateCar: (car: Car) -> Unit) {
    selectedCar?.let {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            ServiceCard(it, updateCar = { updateCar(it) })
            Spacer(Modifier.padding(padding))
            TyresCard(it, updateCar = { updateCar(it) })
            Spacer(Modifier.padding(padding))
            KTEOCard(it, updateCar = { updateCar(it) })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarDetailsPreview() {
    val selectedCar = Car(
        name = "Test Car",
        service = Service(date = "03-04-2023", odometer = "150000", nextService = "175000"),
        tyres = Tyres(date = "23-07-2020", odometer = "42000", nextChange = "80000"),
        kteo = KTEO(date = "15-06-2024", exhaustCard = "15-06-2023")
    )
    CarMaintenanceTheme {
        CarDetails(selectedCar) {}
    }
}