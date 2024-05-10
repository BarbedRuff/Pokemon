package com.vk.pokemon

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.vk.pokemon.model.Pokemon
import com.vk.pokemon.ui.theme.card
import com.vk.pokemon.ui.theme.progressBarBackground
import com.vk.pokemon.ui.theme.progressBarFill
import com.vk.pokemon.ui.theme.statCard

class PokemonActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pokemon = intent.extras?.getSerializable("pokemon") as Pokemon
        setContent {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon.sprite)
                    .build()
            )
            Surface(
                modifier = Modifier
                    .background(card)
                    .padding(horizontal = 15.dp, vertical = 15.dp)
                    .fillMaxSize()
            ){
                Column(modifier=Modifier
                    .background(card)
                    .fillMaxWidth()
                ) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(200.dp)
                            .padding(top=25.dp)
                    )
                    BaseCard(pokemon)
                    StatsCard(pokemon)
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = pokemon.name,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @Composable
    fun BaseCard(pokemon: Pokemon){
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .background(card),
            colors = CardDefaults.cardColors(
                containerColor = statCard,
            )
        ){
            Row(modifier = Modifier
                .padding(top=25.dp)){
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                ){
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text= "Height",
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text= "${pokemon.height}",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                ){
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text= "Weight",
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text= "${pokemon.weight}",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Text(
                modifier = Modifier
                    .padding(top=20.dp)
                    .fillMaxWidth(),
                text="Base experience",
                textAlign = TextAlign.Center,
                fontSize = 25.sp
            )
            Text(
                modifier = Modifier
                    .padding(bottom = 25.dp)
                    .fillMaxWidth(),
                text="${pokemon.baseExperience}",
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
    }

    @Composable
    fun StatsCard(pokemon: Pokemon) {
        Log.d("stats", pokemon.stats.joinToString(" "))
        val statsLabel = listOf("Hp", "Attack", "Defense", "Special attack", "Special defense", "Speed")
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .background(card),
            colors = CardDefaults.cardColors(
                containerColor = statCard,
            )
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 15.dp, start = 10.dp),
                text="Stats",
                fontSize = 25.sp
            )
            Column(
                modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 15.dp)
            ){
                for(i in 0..5){
                    Row{
                        Box(modifier=Modifier
                            .height(20.dp)
                            .weight(0.6f)
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)
                            .background(progressBarBackground)
                        ){
                            Box(modifier=Modifier
                                .fillMaxHeight()
                                .width(230.dp * pokemon.stats[i])
                                .background(progressBarFill)
                            )
                        }
                        Text(
                            modifier=Modifier.weight(0.4f).padding(start=5.dp),
                            text = statsLabel[i],
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier=Modifier.height(15.dp))
                }
            }
        }
    }
}