package com.example.itunes

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.itunes.composables.BottomShadow
import com.example.itunes.models.SongListResponse
import com.example.itunes.services.SongListProvider
import com.example.itunes.services.SongListProviderImpl
import com.example.itunes.ui.theme.ItunesTheme
import com.example.itunes.states.ScreenState
import com.example.itunes.viewmodels.ScreenViewModel
import com.example.itunes.viewmodels.ScreenViewModelFactory
import com.example.itunes.models.Sort
import com.example.itunes.models.SortDirection
import com.example.itunes.composables.SongItem
import com.example.itunes.composables.QueryPanel
import com.example.itunes.ui.theme.fontFamily
import kotlinx.coroutines.delay

class AlwaysFailSongListProvider : SongListProvider {
    override suspend fun fetch(): Result<SongListResponse> {
        delay(2000)
        return Result.failure(RuntimeException("Error!"))
    }
}

class RetrySongListProvider(
    queue: RequestQueue,
    private val failCount: Int = 1,
): SongListProvider {
    private val impl = SongListProviderImpl(queue)
    private var currentFailCount = 0

    override suspend fun fetch(): Result<SongListResponse> {
        if (currentFailCount < failCount) {
            currentFailCount++
            delay(2000)
            return Result.failure(RuntimeException("Error!"))
        }
        return impl.fetch()
    }
}

// TODO: make collapsible

@Suppress("ReplaceGetOrSet")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val queue = Volley.newRequestQueue(this)
        val songListProvider: SongListProvider =
            SongListProviderImpl(queue)
            // AlwaysFailSongListProvider()
            // RetrySongListProvider(queue)

        val viewModel = ViewModelProvider(this, ScreenViewModelFactory(songListProvider))
            .get(ScreenViewModel::class.java)

        super.onCreate(savedInstanceState)
        setContent {
            ItunesTheme(darkTheme = false) {
                Screen(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(viewModel: ScreenViewModel) {
    Column {
        CenterAlignedTopAppBar(
            title = { Text("Swiftie App", fontFamily = fontFamily, fontWeight = FontWeight.Medium) },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        )
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            val state by viewModel.state.collectAsState();
            Box(contentAlignment = Alignment.Center) {
                when (val state = state) {
                    is ScreenState.Loading -> LoadingContent()
                    is ScreenState.Ready -> ReadyContent(state = state)
                    is ScreenState.Error -> ErrorContent(state = state, reload = viewModel::reload)
                }
            }
        }
    }
}

@Composable
fun LoadingContent() {
    CircularProgressIndicator(
        modifier = Modifier
            .width(48.dp)
            .height(48.dp)
    )
}

@Composable
fun ReadyContent(
    modifier: Modifier = Modifier,
    state: ScreenState.Ready,
) {
    var songNameKeyword by remember { mutableStateOf("") }
    var albumNameKeyword by remember { mutableStateOf("") }
    var sort: Sort by remember { mutableStateOf(Sort.SongName) }
    var sortDirection: SortDirection by remember { mutableStateOf(SortDirection.Ascending) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        QueryPanel(
            modifier = Modifier.fillMaxWidth(),
            songNameKeyword = songNameKeyword,
            onSongNameKeywordChange = { songNameKeyword = it},
            albumNameKeyword = albumNameKeyword,
            onAlbumNameKeywordChange = { albumNameKeyword = it },
            onSortButtonClick = { /* TODO */ }
        )
        Box(
            modifier = Modifier
                .weight(weight = 1F, fill = true),
        ) {
            Surface(
                color = MaterialTheme.colorScheme.background,
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(all = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    val displayedSongs = state.songs
                        .filter { song ->
                            val songNameMatched = song.trackName.lowercase()
                                .contains(songNameKeyword.lowercase())
                            val albumNameMatched = song.collectionName.lowercase()
                                .contains(albumNameKeyword.lowercase())
                            songNameMatched && albumNameMatched
                        }
                        .sortedWith { a, b ->
                            val result = when (sort) {
                                is Sort.SongName -> {
                                    val name1 = a.trackName.lowercase()
                                    val name2 = b.trackName.lowercase()
                                    name1 compareTo name2
                                }
                                is Sort.AlbumName -> {
                                    val name1 = a.collectionName.lowercase()
                                    val name2 = b.collectionName.lowercase()
                                    name1 compareTo name2
                                }
                            }
                            when (sortDirection) {
                                is SortDirection.Ascending -> result
                                is SortDirection.Descending -> -result
                            }
                        }
                    for (song in displayedSongs) {
                        item {
                            SongItem(song = song)
                        }
                    }
                }
                BottomShadow(
                    modifier = Modifier.align(Alignment.TopCenter),
                    height = 4.dp,
                    alpha = 0.1F,
                )
            }
        }
    }
}

@Composable
fun ErrorContent(
    modifier: Modifier = Modifier,
    state: ScreenState.Error,
    reload: () -> Unit,
) {
    // TODO: show error
    Column(
        modifier = modifier
            .fillMaxWidth(fraction = 0.8F)
            .fillMaxHeight(fraction = 0.8F),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "An error occurred when fetching a song list from iTunes!",
            textAlign = TextAlign.Center,
        )
        ElevatedButton(
            onClick = reload
        ) {
            Text("Reload")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ItunesTheme {
        Greeting("Android")
    }
}