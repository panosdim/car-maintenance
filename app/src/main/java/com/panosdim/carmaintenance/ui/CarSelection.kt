package com.panosdim.carmaintenance.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.ui.theme.CarMaintenanceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarSelection(viewModel: MainViewModel) {
    val options = viewModel.cars.collectAsStateWithLifecycle(initialValue = emptyList())
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf("") }
    var openDialog by rememberSaveable { mutableStateOf(false) }

    if (options.value.isNotEmpty() && selectedOptionText.isBlank()) {
        selectedOptionText = options.value[0].name
        viewModel.changeSelectedCar(options.value[0])
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
            options.value.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption.name) },
                    onClick = {
                        selectedOptionText = selectionOption.name
                        viewModel.changeSelectedCar(selectionOption)
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
        viewModel = viewModel
    )
}

@Preview(showBackground = true)
@Composable
fun CarSelectionPreview() {
    val carsViewModel = MainViewModel()
    CarMaintenanceTheme {
        CarSelection(carsViewModel)
    }
}