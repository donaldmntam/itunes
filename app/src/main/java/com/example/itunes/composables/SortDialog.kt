@file:Suppress("MoveLambdaOutsideParentheses")

package com.example.itunes.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.itunes.R
import com.example.itunes.models.DialogModel
import com.example.itunes.models.Sort

@Composable
fun SortDialog(
    onDismissRequest: () -> Unit,
    model: DialogModel.Sort,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(1F),
            shape = RectangleShape,
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    "Select a sort type",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Spacer(modifier = Modifier.height(14.dp))
                Option(
                    modifier = Modifier.fillMaxWidth(),
                    selected = model.sort is Sort.SongName,
                    text = "By Song Name",
                    onClick = { model.onChangeSort(Sort.SongName); onDismissRequest() },
                )
                Spacer(modifier = Modifier.height(0.dp))
                Option(
                    modifier = Modifier.fillMaxWidth(),
                    selected = model.sort is Sort.AlbumName,
                    text = "By Album Name",
                    onClick = { model.onChangeSort(Sort.AlbumName); onDismissRequest() },
                )
            }
        }
    }
}

@Composable
private fun Option(
    modifier: Modifier = Modifier,
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick,
        ).padding(8.dp)
    ) {
        Icon(
            painter = painterResource(
                if (selected) {
                    R.drawable.ic_radio_button_checked
                } else {
                    R.drawable.ic_radio_button_unchecked
                }
            ),
            contentDescription = text,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}
