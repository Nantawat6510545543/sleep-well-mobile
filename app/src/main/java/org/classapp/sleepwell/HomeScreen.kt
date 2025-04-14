package org.classapp.sleepwell

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen() {
    val user = FirebaseAuth.getInstance().currentUser   // TODO update user state when logout

    if (user != null) {
        Column {
            Text(text = "Welcome, UID: ${user.uid}")
            Spacer(modifier = Modifier.height(16.dp))
            SignOutButton()     // TODO redirect to Login Page + update UI state
        }
    }
}

@Composable
fun SignOutButton() {
    Button(onClick = {
        FirebaseAuth.getInstance().signOut()
    }) {
        Text(text = "Logout")
    }
}


//        val db = Firebase.firestore
//        db.collection("test").get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    Log.d(TAG, "${document.id} => ${document.data}")
//                    val data = document.data
//                    val name = data["name"] as String
//                    val age = data["age"] as Number
//                    setContent {
//                        MessageCard(name, age)
//                        FilledButtonExample(onClick = {})
//                    }
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d(TAG, "Error getting documents: ", exception)
//            }
