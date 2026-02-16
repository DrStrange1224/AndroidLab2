package ru.laba

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    lateinit var state : MutableState<Int>

    //START
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            state = remember { mutableIntStateOf(0) }
            RefreshComposition()
        }
    }

    //for checking if orientation changed
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setContent {
            RefreshComposition()
        }
    }

    @Composable
    fun RefreshComposition(){
        Scaffold(
            modifier = Modifier.fillMaxWidth()
        ){
            innerPadding -> run {
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    MainLandscapeSurface(innerPadding)
                }else{
                    MainPortraitSurface(innerPadding)
                }
            }
        }
    }

    @Preview
    @Composable
    fun MainPortraitSurface(innerPadding : PaddingValues = PaddingValues(all=0.dp)){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start=30.dp, end=30.dp, top=30.dp), //TODO clear hardcode
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageSection(
                modifier = Modifier.weight(5f).fillMaxWidth()
            )
            TitleSection(
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
            ButtonSection(
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
        }
    }

    @Preview
    @Composable
    fun MainLandscapeSurface(innerPadding : PaddingValues = PaddingValues(all=0.dp)){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start=30.dp, end=30.dp, top=30.dp, bottom = 30.dp), //TODO clear hardcode
        ) {
            ImageSection(
                modifier = Modifier.fillMaxWidth().align(Alignment.Center).padding(bottom=60.dp) //TODO clear hardcode
            )
            TitleSection(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
            )
            ButtonSection(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart)
            )
        }
    }

    @Composable
    fun ImageSection(modifier : Modifier){
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .background(color=Color.Gray) //TODO clear hardcode
                    .align(Alignment.Center)
                    .size(width=250.dp, height=380.dp) //TODO clear hardcode
                    .shadow(elevation = 5.dp) //TODO clear hardcode
            ){
                Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top=20.dp, bottom=20.dp, start=20.dp, end=20.dp), //TODO clear hardcode
                    painter = painterResource(id = getCurArtwork().artId),
                    contentDescription = null
                )
            }
        }
    }

    @Composable
    fun TitleSection(modifier : Modifier){
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text=stringResource(getCurArtwork().titleId),
                fontSize = 32.sp, //TODO clear hardcode
                textAlign = TextAlign.Center
            )
            Text(
                text=stringResource(getCurArtwork().artistId),
                fontSize = 18.sp, //TODO clear hardcode
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    fun ButtonSection(modifier : Modifier){
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Button(
                onClick = { goPrev() }
            ){
                Text(text=stringResource(R.string.prev_btn))
            }
            Button(
                onClick = { goNext() }
            ){
                Text(text=stringResource(R.string.next_btn))
            }
        }
    }

    fun goPrev(){
        state.value--
        if (state.value < 0) state.value++
    }

    fun goNext(){
        state.value++
        if (state.value >= Arts.size) state.value--
    }

    fun getCurArtwork() : Artwork{
        return Arts[state.value]
    }
}