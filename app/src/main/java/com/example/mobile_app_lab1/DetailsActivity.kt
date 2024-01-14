package com.example.mobile_app_lab1

import android.content.Intent
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mobile_app_lab1.repository.model.Character
import com.example.mobile_app_lab1.repository.viewModel.MainViewModel
import com.example.mobile_app_lab1.ui.theme.Mobileapplab1Theme

class DetailsActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val characterId: String? = intent.getStringExtra("characterId")

        characterId?.let {
            viewModel.getCharacterDetails(characterId)
        } ?: run {
            navigateToMain()
        }

        setContent {
            Mobileapplab1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF2E2E2E)
                ) {
                    MainView(viewModel, backToMain = { navigateToMain() })
                }
            }
        }

    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun Detail(name: String, content: String) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "$name: $content",
        color = Color.White,
        fontSize = 23.sp,
        fontFamily = FontFamily(Font(R.font.main_font))
    )
}

@Composable
fun CharacterDetailsView(character: Character, backToMain: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.padding(top=20.dp)
        ) {
            Text(text = "< Back to list",
                color = Color.White,
                fontSize = 33.sp,
                fontFamily = FontFamily(Font(R.font.main_font)),
                modifier = Modifier
                    .clickable { backToMain.invoke() }
                    .align(alignment = Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(
                model = character.image,
                contentDescription = "${character.name} image",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = character.name,
                color = Color.White,
                fontSize = 33.sp,
                fontFamily = FontFamily(Font(R.font.main_font))
            )
            Detail(name = "Gender", content = character.gender)
            Detail(name = "Species", content = character.species)
            Detail(name = "Ancestry", content = character.ancestry)
            Detail(name = "House", content = character.house)
            Detail(name = "Patronus", content = character.patronus)
        }
    }

}


@Composable
fun MainView(viewModel: MainViewModel, modifier: Modifier = Modifier, backToMain: () -> Unit) {
    val uiState by viewModel.immutableHpData.observeAsState(UiState())

    when {
        uiState.isLoading -> {
            LoadingView()
        }

        uiState.error != null -> {
            ErrorView()
        }

        uiState.data != null -> {
            uiState.data?.let {
                CharacterDetailsView(
                    character = it.first(),
                    backToMain = backToMain
                )
            }
        }
    }
}