package com.example.itunes.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itunes.R
import com.example.itunes.models.Sort
import com.example.itunes.models.SortDirection
import com.example.itunes.ui.theme.fontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueryPanel(
    modifier: Modifier = Modifier,
    albumNameKeyword: String,
    onAlbumNameKeywordChange: (String) -> Unit,
    songNameKeyword: String,
    onSongNameKeywordChange: (String) -> Unit,
    sort: Sort,
    onSortButtonClick: () -> Unit,
    sortDirection: SortDirection,
    onSortDirectionButtonClick: () -> Unit,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.End,
        ) {
            MyTextField("Search Song Name", songNameKeyword, onSongNameKeywordChange)
            MyTextField("Search Album Name", albumNameKeyword, onAlbumNameKeywordChange)
            Row {
                MyButton(
                    onClick = onSortButtonClick,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_sort),
                        contentDescription = "Sort",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.width(width = 8.dp))
                    Text(
                        sort.title,
                        fontFamily = fontFamily,
                        fontSize = 18.sp,
                    )
                }
                Spacer(
                    modifier = Modifier.width(8.dp)
                )
                MyButton(
                    onClick = onSortDirectionButtonClick,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(
                            when (sortDirection) {
                                is SortDirection.Ascending -> R.drawable.ic_sort_ascending
                                is SortDirection.Descending -> R.drawable.ic_sort_descending
                            },
                        ),
                        contentDescription = "Sort Direction",
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label, fontFamily = fontFamily) },
        value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            textColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            placeholderColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        ),
        trailingIcon = {
            if (value.isNotEmpty()) Icon(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                   onValueChange("")
                },
                imageVector = Icons.Sharp.Clear,
                contentDescription = "Clear",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    )
}

val Sort.title: String
    get() = when (this) {
        Sort.SongName -> "Song Name"
        Sort.AlbumName -> "Album Name"
    }