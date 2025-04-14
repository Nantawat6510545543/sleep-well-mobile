package org.classapp.sleepwell

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen() {
    val user = FirebaseAuth.getInstance().currentUser
    Text(text = "Welcome, UID: ${user?.uid}")
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


//@Composable
//fun MessageCard(name: String, age: Number) {
//    Text(text = "Name= $name!, Age= $age")
//}
//
//@Composable
//fun FilledButtonExample(onClick: () -> Unit) {
//    Button (
//        onClick = { /* TODO */ },
//        modifier = Modifier.padding(top = 16.dp)
//    ) {
//        Text("Test Button")
//    }
//}