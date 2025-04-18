package com.panosdim.carmaintenance.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cable
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.paddingLarge
import com.panosdim.carmaintenance.paddingSmall
import com.panosdim.carmaintenance.utils.getFormattedNumber
import com.panosdim.carmaintenance.utils.toFormattedString
import com.panosdim.carmaintenance.utils.toLocalDate
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimingBeltDetails(car: Car) {
    val scope = rememberCoroutineScope()
    val textSize = with(LocalDensity.current) {
        MaterialTheme.typography.headlineLarge.fontSize.toDp()
    }

    val skipPartiallyExpanded by remember { mutableStateOf(true) }

    val updateTimingBeltSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { scope.launch { updateTimingBeltSheetState.show() } })
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(modifier = Modifier.padding(paddingLarge)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(paddingSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Cable,
                        contentDescription = null,
                        modifier = Modifier.size(textSize)
                    )
                    Text(
                        text = stringResource(R.string.timing_belt),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
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
    }

    UpdateCarTimingBeltDialog(
        bottomSheetState = updateTimingBeltSheetState,
        car = car
    )
}