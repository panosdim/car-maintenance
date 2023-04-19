package com.panosdim.carmaintenance.ui

import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.Service
import com.panosdim.carmaintenance.model.Tyres
import com.panosdim.carmaintenance.ui.theme.CarMaintenanceTheme
import com.panosdim.carmaintenance.utils.toEpochMilli
import com.panosdim.carmaintenance.utils.toFormattedString
import com.panosdim.carmaintenance.utils.toLocalDate
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCarTyresDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    selectedCar: Car,
    updateCar: (car: Car) -> Unit
) {
    val context = LocalContext.current

    val openDatePickerDialog = remember { mutableStateOf(false) }
    val source = remember { MutableInteractionSource() }
    if (source.collectIsPressedAsState().value) {
        openDatePickerDialog.value = true
    }

    if (openDialog) {
        var carTyres by rememberSaveable {
            if (selectedCar.tyres.odometer.isBlank()) {
                return@rememberSaveable mutableStateOf("")
            } else {
                return@rememberSaveable mutableStateOf(selectedCar.tyres.odometer)
            }
        }
        var nextTyresChange by rememberSaveable {
            if (selectedCar.tyres.odometer.isBlank()) {
                return@rememberSaveable mutableStateOf("")
            } else {
                return@rememberSaveable mutableStateOf(selectedCar.tyres.nextChange)
            }
        }
        val date = remember {
            derivedStateOf {
                if (selectedCar.tyres.date.isBlank()) {
                    return@derivedStateOf LocalDate.now()
                } else {
                    return@derivedStateOf selectedCar.tyres.date.toLocalDate()
                }
            }
        }
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = date.value.toEpochMilli())
        val confirmEnabled = remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }

        if (openDatePickerDialog.value) {
            DatePickerDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onDismissRequest.
                    openDatePickerDialog.value = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDatePickerDialog.value = false
                        },
                        enabled = confirmEnabled.value
                    ) {
                        Text(stringResource(R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDatePickerDialog.value = false
                        }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        fun isFormValid(): Boolean {
            return carTyres.isNotBlank() && nextTyresChange.isNotBlank()
        }

        AlertDialog(
            onDismissRequest = closeDialog,
            title = {
                Text(text = stringResource(R.string.car_tyres_title))
            },
            text = {
                Column {
                    datePickerState.selectedDateMillis?.toLocalDate()?.let {
                        OutlinedTextField(
                            label = { Text(text = stringResource(R.string.car_tyres_date)) },
                            readOnly = true,
                            interactionSource = source,
                            value = it
                                .toFormattedString(),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "Calendar Icon"
                                )
                            },
                            onValueChange = { },
                        )
                    }
                    OutlinedTextField(
                        value = carTyres,
                        onValueChange = { carTyres = it },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.car_tyres_placeholder)
                            )
                        },
                        suffix = { Text(text = stringResource(R.string.km)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = carTyres.isBlank(),
                        singleLine = true,
                        label = { Text(text = stringResource(R.string.car_tyres_change)) },
                    )
                    OutlinedTextField(
                        value = nextTyresChange,
                        onValueChange = { nextTyresChange = it },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.next_car_tyres_next_placeholder)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = nextTyresChange.isBlank(),
                        singleLine = true,
                        suffix = { Text(text = stringResource(R.string.km)) },
                        label = { Text(text = stringResource(R.string.next_car_tyres)) },
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val newTyres = datePickerState.selectedDateMillis?.toLocalDate()?.let {
                            Tyres(
                                date = it.toFormattedString(),
                                odometer = carTyres,
                                nextChange = nextTyresChange
                            )

                        }

                        if (newTyres != null) {
                            selectedCar.tyres = newTyres

                            updateCar(selectedCar)

                            Toast.makeText(
                                context, R.string.car_tyres_message,
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

@Preview(showBackground = true)
@Composable
fun UpdateCarTyresDialogPreview() {
    val selectedCar = Car(
        name = "Test Car",
        service = Service(date = "03-04-2023", odometer = "150000", nextService = "175000"),
        tyres = Tyres(date = "23-07-2020", odometer = "42000", nextChange = "80000")
    )
    CarMaintenanceTheme {
        UpdateCarTyresDialog(true, {}, selectedCar) {}
    }
}