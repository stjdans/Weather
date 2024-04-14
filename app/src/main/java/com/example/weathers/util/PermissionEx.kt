package com.example.weathers.util

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

val LOCATION_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

fun AppCompatActivity.setUpLocationContract(onGranted: () -> Unit = {}, onDenied: () -> Unit = {}): ActivityResultLauncher<Array<String>> {
    return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
                    || permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
            -> onGranted()

            else -> onDenied()
        }
    }
}

fun AppCompatActivity.checkLocationPermissions(onGranted: () -> Unit, onRationnale: () -> Unit, onDenied: () -> Unit) {
    when {
        checkPermissionsAny(LOCATION_PERMISSIONS) -> onGranted()
        showRequestPermissionRationalesAny(LOCATION_PERMISSIONS) -> onRationnale()
        else -> onDenied()
    }
}

fun AppCompatActivity.checkPermission(permission: String) = ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
fun AppCompatActivity.checkPermissionsAny(permissions: Array<String>) =
    permissions.map { checkPermission(it) }.also { println("상태 : $it") }.any { it }

fun AppCompatActivity.showRequestPermissionRationale(permission: String) = ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
fun AppCompatActivity.showRequestPermissionRationalesAny(permissions: Array<String>) =
    permissions.map { showRequestPermissionRationale(it) }.any { it }

fun AppCompatActivity.checkLocationPermission() =
    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

fun AppCompatActivity.goToDeviceSettings() {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).run {
        data = Uri.fromParts("package", packageName, null)
        startActivity(this)
    }
}

fun AppCompatActivity.goToDeviceLocation() {
    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).run {
        startActivity(this)
    }
}