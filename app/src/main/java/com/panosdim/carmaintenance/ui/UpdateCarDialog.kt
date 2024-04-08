package com.panosdim.carmaintenance.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.paddingSmall
import com.panosdim.carmaintenance.utils.toEpochMilli
import com.panosdim.carmaintenance.utils.toFormattedString
import com.panosdim.carmaintenance.utils.toLocalDate
import kotlinx.coroutines.job
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCarDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    car: Car,
) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()

    if (openDialog) {
        var carName by rememberSaveable { mutableStateOf(car.name) }
        val focusRequester = FocusRequester()

        val date = remember { car.purchaseDate.toLocalDate() }
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = date.toEpochMilli())

        AlertDialog(
            onDismissRequest = closeDialog,
            title = {
                Text(
                    text = stringResource(R.string.car_update_title)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(paddingSmall)) {
                    OutlinedTextField(
                        value = carName,
                        onValueChange = { carName = it },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.car_name_placeholder)
                            )
                        },
                        singleLine = true,
                        label = { Text(text = stringResource(R.string.car_name)) },
                        modifier = Modifier.focusRequester(focusRequester)
                    )
                    OutlinedDatePicker(
                        state = datePickerState,
                        label = stringResource(R.string.purchase_date)
                    )
                    LaunchedEffect(Unit) {
                        coroutineContext.job.invokeOnCompletion {
                            focusRequester.requestFocus()
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (carName.isNotBlank()) {
                            car.name = carName
                            car.purchaseDate = datePickerState.selectedDateMillis?.toLocalDate()
                                ?.toFormattedString() ?: LocalDate.now().toFormattedString()
                            viewModel.updateCar(car)

                            Toast.makeText(
                                context, R.string.update_car_message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        closeDialog()
                    },
                    enabled = carName.isNotBlank()
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