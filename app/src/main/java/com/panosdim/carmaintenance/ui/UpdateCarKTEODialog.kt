package com.panosdim.carmaintenance.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.KTEO
import com.panosdim.carmaintenance.paddingLarge
import com.panosdim.carmaintenance.paddingSmall
import com.panosdim.carmaintenance.utils.toEpochMilli
import com.panosdim.carmaintenance.utils.toFormattedString
import com.panosdim.carmaintenance.utils.toLocalDate
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCarKTEODialog(
    bottomSheetState: SheetState,
    car: Car,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()

    if (bottomSheetState.isVisible) {
        val date = remember {
            derivedStateOf {
                if (car.kteo.date.isBlank()) {
                    return@derivedStateOf LocalDate.now().plusYears(2)
                } else {
                    return@derivedStateOf car.kteo.date.toLocalDate()
                }
            }
        }
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = date.value.toEpochMilli())

        val exhaustDate = remember {
            derivedStateOf {
                if (car.kteo.exhaustCard.isBlank()) {
                    return@derivedStateOf LocalDate.now().plusYears(1)
                } else {
                    return@derivedStateOf car.kteo.exhaustCard.toLocalDate()
                }
            }
        }
        val exhaustDatePickerState =
            rememberDatePickerState(initialSelectedDateMillis = exhaustDate.value.toEpochMilli())

        ModalBottomSheet(
            onDismissRequest = { scope.launch { bottomSheetState.hide() } },
            sheetState = bottomSheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingLarge)
                    .imePadding()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(paddingSmall)
            ) {
                Text(
                    stringResource(id = R.string.car_kteo_title),
                    style = MaterialTheme.typography.headlineMedium
                )

                OutlinedDatePicker(
                    state = datePickerState,
                    label = stringResource(R.string.car_kteo_date),
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedDatePicker(
                    state = exhaustDatePickerState,
                    label = stringResource(R.string.car_exhaust_date),
                    modifier = Modifier.fillMaxWidth(),
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        enabled = datePickerState.selectedDateMillis != null && exhaustDatePickerState.selectedDateMillis != null,
                        onClick = {
                            val newKTEO = datePickerState.selectedDateMillis?.toLocalDate()?.let {
                                exhaustDatePickerState.selectedDateMillis?.toLocalDate()
                                    ?.let { exhaust ->
                                        KTEO(
                                            date = it.toFormattedString(),
                                            exhaustCard = exhaust.toFormattedString(),
                                        )
                                    }
                            }

                            if (newKTEO != null) {
                                car.kteo = newKTEO

                                viewModel.updateCar(car)

                                Toast.makeText(
                                    context, R.string.car_kteo_message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            scope.launch { bottomSheetState.hide() }
                        },
                    ) {
                        Icon(
                            Icons.Filled.Save,
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(stringResource(id = R.string.update))
                    }
                }
            }
        }
    }
}