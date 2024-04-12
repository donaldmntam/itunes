package com.example.itunes.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.itunes.models.DialogModel

@Composable
fun ErrorDialog(
    onDismissRequest: () -> Unit,
    model: DialogModel.Error,
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
                Text(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(1F, fill = false),
                    text = model.message,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onDismissRequest,
                        )
                        .align(Alignment.End),
                    text = "Ok",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
