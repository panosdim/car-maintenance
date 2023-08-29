package com.panosdim.carmaintenance.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.Tires
import com.panosdim.carmaintenance.utils.toEpochMilli
import com.panosdim.carmaintenance.utils.toFormattedString
import com.panosdim.carmaintenance.utils.toLocalDate
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCarTiresDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    selectedCar: Car,
) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()

    if (openDialog) {
        var carTires by rememberSaveable {
            if (selectedCar.tires.odometer.isBlank()) {
                return@rememberSaveable mutableStateOf("")
            } else {
                return@rememberSaveable mutableStateOf(selectedCar.tires.odometer)
            }
        }
        var nextTiresChange by rememberSaveable {
            if (selectedCar.tires.odometer.isBlank()) {
                return@rememberSaveable mutableStateOf("")
            } else {
                return@rememberSaveable mutableStateOf(selectedCar.tires.nextChange)
            }
        }
        val date = remember {
            derivedStateOf {
                if (selectedCar.tires.date.isBlank()) {
                    return@derivedStateOf LocalDate.now()
                } else {
                    return@derivedStateOf selectedCar.tires.date.toLocalDate()
                }
            }
        }
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = date.value.toEpochMilli())

        fun isFormValid(): Boolean {
            return carTires.isNotBlank() && nextTiresChange.isNotBlank()
        }

        AlertDialog(
            onDismissRequest = closeDialog,
            title = {
                Text(text = stringResource(R.string.car_tires_title))
            },
            text = {
                Column {
                    OutlinedDatePicker(
                        state = datePickerState,
                        label = stringResource(R.string.car_tires_date)
                    )
                    OutlinedTextField(
                        value = carTires,
                        onValueChange = {
                            carTires = it
                            try {
                                nextTiresChange = (it.toInt() + 40000).toString()
                            } catch (_: NumberFormatException) {
                            }
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.car_tires_placeholder)
                            )
                        },
                        suffix = { Text(text = stringResource(R.string.km)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = carTires.isBlank(),
                        singleLine = true,
                        label = { Text(text = stringResource(R.string.car_tires_change)) },
                    )
                    OutlinedTextField(
                        value = nextTiresChange,
                        onValueChange = {
                            nextTiresChange = it
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.next_car_tires_next_placeholder)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = nextTiresChange.isBlank(),
                        singleLine = true,
                        suffix = { Text(text = stringResource(R.string.km)) },
                        label = { Text(text = stringResource(R.string.next_car_tires)) },
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val newTires = datePickerState.selectedDateMillis?.toLocalDate()?.let {
                            Tires(
                                date = it.toFormattedString(),
                                odometer = carTires,
                                nextChange = nextTiresChange
                            )

                        }

                        if (newTires != null) {
                            selectedCar.tires = newTires

                            viewModel.updateCar(selectedCar)

                            Toast.makeText(
                                context, R.string.car_tires_message,
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