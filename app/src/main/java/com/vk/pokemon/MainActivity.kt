package com.vk.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.vk.pokemon.model.Pokemon
import com.vk.pokemon.ui.theme.background
import com.vk.pokemon.ui.theme.card

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel: PokemonViewModel by viewModels()
        val loadThreshold = 10
        setContent {
            val pokemons by mainViewModel.pokemons.collectAsState()
            mainViewModel.fetchPokemons(loadThreshold)
            Surface(
                modifier = Modifier
                    .background(background)
                    .padding(horizontal = 15.dp)
                    .fillMaxSize()
            ) {
                LazyColumn(modifier = Modifier.background(background).padding(bottom = 5.dp)) {
                    items(pokemons.size) {
                        if (it >= pokemons.size - loadThreshold) {
                            mainViewModel.fetchPokemons(loadThreshold)
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                        )
                        PokemonCard(pokemons[it])
                    }
                }
            }
        }
    }

    @Composable
    fun PokemonCard(pokemon: Pokemon) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(pokemon.sprite)
                .build()
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(background),
            colors = CardDefaults.cardColors(
                containerColor = card,
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp, bottom = 10.dp, end = 10.dp)
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(96.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .align(Alignment.CenterVertically)
                )
                {
                    Text(
                        text = pokemon.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "height: ${pokemon.height} decimetres",
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = "weight: ${pokemon.weight} hectograms",
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${pokemon.baseExperience}",
                        fontSize = 40.sp,
                        color = Color.Blue,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Exp",
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color.Blue,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}