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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    lateinit var curPage : MutableState<Int>
    lateinit var isFirstPage : MutableState<Boolean>
    lateinit var isLastPage : MutableState<Boolean>

    /**
     * START
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            curPage = remember { mutableIntStateOf(0) }
            isFirstPage = remember { mutableStateOf(true) }
            isLastPage = remember { mutableStateOf(false) }
            RefreshComposition()
        }
    }

    /**
     * Launches when orientation changes
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setContent {
            RefreshComposition()
        }
    }


    /**
     * Launches EITHER at app starting OR when orientation changes
     */
    @Composable
    fun RefreshComposition(){
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(color=MaterialTheme.colorScheme.background),
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

    /**
     * App composition for portrait orientation
     */
    @Preview
    @Composable
    fun MainPortraitSurface(innerPadding : PaddingValues = PaddingValues(all=0.dp)){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    all=dimensionResource(R.dimen.mainSurfacePadding)
                ),
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

    /**
     * App composition for landscape orientation
     */
    @Preview
    @Composable
    fun MainLandscapeSurface(innerPadding : PaddingValues = PaddingValues(all=0.dp)){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    all=dimensionResource(R.dimen.mainSurfacePadding)
                ),
        ) {
            ImageSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(bottom=dimensionResource(R.dimen.extraLandImgBottomPadding))
            )
            TitleSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )
            ButtonSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
            )
        }
    }

    /**
     * Part of composition that includes image and its background
     */
    @Composable
    fun ImageSection(modifier : Modifier){
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .background(color= MaterialTheme.colorScheme.secondary)
                    .align(Alignment.Center)
                    .size(
                        width=dimensionResource(R.dimen.imgFrameWidth),
                        height=dimensionResource(R.dimen.imgFrameHeight)
                    )
                    .shadow(elevation = dimensionResource(R.dimen.shadowElevation))
            ){
                Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(all=dimensionResource(R.dimen.minImagePadding)),
                    painter = painterResource(id = getCurArtwork().artId),
                    contentDescription = stringResource(getCurArtwork().descriptionId),
                )
            }
        }
    }

    /**
     * Part of composition that includes title and subtitle
     */
    @Composable
    fun TitleSection(modifier : Modifier){
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text=stringResource(getCurArtwork().titleId),
                fontSize = dimensionResource(R.dimen.titleFontSize).value.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(getCurArtwork().artistId),
                fontSize = dimensionResource(R.dimen.subtitleFontSize).value.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
    }

    /**
     * Part of composition that includes buttons
     */
    @Composable
    fun ButtonSection(modifier : Modifier){
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Button(
                onClick = { goPrev() },
                enabled = !isFirstPage.value
            ){
                Text(text=stringResource(R.string.prev_btn))
            }
            Button(
                onClick = { goNext() },
                enabled = !isLastPage.value
            ){
                Text(text=stringResource(R.string.next_btn))
            }
        }
    }

    /**
     * Decreases value of [curPage]
     *
     * If value is zero, it's not changing, and makes "Prev" button non-enabled
     */
    fun goPrev(){
        isLastPage.value = false
        curPage.value--
        if (curPage.value == 0){
            isFirstPage.value = true
        }
        else if (curPage.value < 0){
            curPage.value++
        }
    }

    /**
     * Increases value of [curPage]
     *
     * If value is equal to arts count (length of [Arts] array), it's not changing, and makes "Next" button non-enabled
     */
    fun goNext(){
        isFirstPage.value = false
        curPage.value++
        if (curPage.value == Arts.size - 1){
            isLastPage.value = true
        }
        else if (curPage.value >= Arts.size){
            curPage.value--
        }
    }

    /**
     * Returning current showing art information as [Artwork] data class
     */
    fun getCurArtwork() : Artwork{
        return Arts[curPage.value]
    }
}