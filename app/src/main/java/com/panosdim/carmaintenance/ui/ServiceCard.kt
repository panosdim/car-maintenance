package com.panosdim.carmaintenance.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CarRepair
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCard(selectedCar: Car) {
    var openDialog by rememberSaveable { mutableStateOf(false) }

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
                    Icons.Outlined.CarRepair, contentDescription = null, modifier = Modifier.size(
                        MaterialTheme.typography.displaySmall.fontSize.value.dp
                    )
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(
                    text = stringResource(R.string.service),
                    style = MaterialTheme.typography.displaySmall
                )
            }
            Spacer(Modifier.padding(paddingSmall))
            if (selectedCar.service.date.isNotEmpty()) {
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
                        getFormattedNumber(selectedCar.service.odometer),
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
                text = stringResource(R.string.next_service),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                getFormattedNumber(selectedCar.service.nextService),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        UpdateCarServiceDialog(
            openDialog = openDialog,
            closeDialog = { openDialog = false },
            selectedCar = selectedCar
        )
    }
}