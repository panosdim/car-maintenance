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
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.utils.getFormattedNumber
import com.panosdim.carmaintenance.utils.toFormattedString
import com.panosdim.carmaintenance.utils.toLocalDate


@Composable
fun TimingBeltDetails(car: Car) {
    var openDialog by rememberSaveable { mutableStateOf(false) }

    val nextTimingBeltService =
        try {
            (car.timingBelt.odometer.toInt() + 175000).toString()
        } catch (_: NumberFormatException) {
            "175000"
        }

    val nextTimingBeltDateService =
        if (car.timingBelt.date.isEmpty()) car.purchaseDate.toLocalDate().plusYears(10)
            .toFormattedString() else car.timingBelt.date.toLocalDate().plusYears(10)
            ?.toFormattedString()

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
            if (car.timingBelt.date.isNotEmpty()) {
                Column {
                    Text(
                        car.timingBelt.date,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        getFormattedNumber(car.timingBelt.odometer),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            } else {
                Text(
                    "-",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(), horizontalAlignment = Alignment.End
            ) {
                Text(
                    nextTimingBeltDateService.toString(),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    getFormattedNumber(nextTimingBeltService),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }

    UpdateCarTimingBeltDialog(
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        car = car
    )
}