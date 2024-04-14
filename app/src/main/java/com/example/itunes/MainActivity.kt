@file:Suppress("MoveLambdaOutsideParentheses")

package com.example.itunes

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.itunes.composables.BottomShadow
import com.example.itunes.composables.ErrorDialog
import com.example.itunes.composables.QueryPanel
import com.example.itunes.composables.SongItem
import com.example.itunes.composables.SortDialog
import com.example.itunes.functions.filter
import com.example.itunes.functions.sort
import com.example.itunes.models.DialogModel
import com.example.itunes.models.SongListResponse
import com.example.itunes.models.Sort
import com.example.itunes.models.SortDirection
import com.example.itunes.services.SongListProvider
import com.example.itunes.services.SongListProviderImpl
import com.example.itunes.states.ScreenState
import com.example.itunes.ui.theme.ItunesTheme
import com.example.itunes.ui.theme.fontFamily
import com.example.itunes.viewmodels.ScreenViewModel
import com.example.itunes.viewmodels.ScreenViewModelFactory
import kotlinx.coroutines.delay


/**
 * This SongListProvider will always fail.
 */
class AlwaysFailSongListProvider : SongListProvider {
    override suspend fun fetch(): Result<SongListResponse> {
        delay(2000)
        return Result.failure(RuntimeException("Mocked Network Error!"))
    }
}

/**
 * This SongListProvider will fail for a specified number of times before an actual network call is
 * made to fetch the song list from the iTunes endpoint.
 * @param failCount The number of times of mocked network failures.
 */
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
            return Result.failure(RuntimeException("Mocked Network Error!"))
        }
        return impl.fetch()
    }
}

/**
 * This SongListProvider always returns an empty song list.
 */
class EmptySongListProvider : SongListProvider {
    override suspend fun fetch(): Result<SongListResponse> {
        delay(2000)
        return Result.success(
            SongListResponse(
                resultCount = 0,
                results = emptyList(),
            ),
        )
    }
}

@Suppress("ReplaceGetOrSet")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val queue = Volley.newRequestQueue(this)
        // NOTE: test different scenarios with these injected SongListProviders here
        val songListProvider: SongListProvider =
            SongListProviderImpl(queue)
            // AlwaysFailSongListProvider()
            // RetrySongListProvider(queue)
            // EmptySongListProvider()

        val viewModel = ViewModelProvider(this, ScreenViewModelFactory(songListProvider))
            .get(ScreenViewModel::class.java)

        super.onCreate(savedInstanceState)
        setContent {
            ItunesTheme(darkTheme = false) {
                Screen(viewModel, this::copyLink)
            }
        }
    }

    private fun copyLink(link: String) {
        val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("iTunes Link", link)
        clipboard.setPrimaryClip(clip)
    }
}

private val contentPadding = 24.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(
    viewModel: ScreenViewModel,
    copyLink: (String) -> Unit,
) {
    var currentDialogModel: DialogModel? by remember { mutableStateOf(null) }
    when(val model = currentDialogModel) {
        null -> Unit
        is DialogModel.Sort -> SortDialog(
            onDismissRequest = { currentDialogModel = null },
            model = model,
        )
        is DialogModel.Error -> ErrorDialog(
            onDismissRequest = { currentDialogModel = null },
            model = model,
        )
    }

    Column {
        CenterAlignedTopAppBar(
            title = {
                Text("Swiftie App", fontFamily = fontFamily, fontWeight = FontWeight.Medium)
            },
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
            when (val state = state) {
                is ScreenState.Loading -> LoadingContent()
                is ScreenState.Ready -> {
                    if (state.songs.isEmpty()) {
                        EmptyContent()
                    } else {
                        ReadyContent(
                            state = state,
                            showSortDialog = { dialog -> currentDialogModel = dialog },
                            copyLink = copyLink,
                        )
                    }
                }
                is ScreenState.Error -> ErrorContent(
                    state = state,
                    reload = viewModel::reload,
                    showErrorDialog = { dialog -> currentDialogModel = dialog },
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box {
        CircularProgressIndicator(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.padding(contentPadding),
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Looks like Taylor Swift got banned from iTunes because the fetched song list is empty!",
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ReadyContent(
    modifier: Modifier = Modifier,
    state: ScreenState.Ready,
    showSortDialog: (DialogModel.Sort) -> Unit,
    copyLink: (String) -> Unit,
) {
    require(state.songs.isNotEmpty()) { "Song list is unexpectedly empty!" }

    var songNameKeyword by remember { mutableStateOf("") }
    var albumNameKeyword by remember { mutableStateOf("") }
    var sort: Sort by remember { mutableStateOf(Sort.SongName) }
    var sortDirection: SortDirection by remember { mutableStateOf(SortDirection.Ascending) }

    val displayedSongs = state.songs
        .filter(
            songNameKeyword = songNameKeyword,
            albumNameKeyword = albumNameKeyword,
        )
        .sort(
            sort = sort,
            sortDirection = sortDirection,
        )

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
            sort = sort,
            onSortButtonClick = { showSortDialog(DialogModel.Sort(sort, { sort = it })) },
            sortDirection = sortDirection,
            onSortDirectionButtonClick = {
                sortDirection = when (sortDirection) {
                    is SortDirection.Ascending -> SortDirection.Descending
                    is SortDirection.Descending -> SortDirection.Ascending
                }
            }
        )
        Box(
            modifier = Modifier
                .weight(weight = 1F, fill = true),
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                if (displayedSongs.isEmpty()) {
                    Box(
                        modifier = Modifier.padding(contentPadding),
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = emptySearchMessage(
                                songNameKeyword = songNameKeyword,
                                albumNameKeyword = albumNameKeyword,
                            ),
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(all = 18.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        for (song in displayedSongs) {
                            item {
                                SongItem(
                                    modifier = Modifier.clickable { copyLink(song.trackViewUrl) },
                                    song = song,
                                )
                            }
                        }
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

@Composable
private fun ErrorContent(
    modifier: Modifier = Modifier,
    state: ScreenState.Error,
    reload: () -> Unit,
    showErrorDialog: (DialogModel.Error) -> Unit,
) {
    Column(
        modifier = modifier.padding(contentPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "An error occurred when fetching song list!",
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "See Error",
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                showErrorDialog(DialogModel.Error(state.error.toString()))
            },
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Icon(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = reload,
                )
                .size(36.dp),
            imageVector = Icons.Sharp.Refresh,
            contentDescription = "Retry",
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

fun emptySearchMessage(
    songNameKeyword: String,
    albumNameKeyword: String,
): String {
    if (songNameKeyword.isNotEmpty() && albumNameKeyword.isNotEmpty()) {
        return "No songs found with song name \"$songNameKeyword\" and album name \"$albumNameKeyword\""
    }
    if (albumNameKeyword.isNotEmpty()) {
        return "No songs found with album name \"$albumNameKeyword\""
    }
    return "No songs found with song name \"$songNameKeyword\""
}