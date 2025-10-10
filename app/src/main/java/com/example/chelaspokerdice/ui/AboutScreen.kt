package com.example.chelaspokerdice.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.chelaspokerdice.R

@Composable
fun AboutScreen() {
    Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
        Text(stringResource(R.string.about),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = 150.dp)
            )

        Text(stringResource(R.string.about_welcome),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 30.dp))

        Text(stringResource(R.string.about_start_match),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp))

        Text(stringResource(R.string.about_gameplay_desc),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(5.dp))

        Text(stringResource(R.string.about_dice_combinations),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(5.dp))

        val context = LocalContext.current

        Text(stringResource(R.string.about_full_rules),
            color = Color.Blue,
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 10.dp)
                .clickable{
            val intent = Intent(Intent.ACTION_VIEW,
                "https://gathertogethergames.com/poker-dice#google_vignette".toUri())
            context.startActivity(intent)
        })

        Text(stringResource(R.string.about_made_by),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 30.dp))

        IconButton(onClick = {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:01106950@pw.edu.pl".toUri()
            }

            context.startActivity(intent)
        }) {
            Icon(imageVector = Icons.Outlined.Email,
                contentDescription = "Profile"
            )
        }

    }

}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun AboutScreenPreview() {
    AboutScreen()
}