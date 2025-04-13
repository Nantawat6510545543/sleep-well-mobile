package org.classapp.sleepwell

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
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
//    }
//}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold (
                bottomBar = { BottomNavigationBar() }
            ) { /* TODO */ }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    val historyIcon: ImageVector = ImageVector.vectorResource(R.drawable.list_alt_24px)
    val analyticsIcon: ImageVector = ImageVector.vectorResource(R.drawable.analytics_24px)
    var selectedItem by remember { mutableIntStateOf(0) }
    // TODO refactor
    val items = listOf("Home", "History", "Analytics", "Profile")
    val selectedIcons = listOf(Icons.Filled.Home, historyIcon, analyticsIcon, Icons.Filled.Person)
    val unselectedIcons = listOf(Icons.Outlined.Home, historyIcon, analyticsIcon, Icons.Outlined.Person)

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item
                    )
                },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
        }
    }
}

@Composable
fun MessageCard(name: String, age: Number) {
    Text(text = "Name= $name!, Age= $age")
}

@Composable
fun FilledButtonExample(onClick: () -> Unit) {
    Button (
        onClick = { /* TODO */ },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text("Test Button")
    }
}