package com.panosdim.carmaintenance.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.Service
import com.panosdim.carmaintenance.model.Tyres
import com.panosdim.carmaintenance.padding
import com.panosdim.carmaintenance.ui.theme.CarMaintenanceTheme
import com.panosdim.carmaintenance.utils.getFormattedNumber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TyresCard(selectedCar: Car, updateCar: (car: Car) -> Unit) {
    var openDialog by rememberSaveable { mutableStateOf(false) }

    Card(
        onClick = { openDialog = true },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(padding)) {
            Text(
                text = stringResource(R.string.tyres),
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
                    selectedCar.tyres.date,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    "${getFormattedNumber(selectedCar.tyres.odometer)} km",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Spacer(Modifier.padding(padding))
            Text(
                text = stringResource(R.string.next_change),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(Modifier.padding(padding))
            Text(
                "${getFormattedNumber(selectedCar.tyres.nextChange)} km",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineMedium
            )
        }

        UpdateCarTyresDialog(
            openDialog = openDialog,
            closeDialog = { openDialog = false },
            selectedCar = selectedCar
        ) { updateCar(it) }

    }
}


@Preview(showBackground = true)
@Composable
fun TyresCardPreview() {
    val selectedCar = Car(
        name = "Test Car",
        service = Service(date = "03-04-2023", odometer = "150000", nextService = "175000"),
        tyres = Tyres(date = "23-07-2020", odometer = "42000", nextChange = "80000")
    )

    CarMaintenanceTheme {
        TyresCard(selectedCar) {}
    }
}