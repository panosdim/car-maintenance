package com.panosdim.carmaintenance.ui

import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.KTEO
import com.panosdim.carmaintenance.model.Service
import com.panosdim.carmaintenance.ui.theme.CarMaintenanceTheme
import com.panosdim.carmaintenance.utils.toEpochMilli
import com.panosdim.carmaintenance.utils.toFormattedString
import com.panosdim.carmaintenance.utils.toLocalDate
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCarKTEODialog(
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

    val openExhaustDatePickerDialog = remember { mutableStateOf(false) }
    val exhaustSource = remember { MutableInteractionSource() }
    if (exhaustSource.collectIsPressedAsState().value) {
        openExhaustDatePickerDialog.value = true
    }

    if (openDialog) {
        val date = remember {
            derivedStateOf {
                if (selectedCar.kteo.date.isBlank()) {
                    return@derivedStateOf LocalDate.now()
                } else {
                    return@derivedStateOf selectedCar.kteo.date.toLocalDate()
                }
            }
        }
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = date.value.toEpochMilli())

        val exhaustDate = remember {
            derivedStateOf {
                if (selectedCar.kteo.exhaustCard.isBlank()) {
                    return@derivedStateOf LocalDate.now()
                } else {
                    return@derivedStateOf selectedCar.kteo.exhaustCard.toLocalDate()
                }
            }
        }
        val exhaustDatePickerState =
            rememberDatePickerState(initialSelectedDateMillis = exhaustDate.value.toEpochMilli())
        val confirmEnabled = remember {
            derivedStateOf { datePickerState.selectedDateMillis != null && exhaustDatePickerState.selectedDateMillis != null }
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

        if (openExhaustDatePickerDialog.value) {
            DatePickerDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onDismissRequest.
                    openExhaustDatePickerDialog.value = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openExhaustDatePickerDialog.value = false
                        },
                        enabled = confirmEnabled.value
                    ) {
                        Text(stringResource(R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openExhaustDatePickerDialog.value = false
                        }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(state = exhaustDatePickerState)
            }
        }

        AlertDialog(
            onDismissRequest = closeDialog,
            title = {
                Text(text = stringResource(R.string.car_kteo_title))
            },
            text = {
                Column {
                    datePickerState.selectedDateMillis?.toLocalDate()?.let {
                        OutlinedTextField(
                            label = { Text(text = stringResource(R.string.car_kteo_date)) },
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
                    exhaustDatePickerState.selectedDateMillis?.toLocalDate()?.let {
                        OutlinedTextField(
                            label = { Text(text = stringResource(R.string.car_exhaust_date)) },
                            readOnly = true,
                            interactionSource = exhaustSource,
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
                }
            },
            confirmButton = {
                TextButton(
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
                            selectedCar.kteo = newKTEO

                            updateCar(selectedCar)

                            Toast.makeText(
                                context, R.string.car_kteo_message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        closeDialog()
                    },
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
fun UpdateCarKTEODialogPreview() {
    val selectedCar = Car(
        name = "Test Car",
        service = Service(date = "03-04-2023", odometer = "150000", nextService = "175000"),
        kteo = KTEO(date = "15-06-2024", exhaustCard = "15-06-2023")
    )
    CarMaintenanceTheme {
        UpdateCarKTEODialog(true, {}, selectedCar) {}
    }
}