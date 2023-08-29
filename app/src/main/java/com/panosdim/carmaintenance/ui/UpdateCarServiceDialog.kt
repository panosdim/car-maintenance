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
import com.panosdim.carmaintenance.model.Service
import com.panosdim.carmaintenance.utils.toEpochMilli
import com.panosdim.carmaintenance.utils.toFormattedString
import com.panosdim.carmaintenance.utils.toLocalDate
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCarServiceDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    selectedCar: Car,
) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()

    if (openDialog) {
        var carService by rememberSaveable {
            if (selectedCar.service.odometer.isBlank()) {
                return@rememberSaveable mutableStateOf("")
            } else {
                return@rememberSaveable mutableStateOf(selectedCar.service.odometer)
            }
        }
        var nextCarService by rememberSaveable {
            if (selectedCar.service.nextService.isBlank()) {
                return@rememberSaveable mutableStateOf("")
            } else {
                return@rememberSaveable mutableStateOf(selectedCar.service.nextService)
            }
        }
        val date = remember {
            derivedStateOf {
                if (selectedCar.service.date.isBlank()) {
                    return@derivedStateOf LocalDate.now()
                } else {
                    return@derivedStateOf selectedCar.service.date.toLocalDate()
                }
            }
        }
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = date.value.toEpochMilli())

        fun isFormValid(): Boolean {
            return carService.isNotBlank() && nextCarService.isNotBlank()
        }

        AlertDialog(
            onDismissRequest = closeDialog,
            title = {
                Text(text = stringResource(R.string.car_service_title))
            },
            text = {
                Column {
                    OutlinedDatePicker(
                        state = datePickerState,
                        label = stringResource(id = R.string.car_service_date)
                    )
                    OutlinedTextField(
                        value = carService,
                        onValueChange = {
                            carService = it
                            try {
                                nextCarService = (it.toInt() + 15000).toString()
                            } catch (_: NumberFormatException) {
                            }
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.car_service_placeholder)
                            )
                        },
                        suffix = { Text(text = stringResource(R.string.km)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = carService.isBlank(),
                        singleLine = true,
                        label = { Text(text = stringResource(R.string.car_service)) },
                    )
                    OutlinedTextField(
                        value = nextCarService,
                        onValueChange = { nextCarService = it },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.next_car_service_placeholder)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = nextCarService.isBlank(),
                        singleLine = true,
                        suffix = { Text(text = stringResource(R.string.km)) },
                        label = { Text(text = stringResource(R.string.next_car_service)) },
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val newService = datePickerState.selectedDateMillis?.toLocalDate()?.let {
                            Service(
                                date = it.toFormattedString(),
                                odometer = carService,
                                nextService = nextCarService
                            )

                        }

                        if (newService != null) {
                            selectedCar.service = newService

                            viewModel.updateCar(selectedCar)

                            Toast.makeText(
                                context, R.string.car_service_message,
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
