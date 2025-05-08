package org.classapp.sleepwell.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun NumericInputField(
    label: String,
    value: Int?,
    onValueChange: (Int?) -> Unit
) {
    OutlinedTextField(
        value = value?.toString() ?: "",
        onValueChange = {
            val newValue = it.toIntOrNull()  // Convert string input to Int or null
            onValueChange(newValue)  // Pass updated value to the parent
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun EditableField(
    label: String,
    value: String?,
    isEditing: Boolean,
    onValueChange: (String) -> Unit
) {
    if (isEditing) {
        OutlinedTextField(
            value = value ?: "",
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    } else {
        Text(
            text = "$label: ${value ?: "N/A"}",
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}