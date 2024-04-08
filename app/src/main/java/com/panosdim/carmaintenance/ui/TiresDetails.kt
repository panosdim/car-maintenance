package com.panosdim.carmaintenance.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.TireRepair
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.utils.getFormattedNumber

@Composable
fun TiresDetails(car: Car) {
    var openDialog by rememberSaveable { mutableStateOf(false) }

    val nextTireSwap by rememberSaveable {
        if (car.tires.nextSwap.isBlank()) {
            try {
                return@rememberSaveable mutableStateOf((car.tires.odometer.toInt() + 20000).toString())
            } catch (_: NumberFormatException) {
                return@rememberSaveable mutableStateOf("20000")
            }
        } else {
            return@rememberSaveable mutableStateOf(car.tires.nextSwap)
        }
    }

    val nextTireChange by rememberSaveable {
        if (car.tires.nextChange.isBlank()) {
            try {
                return@rememberSaveable mutableStateOf((car.tires.odometer.toInt() + 40000).toString())
            } catch (_: NumberFormatException) {
                return@rememberSaveable mutableStateOf("40000")
            }
        } else {
            return@rememberSaveable mutableStateOf(car.tires.nextChange)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.TireRepair, contentDescription = null, modifier = Modifier.size(48.dp))
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Column(modifier = Modifier.clickable(onClick = { openDialog = true })) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.tires),
                    style = MaterialTheme.typography.headlineMedium
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(), horizontalAlignment = Alignment.End
                ) {
                    if (car.tires.date.isNotEmpty()) {
                        Text(
                            car.tires.date,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            getFormattedNumber(car.tires.odometer),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    } else {
                        Text(
                            "-",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.next_swap_tires),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    getFormattedNumber(nextTireSwap),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.next_change),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    getFormattedNumber(nextTireChange),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold
                )
            }

            UpdateCarTiresDialog(
                openDialog = openDialog,
                closeDialog = { openDialog = false },
                car = car
            )
        }
    }
}