package com.samwrotethecode.locationpicker

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.samwrotethecode.locationpicker.presentation.HomeScreen
import com.samwrotethecode.locationpicker.presentation.LocationPermissionDialog
import com.samwrotethecode.locationpicker.ui.theme.LocationPickerTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocationPickerTheme {
                val fineLocationPermission =
                    rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

                var showMap by remember {
                    mutableStateOf(false)
                }

                var showPermissionReqDialog by remember {
                    mutableStateOf(true)
                }

                LaunchedEffect(showPermissionReqDialog) {
                    when {
                        fineLocationPermission.status.isGranted -> showMap = true
                        !showPermissionReqDialog -> showMap = true
                    }
                }

                when {
                    showMap -> HomeScreen()
                    else -> Column {
                        if (showPermissionReqDialog)
                            LocationPermissionDialog(
                                onDismissRequest = {
                                    showPermissionReqDialog = false
                                    fineLocationPermission.launchPermissionRequest()
                                },
                                message = stringResource(R.string.permission_request_text)
                            )
                    }
                }
            }
        }
    }
}