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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.paddingSmall
import com.panosdim.carmaintenance.ui.theme.CarMaintenanceTheme
import com.panosdim.carmaintenance.utils.toEpochMilli
import com.panosdim.carmaintenance.utils.toFormattedString
import com.panosdim.carmaintenance.utils.toLocalDate
import kotlinx.coroutines.job
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewCarDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()

    if (openDialog) {
        var carName by rememberSaveable { mutableStateOf("") }
        val focusRequester = FocusRequester()

        val date = remember { LocalDate.now() }
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = date.toEpochMilli())

        AlertDialog(
            onDismissRequest = closeDialog,
            title = {
                Text(
                    text = stringResource(R.string.car_add_title)
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
                            val newCar = Car(
                                name = carName,
                                purchaseDate = datePickerState.selectedDateMillis?.toLocalDate()
                                    ?.toFormattedString() ?: LocalDate.now().toFormattedString()
                            )
                            viewModel.addNewCar(newCar)

                            Toast.makeText(
                                context, R.string.add_new_car_message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        closeDialog()
                    },
                    enabled = carName.isNotBlank()
                ) {
                    Text(
                        text = stringResource(R.string.add)
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
fun AddNewCarDialogPreview() {
    CarMaintenanceTheme {
        AddNewCarDialog(true) {}
    }
}