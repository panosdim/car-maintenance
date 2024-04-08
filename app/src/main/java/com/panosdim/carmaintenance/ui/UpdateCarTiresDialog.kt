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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.Tires
import com.panosdim.carmaintenance.paddingSmall
import com.panosdim.carmaintenance.utils.toEpochMilli
import com.panosdim.carmaintenance.utils.toFormattedString
import com.panosdim.carmaintenance.utils.toLocalDate
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCarTiresDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    car: Car,
) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()

    if (openDialog) {
        var carTires by rememberSaveable {
            if (car.tires.odometer.isBlank()) {
                return@rememberSaveable mutableStateOf("")
            } else {
                return@rememberSaveable mutableStateOf(car.tires.odometer)
            }
        }
        var nextTiresChange by rememberSaveable {
            if (car.tires.odometer.isBlank()) {
                return@rememberSaveable mutableStateOf("")
            } else {
                return@rememberSaveable mutableStateOf(car.tires.nextChange)
            }
        }
        var nextTiresSwap by rememberSaveable {
            if (car.tires.odometer.isBlank()) {
                return@rememberSaveable mutableStateOf("")
            } else {
                return@rememberSaveable mutableStateOf(car.tires.nextSwap)
            }
        }
        val date = remember {
            derivedStateOf {
                if (car.tires.date.isBlank()) {
                    return@derivedStateOf LocalDate.now()
                } else {
                    return@derivedStateOf car.tires.date.toLocalDate()
                }
            }
        }
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = date.value.toEpochMilli())

        fun isFormValid(): Boolean {
            return carTires.isNotBlank() && nextTiresChange.isNotBlank() && nextTiresSwap.isNotBlank()
        }

        AlertDialog(
            onDismissRequest = closeDialog,
            title = {
                Text(text = stringResource(R.string.car_tires_title))
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(paddingSmall)) {
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
                                nextTiresSwap = (it.toInt() + 20000).toString()
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
                        value = nextTiresSwap,
                        onValueChange = {
                            nextTiresSwap = it
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.next_car_tires_swap_placeholder)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = nextTiresSwap.isBlank(),
                        singleLine = true,
                        suffix = { Text(text = stringResource(R.string.km)) },
                        label = { Text(text = stringResource(R.string.next_car_tires_swap)) },
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
                                nextChange = nextTiresChange,
                                nextSwap = nextTiresSwap
                            )

                        }

                        if (newTires != null) {
                            car.tires = newTires

                            viewModel.updateCar(car)

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