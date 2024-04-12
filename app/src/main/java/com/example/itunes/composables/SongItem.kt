package com.example.itunes.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.itunes.models.Song
import com.example.itunes.ui.theme.fontFamily

@Composable
fun SongItem(modifier: Modifier = Modifier, song: Song) {
    Surface(
        modifier = modifier
            .aspectRatio(3F),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
    ) {
        Row {
            AsyncImage(
                modifier = modifier.aspectRatio(1F),
                model = song.artworkUrl100,
                contentDescription = "Artwork Image",
            )
            Column(
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp).fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
            ) {
                Box(
                    modifier = Modifier.weight(weight = 1F, fill = false)//.background(Color.Red)
                ) {
                    Text(
                        song.trackName,
                        modifier = Modifier.align(Alignment.CenterStart),
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary,
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Box {
                    Text(
                        song.collectionName,
                        modifier = Modifier.align(Alignment.CenterStart),
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp,
                        ),
                    )
                }
            }
        }
    }
}