package com.panosdim.carmaintenance.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.TimingBelt
import com.panosdim.carmaintenance.paddingSmall
import com.panosdim.carmaintenance.utils.toEpochMilli
import com.panosdim.carmaintenance.utils.toFormattedString
import com.panosdim.carmaintenance.utils.toLocalDate
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCarTimingBeltDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    car: Car,
) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()

    if (openDialog) {
        val carTimingBelt = remember {
            car.timingBelt.let {
                if (it.odometer.isBlank()) {
                    return@remember mutableStateOf("")
                } else {
                    return@remember mutableStateOf(it.odometer)
                }
            }
        }

        val date = remember {
            derivedStateOf {
                car.timingBelt.let {
                    if (it.date.isBlank()) {
                        return@derivedStateOf LocalDate.now()
                    } else {
                        return@derivedStateOf it.date.toLocalDate()
                    }
                }
            }
        }
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = date.value?.toEpochMilli())

        fun isFormValid(): Boolean {
            return carTimingBelt.value.isNotBlank()
        }

        AlertDialog(
            onDismissRequest = closeDialog,
            title = {
                Text(text = stringResource(R.string.car_timing_belt_title))
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(paddingSmall)) {
                    OutlinedDatePicker(
                        state = datePickerState,
                        label = stringResource(id = R.string.car_service_date)
                    )
                    carTimingBelt.let { timingBelt ->
                        OutlinedTextField(
                            value = timingBelt.value,
                            onValueChange = {
                                timingBelt.value = it
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.car_timing_belt_placeholder)
                                )
                            },
                            suffix = { Text(text = stringResource(R.string.km)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = timingBelt.value.isBlank(),
                            singleLine = true,
                            label = { Text(text = stringResource(R.string.car_service)) },
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val newTimingBelt = datePickerState.selectedDateMillis?.toLocalDate()?.let {
                            TimingBelt(
                                date = it.toFormattedString(),
                                odometer = carTimingBelt.value,
                            )

                        }

                        if (newTimingBelt != null) {
                            car.timingBelt = newTimingBelt

                            viewModel.updateCar(car)

                            Toast.makeText(
                                context, R.string.car_timing_belt_message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        closeDialog()
                    },
                    enabled = isFormValid()
                ) {
                    Text(
                        text = stringResource(R.string.update)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = closeDialog
                ) {
                    Text(
                        text = stringResource(R.string.dismiss)
                    )
                }
            }
        )
    }
}
