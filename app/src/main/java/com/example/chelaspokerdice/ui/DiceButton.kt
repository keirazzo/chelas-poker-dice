package com.example.chelaspokerdice.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalResources

@SuppressLint("DiscouragedApi")
@Composable
fun Dice(symbol: String){
    Button(onClick = {}, shape = RectangleShape, contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors( containerColor = Color.Transparent, contentColor = Color.Gray),
    ) {
        Image(painter = painterResource(LocalResources.current.getIdentifier(symbol, "drawable", LocalContext.current.packageName)),
            contentDescription = symbol,
            modifier = Modifier.size(75.dp).padding(0.dp))
    }
}