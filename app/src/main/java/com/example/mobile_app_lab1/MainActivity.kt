package com.example.mobile_app_lab1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile_app_lab1.repository.viewModel.MainViewModel
import com.example.mobile_app_lab1.repository.model.Character
import com.example.mobile_app_lab1.ui.theme.Mobileapplab1Theme
import java.util.Vector
import androidx.lifecycle.LiveData


class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getData()
        setContent {
            Mobileapplab1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF2E2E2E)
                ) {
                    MainView(viewModel, onClick = { characterId: String -> navigateToDetails(characterId) })
                }
            }
        }
    }

    private fun navigateToDetails(characterId: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("characterId", characterId)
        startActivity(intent)
    }
}

//Griffindor
val gMain = Color(0xFF650000)
val gSecond = Color(0xFFE09C0A)
//
//Slitherin
val sMain = Color(0xFF2E761C)
val sSecond = Color(0xFFCCCCCC)
//
//Ravenclav
val rMain = Color(0xFF1B3956)
val rSecond = Color(0xFFB86623)
//
//Hufflepuff
val hMain = Color(0xFFFF9E0D)
val hSecond = Color(0xFF1F1E19)

enum class House {
    gryffindor, slytherin, ravenclaw, hufflepuff
}

fun houseImage(house: House): Int {
    return if(house == House.gryffindor)
        R.drawable.lion
    else if(house == House.slytherin)
        R.drawable.snake
    else if(house == House.ravenclaw)
        R.drawable.raven
    else
        R.drawable.badger
}

fun getHouseColors(house: House): Pair<Color, Color> {
    return if(house == House.gryffindor)
        Pair(gMain, gSecond)
    else if(house == House.slytherin)
        Pair(sMain, sSecond)
    else if(house == House.ravenclaw)
        Pair(rMain, rSecond)
    else
        Pair(hMain, hSecond)
}

fun houseFromString(house: String): House {
    return if(house == "Gryffindor")
        House.gryffindor
    else if(house == "Slytherin")
        House.slytherin
    else if(house == "Ravenclaw")
        House.ravenclaw
    else
        House.hufflepuff
}

@Composable
fun Tile(name: String, house: House, onClick: () -> Unit) {
    val (houseMainColor, houseSecondColor) = getHouseColors(house)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(houseMainColor, shape = RoundedCornerShape(10.dp))
            .padding(10.dp)
            .padding(start = 30.dp)
//            .width(300.dp)
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onClick.invoke() }
    ) {
        Image(
            painter = painterResource(id = houseImage(house)),
            contentDescription = "house icon",
            modifier = Modifier.height(50.dp)


        )
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = name, color = houseSecondColor, fontSize = 23.sp, fontFamily = FontFamily(Font(R.font.main_font)))
    }
}

@Composable
fun TileList(characters: List<Character>, onClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 10.dp)
    ) {
        items(characters) { character ->
            Tile(name = character.name, house = houseFromString(character.house), onClick = { onClick.invoke(character.id) })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    Log.d("Characters size", "Size: ${characters.size}")
}

@Composable
fun ErrorView() {
    Text(text = "Error occurred!", color = Color.Red, fontSize = 23.sp, fontFamily = FontFamily(Font(R.font.main_font)))
}

@Composable
fun LoadingView() {
    Text(text = "Loading characters", color = Color.White, fontSize = 23.sp, fontFamily = FontFamily(Font(R.font.main_font)))
}

@Composable
fun MainView(viewModel: MainViewModel, modifier: Modifier = Modifier, onClick: (String) -> Unit) {
    val uiState by viewModel.immutableHpData.observeAsState(UiState())

    when {
        uiState.isLoading -> {
            LoadingView()
        }
        uiState.error != null -> {
            ErrorView()
        }
        uiState.data != null -> {
            uiState.data?.let { TileList(characters = it, onClick = onClick) }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
//    viewModel.getData()
    Mobileapplab1Theme {
//    TileList()

    }
}