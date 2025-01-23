package com.example.sikhoapp.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.sikhoapp.model.AnimeListResponse

@Composable
fun ShowAnimeList(response: AnimeListResponse.AnimeData?, onItemClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(Color.White),
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        onClick = { onItemClick() }
    ) {
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
            Image(
                modifier = Modifier.width(140.dp),
                contentScale = ContentScale.Crop,
                painter = rememberAsyncImagePainter(model = response?.images?.webp?.imageUrl),
                contentDescription = ""
            )
            Column(modifier = Modifier.padding(5.dp)) {
                AnimInfo(label = "Title: ", response?.titleEnglish ?: response?.title.orEmpty())
                AnimInfo(label = "Episodes: ", response?.episodes.orEmpty())
                AnimInfo(label = "Rating: ", response?.rating.orEmpty())
            }

        }
    }
}

@Composable
fun AnimInfo(label: String, labelInfo: String) {
    Row {
        Text(
            modifier = Modifier
                .padding(start = 5.dp, top = 5.dp, bottom = 5.dp),
            lineHeight = 15.sp,
            fontSize = 13.sp, fontWeight = FontWeight.Bold, text = label
        )

        Text(
            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, end = 5.dp),
            lineHeight = 15.sp,
            fontSize = 13.sp,
            text = labelInfo
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ComposePreview() {
    ShowAnimeList(null){}
}