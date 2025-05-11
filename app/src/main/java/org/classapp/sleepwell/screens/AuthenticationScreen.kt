package org.classapp.sleepwell.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth


@Composable
fun AuthenticationScreen(
    onSignInResult: (FirebaseAuthUIAuthenticationResult) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = FirebaseAuthUIActivityResultContract(),
        onResult = onSignInResult
    )

    val currentUser = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(true) {
        if (currentUser != null) {
            // User is already signed in, simulate a success result
            onSignInResult(
                FirebaseAuthUIAuthenticationResult(
                    Activity.RESULT_OK, null
                )
            )
        } else {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
//                AuthUI.IdpConfig.GoogleBuilder().build()
            )

            val intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()

            launcher.launch(intent)
        }
    }
}