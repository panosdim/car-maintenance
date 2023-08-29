package com.panosdim.carmaintenance.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.KTEO
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
) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()

    if (openDialog) {
        val date = remember {
            derivedStateOf {
                if (selectedCar.kteo.date.isBlank()) {
                    return@derivedStateOf LocalDate.now().plusYears(2)
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
                    return@derivedStateOf LocalDate.now().plusYears(1)
                } else {
                    return@derivedStateOf selectedCar.kteo.exhaustCard.toLocalDate()
                }
            }
        }
        val exhaustDatePickerState =
            rememberDatePickerState(initialSelectedDateMillis = exhaustDate.value.toEpochMilli())

        AlertDialog(
            onDismissRequest = closeDialog,
            title = {
                Text(text = stringResource(R.string.car_kteo_title))
            },
            text = {
                Column {
                    OutlinedDatePicker(
                        state = datePickerState,
                        label = stringResource(R.string.car_kteo_date)
                    )
                    OutlinedDatePicker(
                        state = exhaustDatePickerState,
                        label = stringResource(R.string.car_exhaust_date)
                    )
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

                            viewModel.updateCar(selectedCar)

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