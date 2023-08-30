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
import com.panosdim.carmaintenance.model.Injector
import com.panosdim.carmaintenance.utils.toEpochMilli
import com.panosdim.carmaintenance.utils.toFormattedString
import com.panosdim.carmaintenance.utils.toLocalDate
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCarInjectorDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    selectedCar: Car,
) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()

    if (openDialog) {
        var carInjector by rememberSaveable {
            if (selectedCar.injector.odometer.isBlank()) {
                return@rememberSaveable mutableStateOf("")
            } else {
                return@rememberSaveable mutableStateOf(selectedCar.injector.odometer)
            }
        }

        val date = remember {
            derivedStateOf {
                if (selectedCar.injector.date.isBlank()) {
                    return@derivedStateOf LocalDate.now()
                } else {
                    return@derivedStateOf selectedCar.injector.date.toLocalDate()
                }
            }
        }
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = date.value.toEpochMilli())

        fun isFormValid(): Boolean {
            return carInjector.isNotBlank()
        }

        AlertDialog(
            onDismissRequest = closeDialog,
            title = {
                Text(text = stringResource(R.string.car_injector_title))
            },
            text = {
                Column {
                    OutlinedDatePicker(
                        state = datePickerState,
                        label = stringResource(id = R.string.car_service_date)
                    )
                    OutlinedTextField(
                        value = carInjector,
                        onValueChange = {
                            carInjector = it
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.car_injector_placeholder)
                            )
                        },
                        suffix = { Text(text = stringResource(R.string.km)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = carInjector.isBlank(),
                        singleLine = true,
                        label = { Text(text = stringResource(R.string.car_service)) },
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val newInjector = datePickerState.selectedDateMillis?.toLocalDate()?.let {
                            Injector(
                                date = it.toFormattedString(),
                                odometer = carInjector,
                            )

                        }

                        if (newInjector != null) {
                            selectedCar.injector = newInjector

                            viewModel.updateCar(selectedCar)

                            Toast.makeText(
                                context, R.string.car_injector_message,
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
