package com.panosdim.carmaintenance.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.TimingBelt
import com.panosdim.carmaintenance.utils.toEpochMilli
import com.panosdim.carmaintenance.utils.toFormattedString
import com.panosdim.carmaintenance.utils.toLocalDate
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCarTimingBeltDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    selectedCar: Car,
) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()

    if (openDialog) {
        val carTimingBelt = remember {
            selectedCar.timingBelt?.let {
                if (it.odometer.isBlank()) {
                    return@remember mutableStateOf("")
                } else {
                    return@remember mutableStateOf(it.odometer)
                }
            } ?: run {
                return@remember mutableStateOf("")
            }
        }

        val date = remember {
            derivedStateOf {
                selectedCar.timingBelt?.let {
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
                Column {
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
                            selectedCar.timingBelt = newTimingBelt

                            viewModel.updateCar(selectedCar)

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
