package com.github.workspace.geministarter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.workspace.geministarter.ui.theme.GeministarterTheme
import com.google.ai.client.generativeai.GenerativeModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeministarterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val uiState by viewModel.uiState.collectAsState()

                    MainScreen(
                        uiState = uiState,
                        onResetButtonClick = viewModel::reset,
                        onCountryClick = viewModel::guess
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiState: MainUiState,
    onResetButtonClick: () -> Unit,
    onCountryClick: (Country) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Select Country") }
            )
        },
    ) { contentPadding ->
        when (uiState) {
            is MainUiState.Initial -> {
                CountryGrid(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxSize(),
                    countries = uiState.countries,
                    onCountryClick = onCountryClick
                )
            }

            is MainUiState.Success -> {
                Result(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxSize(),
                    country = uiState.country,
                    capital = uiState.capital,
                    onResetButtonClick = onResetButtonClick
                )
            }

            is MainUiState.Loading -> {
                Loading(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxSize()
                )
            }

            is MainUiState.Error -> {
                Error(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxSize(),
                    errorMessage = uiState.errorMessage,
                    onResetButtonClick = onResetButtonClick
                )
            }
        }
    }
}

@Composable
fun CountryGrid(
    modifier: Modifier = Modifier,
    countries: List<Country>,
    onCountryClick: (Country) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 150.dp)
    ) {
        items(
            countries,
            key = { country -> country.name }
        ) { country ->
            CountryItem(
                country = country,
                onClick = { onCountryClick(country) }
            )
        }
    }
}

@Composable
fun Result(
    modifier: Modifier = Modifier,
    country: Country,
    capital: String,
    onResetButtonClick: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = country.flag, style = MaterialTheme.typography.displayLarge)
            Text(text = country.name, style = MaterialTheme.typography.titleLarge)
            Text(text = capital, style = MaterialTheme.typography.headlineLarge)
            Button(onClick = onResetButtonClick) {
                Text(text = "Reset")
            }
        }
    }
}

@Composable
fun Loading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun Error(
    modifier: Modifier = Modifier,
    errorMessage: String,
    onResetButtonClick: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(all = 8.dp)
            )
            Button(onClick = onResetButtonClick) {
                Text(text = "Reset")
            }
        }
    }
}

@Composable
fun CountryItem(
    country: Country,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = country.flag, style = MaterialTheme.typography.displayLarge)
        Text(text = country.name, style = MaterialTheme.typography.titleLarge)
    }
}
