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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.NoCrash
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.paddingLarge
import com.panosdim.carmaintenance.paddingSmall
import com.panosdim.carmaintenance.utils.toLocalDate
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KTEODetails(car: Car) {
    val scope = rememberCoroutineScope()
    val textSize = with(LocalDensity.current) {
        MaterialTheme.typography.headlineLarge.fontSize.toDp()
    }

    val skipPartiallyExpanded by remember { mutableStateOf(true) }

    val updateKTEOSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { scope.launch { updateKTEOSheetState.show() } })
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
                        imageVector = Icons.Outlined.NoCrash,
                        contentDescription = null,
                        modifier = Modifier.size(textSize)
                    )
                    Text(
                        text = stringResource(R.string.kteo),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
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
                    KteoStatusIcon(date = car.kteo.date.toLocalDate())
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
                    KteoStatusIcon(date = car.kteo.exhaustCard.toLocalDate())
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
        bottomSheetState = updateKTEOSheetState,
        car = car
    )
}

@Composable
fun KteoStatusIcon(date: LocalDate) {
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