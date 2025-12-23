package com.example.chelaspokerdice.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chelaspokerdice.domain.Dice
import com.example.chelaspokerdice.domain.Player

@Composable
fun PlayerHand(player: Player) {

    Row (horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
        Text(player.name)
        if (!player.handIsEmpty()){
            player.currentHand.forEach { dice ->
                Image(painter = painterResource(LocalResources.current.getIdentifier(dice.symbol, "drawable", LocalContext.current.packageName)),
                    contentDescription = dice.symbol,
                    modifier = Modifier.size(30.dp).padding(start = 5.dp))
            }
        }


    }
}


//@Composable
//@Preview(showBackground = true, showSystemUi = true)
//fun PlayerHandPreview(){
//    val hand = listOf(
//        Dice(9, "nine", 1),
//        Dice(10, "ten", 2),
//        Dice(10, "ten", 3),
//        Dice(10, "ten", 4),
//        Dice(10, "ten", 5)
//    )
//    PlayerHand(player = Player("Keira", hand))
//}