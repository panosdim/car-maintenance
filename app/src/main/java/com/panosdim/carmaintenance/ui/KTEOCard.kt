package com.panosdim.carmaintenance.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.KTEO
import com.panosdim.carmaintenance.model.Service
import com.panosdim.carmaintenance.model.Tyres
import com.panosdim.carmaintenance.padding
import com.panosdim.carmaintenance.ui.theme.CarMaintenanceTheme
import com.panosdim.carmaintenance.utils.toLocalDate
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KTEOCard(selectedCar: Car, updateCar: (car: Car) -> Unit) {
    var openDialog by rememberSaveable { mutableStateOf(false) }
    val redrawIcon = remember { mutableStateOf(false) }

    Box(contentAlignment = Alignment.TopEnd) {
        Card(
            onClick = { openDialog = true },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.kteo),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.displayMedium
                )
                Spacer(Modifier.padding(padding))
                Text(
                    selectedCar.kteo.date,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(Modifier.padding(padding))
                Text(
                    text = stringResource(R.string.exhaust_card),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(Modifier.padding(padding))
                Text(
                    selectedCar.kteo.exhaustCard,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            UpdateCarKTEODialog(
                openDialog = openDialog,
                closeDialog = { openDialog = false },
                selectedCar = selectedCar
            ) { updateCar(it); redrawIcon.value = !redrawIcon.value }
        }

        if (redrawIcon.value) {
            StatusIcon(selectedCar = selectedCar)
        } else {
            StatusIcon(selectedCar = selectedCar)
        }
    }
}

@Composable
fun StatusIcon(selectedCar: Car) {
    if (selectedCar.kteo.date.toLocalDate()
            .isBefore(LocalDate.now()) || selectedCar.kteo.exhaustCard.toLocalDate()
            .isBefore(LocalDate.now())
    ) {
        Icon(
            Icons.Default.Error,
            modifier = Modifier.padding(12.dp),
            contentDescription = null,
            tint = Color.Red
        )
    } else if (selectedCar.kteo.date.toLocalDate().minusWeeks(1)
            .isBefore(LocalDate.now()) || selectedCar.kteo.exhaustCard.toLocalDate()
            .minusWeeks(1).isBefore(LocalDate.now())
    ) {
        Icon(
            Icons.Default.Warning,
            modifier = Modifier.padding(12.dp),
            contentDescription = null,
            tint = Color.Yellow
        )
    } else {
        Icon(
            Icons.Default.CheckCircle,
            modifier = Modifier.padding(12.dp),
            contentDescription = null,
            tint = Color.Green
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KTEOCardPreview() {
    val selectedCar = Car(
        name = "Test Car",
        service = Service(date = "03-04-2023", odometer = "150000", nextService = "175000"),
        tyres = Tyres(date = "23-07-2020", odometer = "42000", nextChange = "80000"),
        kteo = KTEO(date = "15-06-2024", exhaustCard = "15-06-2023")
    )

    CarMaintenanceTheme {
        KTEOCard(selectedCar) {}
    }
}