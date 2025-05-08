package org.classapp.sleepwell.utils

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import org.classapp.sleepwell.R

@Composable
fun ProfileImage(photoUrl: String?) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(photoUrl)
            .crossfade(true)
            .build(),
        contentDescription = "Profile picture",
        placeholder = painterResource(R.drawable.profile_avatar_placeholder_large),
        modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp))
    )
}

fun updateFirebaseAuthProfile(username: String?, photoUrl: String?) {
    val user = FirebaseAuth.getInstance().currentUser
    val newPhotoUri = photoUrl?.toUri()

    user?.updateProfile(
        userProfileChangeRequest {
            displayName = username
            photoUri = newPhotoUri
        }
    )
}