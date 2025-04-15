package org.classapp.sleepwell.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HistoryScreen() {
    Text(
        text = "History Screen",
    )
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
