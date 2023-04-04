package com.panosdim.carmaintenance.ui

import android.content.Intent
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.firebase.ui.auth.AuthUI
import com.panosdim.carmaintenance.LoginActivity
import com.panosdim.carmaintenance.ui.theme.CarMaintenanceTheme
import com.panosdim.carmaintenance.user

@Composable
fun SignOut() {
    val context = LocalContext.current

    FilledIconButton(onClick = {
        AuthUI.getInstance()
            .signOut(context)
            .addOnCompleteListener {
                user = null
                val intent = Intent(context, LoginActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }
    }) {
        Icon(
            Icons.Outlined.Logout,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignOutPreview() {
    CarMaintenanceTheme {
        SignOut()
    }
}
