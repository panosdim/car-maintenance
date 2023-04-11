package com.panosdim.carmaintenance.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.Service
import com.panosdim.carmaintenance.ui.theme.CarMaintenanceTheme
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
    updateCarService: (car: Car) -> Unit
) {
    val context = LocalContext.current

    val openDatePickerDialog = remember { mutableStateOf(false) }
    val source = remember { MutableInteractionSource() }
    if (source.collectIsPressedAsState().value) {
        openDatePickerDialog.value = true
    }

    if (openDialog) {
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
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDatePickerDialog.value = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        AlertDialog(
            onDismissRequest = closeDialog,
            title = {
                Text(
                    text = "Update Car Service"
                )
            },
            text = {
                Column {
                    datePickerState.selectedDateMillis?.toLocalDate()?.let {
                        TextField(
                            label = { Text(text = "Service Date") },
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
                            placeholder = {
                                Text(
                                    text = "Type the name of the new car"
                                )
                            },
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
//                        if (carName.isNotBlank()) {
//                            val newCar = Car(
//                                name = carName,
//                            )
//                            updateCarService(newCar)
//
//                            Toast.makeText(
//                                context, "Car Service Updated Successfully.",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }

                        closeDialog()
                    }
                ) {
                    Text(
                        text = "Update"
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = closeDialog
                ) {
                    Text(
                        text = "Dismiss"
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateCarDialogPreview() {
    val selectedCar = Car(
        name = "Test Car",
        service = Service(date = "03-04-2023", odometer = "150.000km", nextService = "175.000km")
    )
    CarMaintenanceTheme {
        UpdateCarServiceDialog(true, {}, selectedCar) {}
    }
}