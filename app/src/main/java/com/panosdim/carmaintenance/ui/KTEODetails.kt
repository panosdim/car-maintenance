package com.panosdim.carmaintenance.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.NoCrash
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.utils.toLocalDate
import java.time.LocalDate

@Composable
fun KTEODetails(car: Car) {
    var openDialog by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.NoCrash, contentDescription = null, modifier = Modifier.size(48.dp))
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Column(modifier = Modifier.clickable(onClick = { openDialog = true })) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.kteo),
                    style = MaterialTheme.typography.headlineMedium
                )
                if (car.kteo.date.isNotEmpty()) {
                    StatusIcon(date = car.kteo.date.toLocalDate())
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    if (car.kteo.date.isNotEmpty()) {
                        Text(
                            text = car.kteo.date,
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
                    text = stringResource(R.string.exhaust_card),
                    style = MaterialTheme.typography.headlineSmall
                )
                if (car.kteo.exhaustCard.isNotEmpty()) {
                    StatusIcon(date = car.kteo.exhaustCard.toLocalDate())
                }
                Text(
                    car.kteo.exhaustCard,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }

    UpdateCarKTEODialog(
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        car = car
    )
}

@Composable
fun StatusIcon(date: LocalDate) {
    if (date.isBefore(LocalDate.now())
    ) {
        Icon(
            Icons.Default.Error,
            modifier = Modifier.padding(12.dp),
            contentDescription = null,
            tint = Color.Red
        )
    } else if (date.minusWeeks(1).isBefore(LocalDate.now())) {
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