package com.panosdim.carmaintenance.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.Service
import com.panosdim.carmaintenance.ui.theme.CarMaintenanceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarSelection(
    options: List<Car>,
    changeSelectedCar: (car: Car) -> Unit,
    addNewCar: (car: Car) -> Unit
) {

    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf("") }
    var openDialog by rememberSaveable { mutableStateOf(false) }

    if (options.isNotEmpty() && selectedOptionText.isBlank()) {
        selectedOptionText = options[0].name
        changeSelectedCar(options[0])
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text("Car Selection") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption.name) },
                    onClick = {
                        selectedOptionText = selectionOption.name
                        changeSelectedCar(selectionOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }

            DropdownMenuItem(
                text = { Text("Add New Car") },
                onClick = {
                    openDialog = true
                    expanded = false
                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            )
        }
    }

    AddNewCarDialog(
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        addNewCar = { addNewCar(it) }
    )
}

@Preview(showBackground = true)
@Composable
fun CarSelectionPreview() {
    val options =
        listOf(
            Car(
                id = "1",
                name = "Opel Corsa",
                service = Service(date = "09-04-2023", "213.000km", "235.000km")
            ),
            Car(
                id = "2",
                name = "Opel Crossland",
                service = Service(date = "01-03-2023", "85.000km", "95.000km")
            )
        )
    CarMaintenanceTheme {
        CarSelection(options = options,
            changeSelectedCar = { },
            addNewCar = { })
    }
}