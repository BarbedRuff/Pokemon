package com.vk.pokemon

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.vk.pokemon.model.Pokemon
import com.vk.pokemon.ui.theme.background
import com.vk.pokemon.ui.theme.card
import com.vk.pokemon.ui.theme.statCard

class MainActivity : ComponentActivity() {
     val itimFamily = FontFamily(
         Font(R.font.itim_regular, FontWeight.Normal)
     )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel: PokemonViewModel by viewModels()
        val loadThreshold = 10
        setContent {
            val pokemons by mainViewModel.pokemons.collectAsState()
            val state by mainViewModel.state.collectAsState()
            mainViewModel.fetchPokemons(loadThreshold)
            Surface(
                modifier = Modifier
                    .background(background)
                    .padding(horizontal = 15.dp)
                    .fillMaxSize()
            ) {
                if(state == "Init"){
                    LoadingGif()
                }
                else if(state == "Error" && pokemons.isEmpty()){
                    RetryButton { mainViewModel.fetchPokemons(loadThreshold) }
                }
                else{
                    LazyColumn(
                        modifier = Modifier
                            .background(background)
                    ) {
                        items(pokemons.size) {
                            if (it >= pokemons.size - loadThreshold) {
                                mainViewModel.fetchPokemons(loadThreshold)
                            }
                            PokemonCard(pokemons[it])
                        }
                        item {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(25.dp)
                            )
                            if(state == "Error"){
                                RetryButton{
                                    mainViewModel.fetchPokemons(loadThreshold)
                                }
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(25.dp)
                                )
                            }
                        }
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

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp)
        )
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(background)
                .clip(RoundedCornerShape(20.dp))
                .clickable {
                    val intent = Intent(this, PokemonActivity::class.java)
                    intent.putExtra("pokemon", pokemon)
                    startActivity(intent)
                },
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
                        fontSize = 23.sp,
                        fontFamily = itimFamily
                    )
                    Text(
                        text = "height: ${pokemon.height} decimetres",
                        fontSize = 16.sp,
                        fontFamily = itimFamily
                    )
                    Text(
                        text = "weight: ${pokemon.weight} hectograms",
                        fontSize = 16.sp,
                        fontFamily = itimFamily
                    )
                }
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxSize()
                        .padding(end = 10.dp),
                    text = "${pokemon.baseExperience}",
                    fontFamily = itimFamily,
                    fontSize = 30.sp,
                    color = Color.Blue,
                    textAlign = TextAlign.End
                )
            }
        }
    }

    @Composable
    fun LoadingGif(){
        val imageLoader = ImageLoader.Builder(this)
            .components{
                if(Build.VERSION.SDK_INT >= 28){
                    add(ImageDecoderDecoder.Factory())
                }
                else{
                    add(GifDecoder.Factory())
                }
            }
            .build()
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(this).data(R.drawable.loading).build(), imageLoader=imageLoader
        )
        Box(modifier = Modifier.background(background)){
            Image(
                modifier = Modifier
                    .size(96.dp)
                    .align(Alignment.Center),
                painter=painter,
                contentDescription = null,
                contentScale = ContentScale.None
            )
        }
    }

    @Composable
    fun RetryButton(onClick: () -> Unit){
        Box(modifier= Modifier
            .fillMaxWidth()
            .background(background)){
            Button(
                onClick = onClick,
                modifier=Modifier
                    .align(Alignment.Center),
                colors = ButtonDefaults.buttonColors(containerColor = statCard)
            ){
                Text(
                    text = "Retry",
                    fontFamily = itimFamily,
                    fontSize = 20.sp
                )
            }
        }
    }
}