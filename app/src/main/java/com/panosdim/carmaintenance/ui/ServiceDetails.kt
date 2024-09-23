package com.panosdim.carmaintenance.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.utils.getFormattedNumber


@Composable
fun ServiceDetails(car: Car) {
    var openDialog by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.clickable(onClick = { openDialog = true })) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.previous),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(R.string.next),
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (car.service.date.isNotEmpty()) {
                Column {
                    Text(
                        car.service.date,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        getFormattedNumber(car.service.odometer),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            } else {
                Text(
                    "-",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Text(
                getFormattedNumber(car.service.nextService),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold
            )
        }
    }

    UpdateCarServiceDialog(
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        car = car
    )
}