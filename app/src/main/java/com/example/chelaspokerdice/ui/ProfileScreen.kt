package com.example.chelaspokerdice.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProfileScreen() {
    Text("Profile Screen", textAlign = TextAlign.Center)
}

@Composable
@Preview
fun ProfileScreenPreview() {
    ProfileScreen()
}