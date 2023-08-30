package com.panosdim.carmaintenance.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.paddingLarge
import com.panosdim.carmaintenance.paddingSmall
import com.panosdim.carmaintenance.utils.toLocalDate
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KTEOCard(selectedCar: Car) {
    var openDialog by rememberSaveable { mutableStateOf(false) }

    Box(contentAlignment = Alignment.TopEnd) {
        Card(
            onClick = { openDialog = true },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(paddingLarge)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.NoCrash,
                        contentDescription = null,
                        modifier = Modifier.size(
                            MaterialTheme.typography.displaySmall.fontSize.value.dp
                        )
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = stringResource(R.string.kteo),
                        style = MaterialTheme.typography.displaySmall
                    )
                }
                Spacer(Modifier.padding(paddingSmall))
                Text(
                    selectedCar.kteo.date,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(Modifier.padding(paddingLarge))
                Text(
                    text = stringResource(R.string.exhaust_card),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.headlineLarge
                )
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
            )
        }

        if (selectedCar.kteo.date.isNotEmpty()) {
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