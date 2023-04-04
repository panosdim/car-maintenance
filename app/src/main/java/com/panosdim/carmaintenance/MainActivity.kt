package com.panosdim.carmaintenance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panosdim.carmaintenance.ui.CarSelection
import com.panosdim.carmaintenance.ui.SignOut
import com.panosdim.carmaintenance.ui.theme.CarMaintenanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CarMaintenanceTheme {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp).height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically
                    ) {
                        CarSelection()
                        SignOut()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CarMaintenanceTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            Row(
                modifier = Modifier.padding(8.dp).height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically
            ) {
                CarSelection()
                SignOut()
            }
        }
    }
}