package com.panosdim.carmaintenance.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.paddingLarge
import com.panosdim.carmaintenance.paddingSmall
import com.panosdim.carmaintenance.utils.getFormattedNumber
import com.panosdim.carmaintenance.utils.toFormattedString
import com.panosdim.carmaintenance.utils.toLocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimingBeltCard(selectedCar: Car) {
    var openDialog by rememberSaveable { mutableStateOf(false) }

    val nextTimingBeltService =
        try {
            ((selectedCar.timingBelt?.odometer?.toInt() ?: 0) + 175000).toString()
        } catch (_: NumberFormatException) {
            "175000"
        }

    val nextTimingBeltDateService = selectedCar.timingBelt?.date?.toLocalDate()?.plusYears(10)
        ?.toFormattedString()

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
                    Icons.Outlined.Cable,
                    contentDescription = null,
                    modifier = Modifier.size(
                        MaterialTheme.typography.displaySmall.fontSize.value.dp
                    )
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(
                    text = stringResource(R.string.timing_belt),
                    style = MaterialTheme.typography.displaySmall
                )
            }
            Spacer(Modifier.padding(paddingSmall))
            selectedCar.timingBelt?.let {
                if (it.odometer == "0") {
                    Text(
                        "-",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.headlineMedium
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            it.date,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            getFormattedNumber(it.odometer),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            } ?: kotlin.run {
                Text(
                    "-",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Spacer(Modifier.padding(paddingLarge))
            Text(
                text = stringResource(R.string.next_service),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineLarge
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    nextTimingBeltDateService.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    getFormattedNumber(nextTimingBeltService),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        UpdateCarTimingBeltDialog(
            openDialog = openDialog,
            closeDialog = { openDialog = false },
            selectedCar = selectedCar
        )
    }
}