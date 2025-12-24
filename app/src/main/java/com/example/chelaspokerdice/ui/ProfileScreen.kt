package com.example.chelaspokerdice.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.chelaspokerdice.R
import com.example.chelaspokerdice.viewmodel.ProfileViewModel
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val user by viewModel.user.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Username") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("New Name") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (newName.isNotBlank()) {
                        viewModel.updateName(newName)
                        showEditDialog = false
                    }
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Cancel") }
            }
        )
    }

    Column {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            title = { Text(text = "Profile", style = MaterialTheme.typography.titleLarge) }
        )

        if (user == null) {
            Text("Loading profile...", modifier = Modifier.padding(16.dp))
        } else {
            val p = user!!
            val totalHands = p.handFrequency.values.sum()
            val winRate = if (p.gamesPlayed > 0) (p.gamesWon.toFloat() / p.gamesPlayed * 100).toInt() else 0

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Username: ${p.name}", modifier = Modifier.padding(10.dp))
                Button(onClick = {
                    newName = p.name
                    showEditDialog = true
                }) {
                    Text("Edit")
                }
            }

            Text("Games played: ${p.gamesPlayed}", modifier = Modifier.padding(horizontal = 10.dp))
            Text("Games won : ${p.gamesWon} ($winRate%)", modifier = Modifier.padding(horizontal = 10.dp))
            Text("Hand frequency: ", modifier = Modifier.padding(10.dp), style = MaterialTheme.typography.titleMedium)

            p.handFrequency.forEach { (handName, count) ->
                val percentage = if (totalHands > 0) (count.toFloat() / totalHands * 100).toInt() else 0
                val cleanName = handName.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = cleanName)
                    Text(text = "$percentage% ($count)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ProfileScreenPreview() {
    ProfileScreen()
}