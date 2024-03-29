package com.panosdim.carmaintenance.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.TireRepair
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.paddingLarge
import com.panosdim.carmaintenance.paddingSmall
import com.panosdim.carmaintenance.utils.getFormattedNumber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiresCard(selectedCar: Car) {
    var openDialog by rememberSaveable { mutableStateOf(false) }

    val nextTireSwap =
        try {
            (selectedCar.tires.odometer.toInt() + 20000).toString()
        } catch (_: NumberFormatException) {
            ""
        }


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
                    Icons.Outlined.TireRepair, contentDescription = null, modifier = Modifier.size(
                        MaterialTheme.typography.displaySmall.fontSize.value.dp
                    )
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(
                    text = stringResource(R.string.tires),
                    style = MaterialTheme.typography.displaySmall
                )
            }
            Spacer(Modifier.padding(paddingSmall))
            if (selectedCar.tires.date.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        selectedCar.tires.date,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        getFormattedNumber(selectedCar.tires.odometer),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            } else {
                Text(
                    "-",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Spacer(Modifier.padding(paddingLarge))
            Text(
                text = stringResource(R.string.next_swap_tires),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                getFormattedNumber(nextTireSwap),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.padding(paddingSmall))
            Text(
                text = stringResource(R.string.next_change),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                getFormattedNumber(selectedCar.tires.nextChange),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        UpdateCarTiresDialog(
            openDialog = openDialog,
            closeDialog = { openDialog = false },
            selectedCar = selectedCar
        )

    }
}