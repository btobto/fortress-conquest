package com.example.fortressconquest.common

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.StringRes
import com.example.fortressconquest.R
import com.example.fortressconquest.common.model.UiText
import com.example.fortressconquest.common.model.ValidationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState

fun validateEmail(input: String): ValidationResult {
    if (input.isBlank()) {
        return ValidationResult.Error(
            UiText.StringResource(
                resId = R.string.error_email_empty
            )
        )
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
        return ValidationResult.Error(
            UiText.StringResource(
                resId = R.string.error_email_invalid
            )
        )
    }

    return ValidationResult.Success
}

fun validatePassword(input: String): ValidationResult {
    if (input.length < Constants.PASSWORD_MIN_LENGTH) {
        return ValidationResult.Error(
            UiText.StringResource(
                resId = R.string.error_password_short,
                args = arrayOf(Constants.PASSWORD_MIN_LENGTH)
            )
        )
    }

    return ValidationResult.Success
}

fun validatePhoneNumber(input: String): ValidationResult {
    if (input.isBlank()) {
        return ValidationResult.Error(
            UiText.StringResource(
                resId = R.string.error_phone_empty
            )
        )
    }

    if (!Regex("^\\d+\$").matches(input)) {
        return ValidationResult.Error(
            UiText.StringResource(
                resId = R.string.error_phone_non_digits
            )
        )
    }

    return ValidationResult.Success
}

fun getErrorText(validationResult: ValidationResult): UiText? {
    return when (validationResult) {
        is ValidationResult.Error -> validationResult.error
        is ValidationResult.Success -> null
    }
}

fun showToast(context: Context, @StringRes messageId: Int) {
    Toast.makeText(
        context,
        UiText.StringResource(
            resId = messageId
        ).asString(context),
        Toast.LENGTH_SHORT
    ).show()
}

fun Activity.createOpenAppSettingsIntent(): Intent =
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    )

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}

suspend fun CameraPositionState.animateToLocation(
    location: Location,
    durationMs: Int = Int.MAX_VALUE
) {
    this.animate(
        update = CameraUpdateFactory.newLatLng(
            LatLng(location.latitude, location.longitude)
        ),
        durationMs = durationMs
    )
}

suspend fun CameraPositionState.animateToLocation(
    location: Location,
    zoom: Float,
    durationMs: Int = Int.MAX_VALUE
) {
    this.animate(
        update = CameraUpdateFactory.newLatLngZoom(
            LatLng(location.latitude, location.longitude),
            zoom
        ),
        durationMs = durationMs
    )
}

fun CameraPositionState.moveToLocation(location: Location) {
    this.move(
        update = CameraUpdateFactory.newLatLng(
            LatLng(location.latitude, location.longitude)
        )
    )
}

fun CameraPositionState.moveToLocation(location: Location, zoom: Float) {
    this.move(
        update = CameraUpdateFactory.newLatLngZoom(
            LatLng(location.latitude, location.longitude),
            zoom
        )
    )
}