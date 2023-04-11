package com.panosdim.carmaintenance.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.Service
import com.panosdim.carmaintenance.padding
import com.panosdim.carmaintenance.ui.theme.CarMaintenanceTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCard(selectedCar: Car) {
    var openDialog by rememberSaveable { mutableStateOf(false) }

    Card(
        onClick = { openDialog = true },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(padding)) {
            Text(
                "Service",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(Modifier.padding(padding))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    selectedCar.service.date,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    selectedCar.service.odometer,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Spacer(Modifier.padding(padding))
            Text(
                "Next Service",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(Modifier.padding(padding))
            Text(
                selectedCar.service.nextService,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineMedium
            )
        }

        UpdateCarServiceDialog(
            openDialog = openDialog,
            closeDialog = { openDialog = false },
            selectedCar = selectedCar
        ) {}

    }
}


@Preview(showBackground = true)
@Composable
fun ServiceCardPreview() {
    val selectedCar = Car(
        name = "Test Car",
        service = Service(date = "03-04-2023", odometer = "150.000km", nextService = "175.000km")
    )

    CarMaintenanceTheme {
        ServiceCard(selectedCar)
    }
}