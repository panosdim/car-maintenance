package com.panosdim.carmaintenance.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.ui.theme.CarMaintenanceTheme
import kotlinx.coroutines.job

@Composable
fun AddNewCarDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    viewModel: MainViewModel
) {
    val context = LocalContext.current

    if (openDialog) {
        var carName by rememberSaveable { mutableStateOf("") }
        val focusRequester = FocusRequester()

        AlertDialog(
            onDismissRequest = closeDialog,
            title = {
                Text(
                    text = "Add a new car"
                )
            },
            text = {
                Column {
                    TextField(
                        value = carName,
                        onValueChange = { carName = it },
                        placeholder = {
                            Text(
                                text = "Type the name of the new car"
                            )
                        },
                        modifier = Modifier.focusRequester(focusRequester)
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
                            )
                            viewModel.addNewCar(newCar)

                            Toast.makeText(
                                context, "New Car Added Successfully.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        
                        closeDialog()
                    }
                ) {
                    Text(
                        text = "Add"
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
fun AddNewCarDialogPreview() {
    val carsViewModel = MainViewModel()
    CarMaintenanceTheme {
        AddNewCarDialog(true, {}, carsViewModel)
    }
}